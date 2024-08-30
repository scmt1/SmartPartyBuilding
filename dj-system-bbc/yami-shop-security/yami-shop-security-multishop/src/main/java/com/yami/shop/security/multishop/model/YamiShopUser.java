/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.multishop.model;

import lombok.Data;

import java.util.Set;

/**
 * 用户详细信息
 *
 * @author
 */
@Data
public class YamiShopUser {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 店铺id
     */
    private Long shopId;

    private boolean enabled;


    /** 店铺管理员id */
    private Long employeeId;

    private Set<String> authorities;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 所属机构编号
     */
    private String orgId;

}
