/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author SJL
 * @date 2020-08-25 15:50:06
 */
@Data
@TableName("tz_supplier")
public class Supplier implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "供应商分类id")
    private Long supplierCategoryId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "单位电话")
    private String tel;

    @ApiModelProperty(value = "省id")
    private Long provinceId;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市id")
    private Long cityId;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区id")
    private Long areaId;

    @ApiModelProperty(value = "区")
    private String area;

    @ApiModelProperty(value = "详细地址")
    private String addr;

    @ApiModelProperty(value = "联系人")
    private String contactName;

    @ApiModelProperty(value = "联系电话")
    private String contactTel;

    @ApiModelProperty(value = "qq号码")
    private String qqNumber;

    @ApiModelProperty(value = "微信号")
    private String wxNumber;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "状态 0禁用 1启用")
    private Integer status;

    @ApiModelProperty(value = "是否为默认供应商")
    private Integer isDefault;

    @ApiModelProperty(value = "供应商分类名称")
    @TableField(exist = false)
    private String categoryName;

}
