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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 店铺角色
 * @author yami
 * @date 2021-03-01 17:42:09
 */
@Data
@TableName("tz_shop_role")
public class ShopRole implements Serializable{
    private static final long serialVersionUID = 1L;


    @TableId
    @ApiModelProperty(value = "角色id")
    private Long roleId;
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    @NotBlank(message="角色名称不能为空")
    private String roleName;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 创建者ID
     */
    @ApiModelProperty(value = "创建者ID")
    private Long createEmployeeId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(exist=false)
    @ApiModelProperty(value = "菜单id列表")
    private List<Long> menuIdList;
}
