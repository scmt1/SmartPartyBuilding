/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.common.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 商家端用户与角色对应关系
 * @author yami
 * @date 2021-03-01 17:42:09
 */
@Data
@TableName("tz_shop_employee_role")
public class ShopEmployeeRole implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long employeeId;
    /**
     * 角色ID
     */
    private Long roleId;

}
