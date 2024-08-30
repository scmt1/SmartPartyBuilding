/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.controller;

import cn.hutool.core.util.StrUtil;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.enums.SocialType;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.service.AppConnectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/6/30
 */
@RestController
@Api(tags = "注销")
public class LogoutController {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private AppConnectService appConnectService;

    @PostMapping("/logOut")
    @ApiOperation(value = "退出登陆", notes = "点击退出登陆，清除token，清除菜单缓存")
    public ServerResponseEntity<Void> logOut(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (StrUtil.isBlank(accessToken)) {
            return ServerResponseEntity.success();
        }

        // 当前登录的用户信息
        UserInfoInTokenBO userInfoInToken = tokenStore.getUserInfoByAccessToken(accessToken, true);

        // 删除该用户在该系统当前的token（退出登录）
        tokenStore.deleteCurrentToken(accessToken);

        // 只有微信公众号和小程序的登录会进行登录并绑定的操作
        if (!Objects.equals(userInfoInToken.getSocialType(), SocialType.MA.value()) && !Objects.equals(userInfoInToken.getSocialType(),SocialType.MP.value())) {
            return ServerResponseEntity.success();
        }

        // 解除绑定的用户
        appConnectService.unBindUser(userInfoInToken.getBizUserId(), userInfoInToken.getSocialType());

        return ServerResponseEntity.success();
    }

}
