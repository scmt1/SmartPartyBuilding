/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.multishop.util;

import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.util.AuthUserContext;
import com.yami.shop.security.multishop.model.YamiShopUser;
import lombok.experimental.UtilityClass;

/**
 *
 * @author LGH
 */
@UtilityClass
public class SecurityUtils {


    /**
     * 获取用户
     */
    public YamiShopUser getShopUser() {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();

        YamiShopUser details = new YamiShopUser();
        details.setTenantId(userInfoInTokenBO.getTenantId());
        details.setOrgId(userInfoInTokenBO.getOrgId());
        details.setUserId(userInfoInTokenBO.getUserId());
        details.setEnabled(userInfoInTokenBO.getEnabled());
        details.setShopId(userInfoInTokenBO.getShopId());
        details.setEmployeeId(userInfoInTokenBO.getOtherId());
        details.setAuthorities(userInfoInTokenBO.getPerms());
        return details;
    }
}
