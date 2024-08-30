/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.bo;

import lombok.Data;

import java.util.Set;

/**
 * 保存在token信息里面的用户信息
 *
 * @author FrozenWatermelon
 * @date 2020/7/3
 */
@Data
public class UserInfoInTokenBO {

    /**
     * 用户在自己系统的用户id
     */
    private String userId;

    /**
     * 租户id (商家id)
     */
    private Long shopId;

    /**
     * 昵称
     */
    private String nikeName;

    /**
     * 系统类型
     * @see com.yami.shop.security.common.enums.SysTypeEnum
     */
    private Integer sysType;

    /**
     * 是否是管理员
     */
    private Integer isAdmin;

    private String bizUserId;

    private String bizUid;

    /**
     * 第三方系统类型 见com.yami.shop.security.common.enums.SocialType
     */
    private Integer socialType;

    /**
     * 小程序session_key
     */
    private String sessionKey;

    /**
     * 权限列表
     */
    private Set<String> perms;

    /**
     * 状态 1 正常 0 无效
     */
    private Boolean enabled;

    /**
     * 其他Id
     */
    private Long otherId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 所属机构编号
     */
    private String orgId;

}
