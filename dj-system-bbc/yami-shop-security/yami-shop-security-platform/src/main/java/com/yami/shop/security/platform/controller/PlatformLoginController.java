/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.platform.controller;

import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.dto.AutoCreateInfoDTO;
import com.yami.shop.bean.dto.ShopCreateInfoDTO;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.PasswordCheckManager;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.vo.TokenInfoVO;
import com.yami.shop.security.platform.dto.CaptchaAuthenticationDTO;
import com.yami.shop.security.platform.util.SecurityUtils;
import com.yami.shop.service.ShopDetailService;
import com.yami.shop.sys.common.dao.SysMenuMapper;
import com.yami.shop.sys.common.dao.SysUserMapper;
import com.yami.shop.sys.common.model.SysMenu;
import com.yami.shop.sys.common.model.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2020/6/30
 */
@RestController
@Api(tags = "登录")
public class PlatformLoginController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private PasswordCheckManager passwordCheckManager;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private PasswordManager passwordManager;

    @Autowired
    private ShopDetailService shopDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/platformLogin")
    @ApiOperation(value = "账号密码 + 验证码登录(用于后台登录)", notes = "通过账号/手机号/用户名密码登录")
    public ServerResponseEntity<?> login(
            @Valid @RequestBody CaptchaAuthenticationDTO captchaAuthenticationDTO) {
        // 登陆后台登录需要再校验一遍验证码
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaAuthenticationDTO.getCaptchaVerification());
        ResponseModel response = captchaService.verification(captchaVO);
        if (!response.isSuccess()) {
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.user.code.error"));
        }

        SysUser sysUser = sysUserMapper.selectByUsername(captchaAuthenticationDTO.getUserName());
        if (sysUser == null) {
            throw new YamiShopBindException("yami.user.account.error");
        }

        String decryptPassword = passwordManager.decryptPassword(captchaAuthenticationDTO.getPassWord());

        // 半小时内密码输入错误十次，已限制登录30分钟
        passwordCheckManager.checkPassword(SysTypeEnum.MULTISHOP,captchaAuthenticationDTO.getUserName(), decryptPassword, sysUser.getPassword());

        // 不是店铺超级管理员，并且是禁用状态，无法登录
        if (Objects.equals(sysUser.getStatus(),0)) {
            // 未找到此用户信息
            throw new YamiShopBindException("yami.shop.user.account.lock");
        }

        UserInfoInTokenBO userInfoInToken = new UserInfoInTokenBO();
        userInfoInToken.setUserId(String.valueOf(sysUser.getUserId()));
        userInfoInToken.setSysType(SysTypeEnum.PLATFORM.value());
        userInfoInToken.setEnabled(sysUser.getStatus() == 1);
        userInfoInToken.setPerms(getUserPermissions(sysUser.getUserId()));
        userInfoInToken.setNikeName(sysUser.getNickName());
        // 存储token返回vo
        TokenInfoVO tokenInfoVO = tokenStore.storeAndGetVo(userInfoInToken);
        return ServerResponseEntity.success(tokenInfoVO);
    }


    private Set<String> getUserPermissions(Long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN_ID){
            List<SysMenu> menuList = sysMenuMapper.selectList(Wrappers.emptyWrapper());


            permsList = menuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
        }else{
            permsList = sysUserMapper.queryAllPerms(userId);
        }
        return permsList.stream().flatMap((perms)->{
                    if (StrUtil.isBlank(perms)) {
                        return null;
                    }
                    return Arrays.stream(perms.trim().split(StrUtil.COMMA));
                }
        ).collect(Collectors.toSet());
    }



    @PostMapping("/createShop")
    @ApiOperation(value = "新建店铺", notes = "新建店铺")
    public ServerResponseEntity<Void> createShop(@RequestBody @Valid AutoCreateInfoDTO shopCreateInfoDTO) throws ParseException {
        shopDetailService.autoCreateShop(shopCreateInfoDTO);
        return ServerResponseEntity.success();
    }


}
