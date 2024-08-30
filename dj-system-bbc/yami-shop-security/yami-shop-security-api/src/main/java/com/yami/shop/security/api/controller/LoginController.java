/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.api.controller;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.enums.SendType;
import com.yami.shop.bean.model.User;
import com.yami.shop.common.enums.StatusEnum;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PrincipalUtil;
import com.yami.shop.config.WxConfig;
import com.yami.shop.dao.UserMapper;
import com.yami.shop.security.api.dto.MaCodeAuthenticationDTO;
import com.yami.shop.security.api.dto.ShopLogin;
import com.yami.shop.security.api.dto.ShopLoginResult;
import com.yami.shop.security.api.dto.SocialAuthenticationDTO;
import com.yami.shop.security.api.util.JsonUtils;
import com.yami.shop.security.api.util.OkHttpUtils;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.dao.AppConnectMapper;
import com.yami.shop.security.common.dto.AuthenticationDTO;
import com.yami.shop.security.common.enums.SocialType;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.PasswordCheckManager;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.model.AppConnect;
import com.yami.shop.security.common.service.AppConnectService;
import com.yami.shop.security.common.vo.TokenInfoVO;
import com.yami.shop.service.SmsLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/6/30
 */
@RestController
@Slf4j
@Api(tags = "登录")
public class LoginController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AppConnectService appConnectService;

    @Autowired
    private PasswordCheckManager passwordCheckManager;

    @Autowired
    private SmsLogService smsLogService;

    @Autowired
    private PasswordManager passwordManager;

    @Autowired
    private AppConnectMapper appConnectMapper;

    @Autowired
    private WxConfig wxConfig;

    @PostMapping("/login")
    @ApiOperation(value = "账号密码(用于h5、pc登录)", notes = "通过账号/手机号/用户名密码登录，还要携带用户的类型，也就是用户所在的系统")
    public ServerResponseEntity<TokenInfoVO> login(
            @Valid @RequestBody AuthenticationDTO authenticationDTO) {
        String mobileOrUserName = authenticationDTO.getUserName();
        User user = getUser(mobileOrUserName);
        String decryptPassword = passwordManager.decryptPassword(authenticationDTO.getPassWord());

        // 半小时内密码输入错误十次，已限制登录30分钟
        passwordCheckManager.checkPassword(SysTypeEnum.ORDINARY,user.getUserMobile(),decryptPassword, user.getLoginPassword());

        return ServerResponseEntity.success(getTokenInfoVO(user, null));
    }

    @PostMapping("/shopLogin")
    @ApiOperation(value = "酒城e通2.0登录", notes = "需要APP端传入酒城e通token")
    public ServerResponseEntity<TokenInfoVO> shopLogin(@RequestBody ShopLogin shopLogin) {
        log.info("开始登录：{}", shopLogin);
        HashMap<String, String> requestMap = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + shopLogin.getAccessToken());
        requestMap.put("appZoneId", shopLogin.getAppId());
        //前端传入access_token 和 应用id 调用接口换取oauthCode
        String postResult = OkHttpUtils.okHttpFrom("http://192.168.5.108/one-access/oauth/authCode", headers, requestMap);
        JSONObject jsonObject = JSONObject.parseObject(postResult);
        if (jsonObject.getBoolean("success")) {
            requestMap = new HashMap<>();
            headers = new HashMap<>();
            String authorization = shopLogin.getAppId() + ":04d3ee7c6220642374e2f55c34cdbd3a36be6f56bd0d28419df42709eea589acc80327fbdf2b6cad0f18eef1daa5e9138fb9a626fd3483eb5e8d80faecf89f3a9f276dc3f424e164afb1ff67ff8f592210438fb81f7476ad580d38af9073d843d051d242f01c578935450f2cd956dbab70b548bc61d9e88c967305441b5d16a044";
            headers.put("Authorization","Basic " + Base64.encode(authorization));
            requestMap.put("grant_type", "oauthCode");
            requestMap.put("oauthCode", jsonObject.getJSONObject("data").getString("oauthCode"));
            //传入oauthCode 和 加密的应用id和秘钥 换取登录信息
            postResult = OkHttpUtils.okHttpFrom("http://192.168.5.108/one-access/oauth/token", headers, requestMap);
            jsonObject = JSONObject.parseObject(postResult);
            if (jsonObject.getBoolean("success")) {
                ShopLoginResult data = JsonUtils.fromJson(jsonObject.getString("data"), ShopLoginResult.class);
                AppConnect appConnect = null;
                if (StringUtils.isNotBlank(data.getDetail().getThirdId())) {
                    appConnect = appConnectService.getById(data.getDetail().getThirdId());
                }
                if(appConnect == null) {
                    appConnect = appConnectService.getByBizUserId(data.getDetail().getPhone(), 1);
                }
                //每次登录后都需要更新统一认证系统返回的电话号码和昵称
                if (appConnect == null) {
                    appConnect = new AppConnect();
                    appConnect.setAppId(1);
                    appConnect.setBizUserId(data.getDetail().getPhone());
                    appConnect.setNickName(data.getNick_name());
                    appConnectService.insertUserIfNecessary(appConnect);
                }else {
                    appConnect.setBizUserId(data.getDetail().getPhone());
                    appConnect.setNickName(data.getNick_name());
                    appConnectService.updateById(appConnect);
                }
                UserInfoInTokenBO userInfoInToken = new UserInfoInTokenBO();
                userInfoInToken.setUserId(appConnect.getUserId());
                userInfoInToken.setSysType(SysTypeEnum.ORDINARY.value());
                userInfoInToken.setEnabled(true);
                userInfoInToken.setBizUid(appConnect.getBizUnionid());
                userInfoInToken.setBizUserId(appConnect.getBizUserId());
                userInfoInToken.setSessionKey(appConnect.getBizTempSession());
                userInfoInToken.setSocialType(appConnect.getAppId());
                // 存储token返回vo
                TokenInfoVO tokenInfoVO = tokenStore.storeAndGetVo(userInfoInToken);
                tokenInfoVO.setUserId(appConnect.getUserId());
                if (StringUtils.isBlank(data.getDetail().getThirdId()) && appConnect.getId() != null) {
                    AppConnect finalAppConnect = appConnect;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //如果统一认证系统不存在该用户，需要推送当前用户主键id
                            HashMap<String, String> requestMap1 = new HashMap<>();
                            HashMap<String, String> headers1 = new HashMap<>();
                            headers1.put("Authorization", "Bearer " + data.getAccess_token());
                            requestMap1.put("ThirdId", finalAppConnect.getId() + "");
                            OkHttpUtils.okHttpFrom("http://192.168.5.108/one-access/oauth/bindThirdId", headers1, requestMap1);
                        }
                    }).start();
                }
                return  ServerResponseEntity.success(tokenInfoVO);
            }
        }
        return  ServerResponseEntity.fail(ResponseEnum.SHOW_FAIL.value(), "获取登录信息失败");
    }



    @PostMapping("/wx/login")
    @ApiOperation(value = "账号密码登录(用于微信小程序和公众号)", notes = "微信小程序和公众号通过账号/手机号/用户名密码登录, 二合一登录（包含绑定 + 登录）")
    public ServerResponseEntity<TokenInfoVO> wxLogin(
            @Valid @RequestBody SocialAuthenticationDTO authenticationDTO) {
        if (StrUtil.isBlank(authenticationDTO.getTempUid())) {
            return ServerResponseEntity.showFailMsg("tempUid is blank");
        }
        User user = getUser(authenticationDTO.getUserName());
        String decryptPassword = passwordManager.decryptPassword(authenticationDTO.getPassWord());
        // 半小时内密码输入错误十次，已限制登录30分钟
        passwordCheckManager.checkPassword(SysTypeEnum.ORDINARY,user.getUserMobile(),decryptPassword, user.getLoginPassword());

        AppConnect appConnect = checkAndGetAppConnect(user.getUserId(), authenticationDTO.getSocialType(), authenticationDTO.getTempUid());

        return ServerResponseEntity.success(getTokenInfoVO(user, appConnect));
    }


    @PostMapping("/ma/login")
    @ApiOperation(value = "微信小程序一键登录", notes = "三合一登录（包含注册 + 绑定 + 登录）")
    public ServerResponseEntity<TokenInfoVO> maLogin(@Valid @RequestBody MaCodeAuthenticationDTO maCodeAuthenticationDTO) {
        WxMaPhoneNumberInfo newPhoneNoInfo;
        try {
            newPhoneNoInfo = wxConfig.getWxMaService().getUserService().getNewPhoneNoInfo(maCodeAuthenticationDTO.getCode());
        } catch (WxErrorException e) {
            throw new YamiShopBindException(e.getMessage());
        }
        // 没有区号的手机号，国外手机号会有区号
        String mobile = newPhoneNoInfo.getPurePhoneNumber();

        return threeInOneLogin(mobile, maCodeAuthenticationDTO.getTempUid(), SocialType.MA.value());
    }


    @PostMapping("/smsLogin")
    @ApiOperation(value = "短信登录", notes = "三合一登录（包含注册 + 绑定 + 登录）")
    public ServerResponseEntity<TokenInfoVO> smsLogin(
            @Valid @RequestBody SocialAuthenticationDTO authenticationDTO) {

        if (StrUtil.isBlank(authenticationDTO.getTempUid())
                && (Objects.equals(authenticationDTO.getSocialType(), SocialType.MA.value())
                || Objects.equals(authenticationDTO.getSocialType(), SocialType.MP.value()))) {
            return ServerResponseEntity.showFailMsg("tempUid is blank");
        }
        // 验证码登录
        boolean validCode;
        try {
            validCode = smsLogService.checkValidCode(authenticationDTO.getUserName(), String.valueOf(authenticationDTO.getPassWord()), SendType.LOGIN);
        }catch (YamiShopBindException e){
            // 验证码校验过频繁，请稍后再试
            throw new YamiShopBindException("yami.user.code.check.too.much");
        }
        if (!validCode){
            // 验证码有误或已过期
            throw new YamiShopBindException("yami.user.code.error");
        }
        // 如果没有注册的话，短信登录将会进行注册
        // 在pc/小程序/公众号的登录，都有短信登录的方法。但是公众号/小程序的短信登录，登录了之后会见这个用户和公众号/小程序绑定一起（登录并绑定）
        return threeInOneLogin(authenticationDTO.getUserName(), authenticationDTO.getTempUid(), authenticationDTO.getSocialType());
    }


    private TokenInfoVO getTokenInfoVO(User user, AppConnect appConnect) {
        UserInfoInTokenBO userInfoInToken = new UserInfoInTokenBO();
        userInfoInToken.setUserId(user.getUserId());
        userInfoInToken.setSysType(SysTypeEnum.ORDINARY.value());
        userInfoInToken.setEnabled(user.getStatus() == 1);
        if (appConnect !=null) {
            userInfoInToken.setBizUid(appConnect.getBizUnionid());
            userInfoInToken.setBizUserId(appConnect.getBizUserId());
            userInfoInToken.setSessionKey(appConnect.getBizTempSession());
            userInfoInToken.setSocialType(appConnect.getAppId());
        }
        // 存储token返回vo
        return tokenStore.storeAndGetVo(userInfoInToken);
    }

    /**
     * 三合一登录（包含注册 + 绑定 + 登录）
     */
    private ServerResponseEntity<TokenInfoVO> threeInOneLogin(String mobile,String tempUid, Integer socialType) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserMobile, mobile));
        AppConnect appConnect = null;
        // 没有就注册
        if (user == null) {
            if (Objects.equals(socialType,SocialType.MA.value()) || Objects.equals(socialType,SocialType.MP.value())) {
                appConnect = appConnectService.getByTempUid(tempUid);
                if (appConnect == null || appConnect.getUserId() != null) {
                    return ServerResponseEntity.fail(ResponseEnum.TEMP_UID_ERROR);
                }
            }
            // 注册并绑定用户
            user = appConnectService.registerAndBindUser(mobile, null, tempUid);
        } else  {
            appConnect = checkAndGetAppConnect(user.getUserId(), socialType, tempUid);
        }
        // 绑定
        return ServerResponseEntity.success(getTokenInfoVO(user, appConnect));
    }

    /**
     * 登录并绑定用户
     */
    private AppConnect checkAndGetAppConnect(String userId, Integer socialType,String tempUid) {
        // 只有微信公众号和小程序的登录会进行登录并绑定的操作
        if (!Objects.equals(socialType,SocialType.MA.value()) && !Objects.equals(socialType,SocialType.MP.value())) {
            return null;
        }
        // 在SocialLoginController 当中，会返回一个tempUid用来换取openid的
        AppConnect appConnect = appConnectService.getByTempUid(tempUid);
        if (appConnect == null) {
            throw new YamiShopBindException(ResponseEnum.TEMP_UID_ERROR);
        }
        // 二合一登录，啥意思呢？
        // 1. 绑定：将该账号和该微信的openId进行绑定
        // 2. 登录：返回token登录成功
        if (appConnect.getUserId() == null) {
            appConnectMapper.bindUserIdByTempUid(userId, appConnect.getTempUid());
        }
        // 如果有userId就判断下是不是一样的user，不是的话，不能够绑定
        else if (!Objects.equals(appConnect.getUserId(), userId)) {
            throw new YamiShopBindException(ResponseEnum.SOCIAL_ACCOUNT_BIND_BY_OTHER);
        }
        return appConnect;
    }

    private User getUser(String mobileOrUserName) {
        User user = null;
        // 手机验证码登陆，或传过来的账号很像手机号
        if (PrincipalUtil.isMobile(mobileOrUserName)) {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserMobile, mobileOrUserName));
        }
        // 如果不是手机验证码登陆， 找不到手机号就找用户名
        if  (PrincipalUtil.isUserName(mobileOrUserName)) {
            user = userMapper.selectOneByUserName(mobileOrUserName);
        }
        // 如果不是手机验证码登陆， 找不到手机号就找用户名
        if  (user == null) {
            user = userMapper.selectOneByUserName(mobileOrUserName);
        }
        if (user == null) {
            throw new YamiShopBindException("yami.user.account.error");
        }
        return user;
    }



}
