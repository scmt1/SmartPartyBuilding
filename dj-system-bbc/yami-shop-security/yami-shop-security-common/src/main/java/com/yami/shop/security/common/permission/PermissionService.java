/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.permission;

import cn.hutool.core.util.StrUtil;
import com.yami.shop.bean.event.PermissionErrorHandleEvent;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.util.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

/**
 * 接口权限判断工具
 * @author FrozenWatermelon
 */
@Slf4j
@Component("pms")
public class PermissionService {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 判断接口是否有xxx:xxx权限
     *
     * @param permission 权限
     * @return {boolean}
     */
    public boolean hasPermission(String permission) {
        if (StrUtil.isBlank(permission)) {
            return false;
        }
        // 框架处理会抛出异常在前端提示：服务器出了小差，请稍后重试
        // 所以没有权限，就自行处理并抛出异常，且告诉用户缺失的菜单权限是哪些，同时避免服务器异常的提示
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        boolean hasPermission = userInfoInTokenBO.getPerms()
                .stream()
                .filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(permission, x));
        if(!hasPermission) {
            applicationContext.publishEvent(new PermissionErrorHandleEvent(permission, userInfoInTokenBO.getShopId()));
        }
        return hasPermission;
    }

}
