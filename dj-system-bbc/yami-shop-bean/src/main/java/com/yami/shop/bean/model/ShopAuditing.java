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
 * 商家审核信息
 *
 * @author Dwl
 * @date 2019-09-19 14:02:57
 */
@Data
@TableName("tz_shop_auditing")
public class ShopAuditing implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 店铺审核id
     */
    @TableId
    private Long shopAuditingId;

    @ApiModelProperty(value = "申请人id", required = true)
    private String userId;

    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;

    @ApiModelProperty(value = "审核人id", required = true)
    private Long auditorId;

    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;

    @ApiModelProperty(value = "更新时间", required = true)
    private Date updateTime;

    @ApiModelProperty(value = "0 未审核 1已通过 -1未通过 -2未提交审核", required = true)
    private Integer status;

    @ApiModelProperty(value = "备注", required = true)
    private String remarks;

    @ApiModelProperty(value = "是否开启开店协议")
    @TableField(exist = false)
    private Boolean shopProtocolSwitch;
}
