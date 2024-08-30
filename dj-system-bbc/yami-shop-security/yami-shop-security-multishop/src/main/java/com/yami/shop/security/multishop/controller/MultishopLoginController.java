/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.multishop.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.enums.PositionType;
import com.yami.shop.bean.model.dj.PartyMember;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.IPartyMemberService;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.PasswordCheckManager;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.vo.TokenInfoVO;
import com.yami.shop.security.multishop.dto.CaptchaAuthenticationDTO;
import com.yami.shop.security.multishop.model.SysApp;
import com.yami.shop.sys.common.dao.ShopEmployeeMapper;
import com.yami.shop.sys.common.dao.ShopMenuMapper;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.model.ShopMenu;
import com.yami.shop.utils.HttpClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2020/6/30
 */
@RestController
@Api(tags = "登录")
public class MultishopLoginController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ShopEmployeeMapper shopEmployeeMapper;

    @Autowired
    private ShopMenuMapper shopMenuMapper;

    @Autowired
    private PasswordCheckManager passwordCheckManager;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private PasswordManager passwordManager;

    @Autowired
    private IPartyMemberService partyMemberService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/shopLogin")
    @ApiOperation(value = "账号密码 + 验证码登录(用于后台登录)", notes = "通过账号/手机号/用户名密码登录")
    public ServerResponseEntity<?> login(@Valid @RequestBody CaptchaAuthenticationDTO captchaAuthenticationDTO) {
        // 登陆后台登录需要再校验一遍验证码
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaAuthenticationDTO.getCaptchaVerification());
        ResponseModel response = captchaService.verification(captchaVO);
        if (!response.isSuccess()) {
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.user.code.error"));
        }

        ShopEmployee shopEmployee = shopEmployeeMapper.getShopByUsernameOrMobile(captchaAuthenticationDTO.getUserName());
        if (shopEmployee == null) {
            throw new YamiShopBindException("yami.user.account.error");
        }

        String decryptPassword = passwordManager.decryptPassword(captchaAuthenticationDTO.getPassWord());

        // 半小时内密码输入错误十次，已限制登录30分钟
        passwordCheckManager.checkPassword(SysTypeEnum.MULTISHOP,captchaAuthenticationDTO.getUserName(), decryptPassword, shopEmployee.getPassword());

        // 不是店铺超级管理员，并且是禁用状态，无法登录
        if (!Objects.equals(PositionType.ADMIN.value(), shopEmployee.getType()) && Objects.equals(shopEmployee.getStatus(),0)) {
            // 未找到此用户信息
            throw new YamiShopBindException("yami.shop.user.account.lock");
        }

        //HashMap<String, Object> requestMap = new HashMap<>();
        //JSONObject obj = new JSONObject();
        //obj.put("phone",shopEmployee.getMobile());
        //requestMap.put("serviceName","UserService");
        //requestMap.put("methodName","getAppByPhone");
        //requestMap.put("bizParams",obj);
        //org.springframework.http.HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.add("Content-Type", "application/json");
        //httpHeaders.add("Accept", "application/json;charset=utf-8");
        //String postResult = HttpClient.sendPostRequest("http://192.168.5.77:8097/gateway", requestMap, httpHeaders);
        //JSONObject jsonObject = JSONObject.parseObject(postResult);
        //if(!"000000".equals(jsonObject.getString("code"))){
        //    throw new YamiShopBindException("yami.user.account.error");
        //}
        //String data = JSONObject.toJSONString(jsonObject.getJSONArray("data"));
        //List<SysApp> sysApps = JSONObject.parseArray(data, SysApp.class);
        //boolean flag = false;
        //if(sysApps.size() > 0){
        //    for (SysApp sysApp : sysApps) {
        //        if (sysApp != null && "dwgl".equals(sysApp.getAppCode())) {
        //            flag = true;
        //        }
        //    }
        //    if(!flag){
        //        throw new YamiShopBindException("登录账号无权限");
        //    }
        //}else{
        //    throw new YamiShopBindException("登录账号无权限");
        //}
        UserInfoInTokenBO userInfoInToken = new UserInfoInTokenBO();
        userInfoInToken.setUserId(shopEmployee.getEmployeeId().toString());
        userInfoInToken.setSysType(SysTypeEnum.MULTISHOP.value());
        userInfoInToken.setEnabled(shopEmployee.getStatus() == 1);
        userInfoInToken.setShopId(shopEmployee.getShopId());
        userInfoInToken.setOtherId(shopEmployee.getEmployeeId());
        userInfoInToken.setPerms(getUserPermissions(shopEmployee));
        userInfoInToken.setTenantId(shopEmployee.getTenantId());
        userInfoInToken.setOrgId(shopEmployee.getOrgId());
        // 存储token返回vo
        TokenInfoVO tokenInfoVO = tokenStore.storeAndGetVo(userInfoInToken);
        return ServerResponseEntity.success(tokenInfoVO);
    }


    private Set<String> getUserPermissions(ShopEmployee shopEmployee) {
        Integer type = shopEmployee.getType();
        Long employeeId = shopEmployee.getEmployeeId();
        List<String> permsList;

        //店铺超级管理员，拥有最高权限
        if(Objects.equals(PositionType.ADMIN.value(),type)){
            List<ShopMenu> menuList = shopMenuMapper.selectList(Wrappers.emptyWrapper());
            permsList = menuList.stream().map(ShopMenu::getPerms).collect(Collectors.toList());
        }else{
            permsList = shopEmployeeMapper.queryAllPerms(employeeId);
        }

        return permsList.stream().flatMap((perms)->{
            if (StrUtil.isBlank(perms)) {
                return null;
            }
            return Arrays.stream(perms.trim().split(StrUtil.COMMA));
        }).collect(Collectors.toSet());
    }


    @PostMapping("/appLogin")
    @ApiOperation(value = "移动端账号密码登录", notes = "通过账号/手机号/用户名密码登录")
    public ServerResponseEntity<?> appLogin(@Valid @RequestBody CaptchaAuthenticationDTO captchaAuthenticationDTO) {
        Object object = redisTemplate.opsForValue().get(captchaAuthenticationDTO.getUuid());
        if(object==null){
            return ServerResponseEntity.showFailMsg("验证码失效");
        }
        String code = String.valueOf(object);
        if(!code.equals(captchaAuthenticationDTO.getCaptchaVerification())){
            return ServerResponseEntity.showFailMsg("验证码错误");
        }
        @NotBlank(message = "userName不能为空") String userName = captchaAuthenticationDTO.getUserName().trim();
        ShopEmployee shopEmployee = shopEmployeeMapper.getShopByUsernameOrMobile(captchaAuthenticationDTO.getUserName());
        QueryWrapper<PartyMember> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNumeric(userName)&&userName.length()==11){
            queryWrapper.eq("party_member.phone",captchaAuthenticationDTO.getUserName()).eq("party_member.del_flag",0);
        }else {
            queryWrapper.eq("party_member.username",captchaAuthenticationDTO.getUserName()).eq("party_member.del_flag",0);
        }

        PartyMember partyMember = partyMemberService.getOne(queryWrapper);
        if (partyMember == null) {
            throw new YamiShopBindException("yami.user.account.error");
        }
        long time = new Date().getTime();
        String decryptPassword = passwordManager.decryptPassword(captchaAuthenticationDTO.getPassWord());
        String dataBasePassword = passwordManager.decryptPassword(partyMember.getPassword());
        if(!decryptPassword.equals(dataBasePassword)){
            return ServerResponseEntity.showFailMsg("密码错误");
        }
        // 半小时内密码输入错误十次，已限制登录30分钟
        //passwordCheckManager.checkPassword(SysTypeEnum.MULTISHOP,captchaAuthenticationDTO.getUserName(), decryptPassword, partyMember.getPassword());


        UserInfoInTokenBO userInfoInToken = new UserInfoInTokenBO();
        userInfoInToken.setUserId(partyMember.getId().toString());
        userInfoInToken.setSysType(SysTypeEnum.MULTISHOP.value());
        userInfoInToken.setEnabled(true);
        userInfoInToken.setShopId(null);
        userInfoInToken.setOtherId(partyMember.getDeptId());
        //userInfoInToken.setPerms(getUserPermissions(shopEmployee));
        //userInfoInToken.setTenantId(shopEmployee.getTenantId());
        //userInfoInToken.setOrgId(shopEmployee.getOrgId());
        // 存储token返回vo
        TokenInfoVO tokenInfoVO = tokenStore.storeAndGetVo(userInfoInToken);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(tokenInfoVO, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication1 = context.getAuthentication();
        redisTemplate.delete(captchaAuthenticationDTO.getUuid());

        return ServerResponseEntity.success(tokenInfoVO);
    }
    @PostMapping("/resetPassword")
    @ApiOperation(value = "重置密码")
    public ServerResponseEntity<?> resetPassword(@Valid @RequestBody CaptchaAuthenticationDTO captchaAuthenticationDTO, HttpServletRequest request) {

       /* Object object = redisTemplate.opsForValue().get(captchaAuthenticationDTO.getUuid());
        if(object==null){
            return ServerResponseEntity.showFailMsg("验证码失效");
        }
        String code = String.valueOf(object);
        if(!code.equals(captchaAuthenticationDTO.getCaptchaVerification())){
            return ServerResponseEntity.showFailMsg("验证码错误");
        }*/
         //String ts = "509e4d9c8a5e4c6e8fce20bd2dd87d8d";
        String authorization = request.getHeader("authorization");
        String decryptToken = tokenStore.decryptToken(authorization);
        //UserInfoInTokenBO userInfoInTokenBO = (UserInfoInTokenBO)redisTemplate.opsForValue().get("mall4j_oauth:token:access:" + ts);
        //UserInfoInTokenBO sss = (UserInfoInTokenBO)redisTemplate.opsForValue().get("mall4j_oauth:token:access:" + decryptToken);
        if (StringUtils.isBlank(captchaAuthenticationDTO.getNewPassword())){
           return ServerResponseEntity.showFailMsg("新密码不能为空");
       }
        @NotBlank(message = "userName不能为空") String userName = captchaAuthenticationDTO.getUserName().trim();

        QueryWrapper<PartyMember> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNumeric(userName)&&userName.length()==11){
            queryWrapper.eq("party_member.phone",captchaAuthenticationDTO.getUserName()).eq("party_member.del_flag",0);
        }else {
            queryWrapper.eq("party_member.username",captchaAuthenticationDTO.getUserName()).eq("party_member.del_flag",0);
        }

        PartyMember partyMember = partyMemberService.getOne(queryWrapper);
        if (partyMember == null) {
            throw new YamiShopBindException("yami.user.account.error");
        }
        // 解密前端的原密码
        //String decryptPassword = passwordManager.decryptPassword(captchaAuthenticationDTO.getPassWord());

        long time = new Date().getTime();
        // 解密数据库密码
        String dataBasePassword = passwordManager.decryptPassword(partyMember.getPassword());
        if(!captchaAuthenticationDTO.getPassWord().equals(dataBasePassword)){
            return ServerResponseEntity.showFailMsg("密码错误");
        }


        partyMember.setPassword(captchaAuthenticationDTO.getNewPassword());
        boolean updateById = partyMemberService.updateById(partyMember);
        if(updateById){
            tokenStore.deleteCurrentToken(authorization);
            //redisTemplate.opsForValue().get("mall4j_oauth:token:access:" +decryptToken);
            return ServerResponseEntity.success("重置成功");
        }else {
            return ServerResponseEntity.showFailMsg("重置失败");
        }
    }

}
