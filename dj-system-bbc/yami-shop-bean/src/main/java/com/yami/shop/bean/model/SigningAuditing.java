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
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author lth
 * @Date 2021/8/19 10:14
 */
@Data
@TableName("tz_signing_auditing")
public class SigningAuditing {
    /**
     * 店铺审核id
     */
    @TableId
    private Long signingAuditingId;

    @ApiModelProperty(value = "申请人id")
    private String userId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "审核人id")
    private Long auditorId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "0 未审核 1已通过 -1未通过")
    private Integer status;

    @ApiModelProperty(value = "签约类型 1分类 2 品牌")
    private Integer type;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "开始时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @TableField(exist = false)
    @ApiModelProperty(value = "店铺logo")
    private String shopLogo;
}
