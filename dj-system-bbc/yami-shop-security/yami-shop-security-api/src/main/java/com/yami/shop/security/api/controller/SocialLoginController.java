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

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.IdUtil;
import com.yami.shop.bean.model.User;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.config.WxConfig;
import com.yami.shop.security.api.vo.TokenWithTempUidVO;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.enums.SocialType;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.model.AppConnect;
import com.yami.shop.security.common.service.AppConnectService;
import com.yami.shop.security.common.vo.TokenInfoVO;
import com.yami.shop.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 尝试使用社交账户登录
 * 如果登录成功，返回token信息
 * 如果登录失败，返回一个临时的uid，同时把openid之类的信息保存到数据库
 *
 * 前提：
 * 1. 如果在小程序注册，那么会直接绑定，下次打开不需要登录按钮进行登录（换了手机也会，只要是同一个微信号）
 * 2. 如果用户直接输入账号密码登录，会直接跟这个用户进行绑定，会有退出登录按钮
 * 3. 如果退出登录的话，就要解除绑定，不然没办法退出登录
 * 4. 如果用户登录过一次，其实就已经绑定了，下次进入也是直接登录，不需要输入账号密码
 * 5. 如果用户在别的微信上登录过（也就是绑定过），需要在别人的微信上进行退出登录（解除绑定），才能在新的微信上登录（换手机不影响）
 *
 * 代码逻辑：
 * 一进去小程序/微信公众号的时候会通过code获取openid（1.获取code不需要按钮的 2.code大概几分钟会过期）。
 * 如果没有openid就没办法进行支付。所以我们要在登录的时候把openid和token进行关联，通过token获取用户的userId和openid进行支付。
 * 但是会出现一种情况 比如在微信公众号是通过页面跳转获取code的，如果跳转完页面，用户一直在这个页面不去登录。后面再登录的时候已经code已经过期了。
 * 那怎么解决这种情况呢？
 *
 * 我们在拿到code的时候直接调用社交登录的接口，如/social/mp，/social/ma 这两个接口，会出现两种情况
 * 1. 如果通过code获取了openid，通过openid获取绑定的userid，如果能获取userid，那就直接登录，返回token。
 * 2. 如果没有获取到openid，就将这个openid保存用一个uuid关联起来，并保持到数据库。
 *    因为code会过期，数据库我们自己写的标识不会过期，将这个标识返回给前端（同时返回了A04001这个http状态，前端用tempUid保存起来）。
 *    前端进行登录的时候，用这个tempUid给到我们进行登录，这个时候用这个标识就能获取到数据库的openid了，也不会出现过期的情况。
 *    这个时候用openid可以关联登录信息、注册信息、绑定信息。
 *
 * @author FrozenWatermelon
 * @date 2021/1/16
 */
@RequestMapping("/social")
@RestController
@Api(tags = "微信code登录")
public class SocialLoginController {


    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private AppConnectService appConnectService;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private UserService userService;

    @PostMapping("/mp")
    @ApiOperation(value = "公众号code登录", notes = "通过公众号进行登录，只要在一进入到应用的时候调用微信登录的这个接口就行了，不要重复调用这个接口(不要在什么注册页面之类的地方再调用这个接口，打开应用的时候调用就已经足够了)" +
            "1.返回状态码 A04001 社交账号未绑定，当前端看到该异常时，将该值tempUid存起来，并在应该在合适的时间（比如在购买的时候跳），跳转到登录的页面" +
            "2.如果返回码是正常的登录成功，前端要保留原本已经存在的tempUid，如果有人切换登录的话，起码能够用这个tempUid重新获取openid进行绑定")
    public ServerResponseEntity<TokenWithTempUidVO> mp(@RequestBody String code) throws WxErrorException {
        Integer socialType = SocialType.MP.value();
        WxOAuth2AccessToken wxAccessToken = wxConfig.getWxMpService().getOAuth2Service().getAccessToken(code);

        AppConnect appConnect = appConnectService.getByBizUserId(wxAccessToken.getOpenId(), socialType);
        if (appConnect != null && appConnect.getUserId() != null) {
            return ServerResponseEntity.success(new TokenWithTempUidVO(appConnect.getTempUid(),getTokenVo(appConnect)));
        }
        String tempUid = IdUtil.simpleUUID();
        WxOAuth2UserInfo wxUserInfo = wxConfig.getWxMpService().getOAuth2Service().getUserInfo(wxAccessToken, null);
        if (appConnect == null) {
            appConnect = new AppConnect();
        }
        appConnect.setAppId(socialType);
        appConnect.setTempUid(tempUid);
        appConnect.setNickName(wxUserInfo.getNickname());
        appConnect.setImageUrl(wxUserInfo.getHeadImgUrl());
        appConnect.setBizUserId(wxUserInfo.getOpenid());
        appConnect.setBizUnionid(wxUserInfo.getUnionId());
        appConnectService.saveOrUpdate(appConnect);
        // 前端要保存这个tempUid
        return ServerResponseEntity.success(new TokenWithTempUidVO(appConnect.getTempUid(), null));
    }

    @PostMapping("/ma")
    @ApiOperation(value = "小程序code登录", notes ="通过小程序进行登录，只要在一进入到应用的时候调用微信登录的这个接口就行了，不要重复调用这个接口(不要在什么注册页面之类的地方再调用这个接口，打开应用的时候调用就已经足够了)" +
            "1.返回状态码 A04001 社交账号未绑定，当前端看到该异常时，将该值tempUid存起来，并在应该在合适的时间（比如在购买的时候跳），跳转到登录的页面" +
            "2.如果返回码是正常的登录成功，前端要保留原本已经存在的tempUid，如果有人切换登录的话，起码能够用这个tempUid重新获取openid进行绑定")
    public ServerResponseEntity<TokenWithTempUidVO> ma(@RequestBody String code) throws WxErrorException {
        String recode = code.replaceAll("\"","");
        Integer socialType = SocialType.MA.value();
        WxMaJscode2SessionResult session = wxConfig.getWxMaService().getUserService().getSessionInfo(recode);

        AppConnect appConnect = appConnectService.getByBizUserId(session.getOpenid(), socialType);
        if (appConnect != null && appConnect.getUserId() != null) {
            return ServerResponseEntity.success(new TokenWithTempUidVO(appConnect.getTempUid(),getTokenVo(appConnect)));
        }

        if (appConnect == null) {
            appConnect = new AppConnect();
        }
        String tempUid = IdUtil.simpleUUID();
        appConnect.setTempUid(tempUid);
        appConnect.setAppId(socialType);
        appConnect.setBizTempSession(session.getSessionKey());
        appConnect.setBizUserId(session.getOpenid());
        appConnect.setBizUnionid(session.getUnionid());
        appConnectService.saveOrUpdate(appConnect);
        return ServerResponseEntity.success(new TokenWithTempUidVO(appConnect.getTempUid(), null));
    }



    private TokenInfoVO getTokenVo(AppConnect appConnect) {
        User user = userService.getUserByUserId(appConnect.getUserId());
        UserInfoInTokenBO data = new UserInfoInTokenBO();
        data.setUserId(user.getUserId());
        data.setSysType(SysTypeEnum.ORDINARY.value());
        data.setBizUid(appConnect.getBizUnionid());
        data.setBizUserId(appConnect.getBizUserId());
        data.setSessionKey(appConnect.getBizTempSession());
        data.setSocialType(appConnect.getAppId());
        data.setEnabled(user.getStatus() == 1);
        // 保存token，返回token数据给前端
        return tokenStore.storeAndGetVo(data);
    }
}
