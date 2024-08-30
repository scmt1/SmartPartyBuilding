/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yami
 */
@Data
@TableName("tz_coupon")
public class Coupon implements Serializable {
    private static final long serialVersionUID = 8018312153820119913L;

    @TableId
    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "优惠类型 1:代金券 2:折扣券 3:兑换券")
    private Integer couponType;

    @ApiModelProperty(value = "使用条件")
    private Double cashCondition;

    @ApiModelProperty(value = "减免金额")
    private Double reduceAmount;

    @ApiModelProperty(value = "折扣额度")
    private Double couponDiscount;

    @ApiModelProperty(value = "生效类型 1:固定时间 2:领取后生效")
    private Integer validTimeType;

    @ApiModelProperty(value = "投放时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date launchTime;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "领券后X天起生效")
    private Integer afterReceiveDays;

    @ApiModelProperty(value = "有效天数")
    private Integer validDays;

    @ApiModelProperty(value = "库存")
    private Integer stocks;

    @ApiModelProperty(value = "原始库存")
    private Integer sourceStock;

    @ApiModelProperty(value = "适用商品类型 0全部商品参与 1指定商品参与 2指定商品不参与")
    private Integer suitableProdType;

    @ApiModelProperty(value = "每个用户领券上限，如不填则默认为1")
    private Integer limitNum;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "是否积分优惠券 0不是 1是")
    private Integer isScoreType;

    @ApiModelProperty(value = "积分价格")
    private Integer scorePrice;

    @ApiModelProperty(value = "优惠券过期状态 0:过期 1:未过期")
    private Integer overdueStatus;

    @ApiModelProperty(value = "优惠券投放状态 0:未投放 1:投放 -1取消投放")
    private Integer putonStatus;

    @ApiModelProperty(value = "优惠券所在的店铺")
    @TableField(exist = false)
    private String shopName;

    @ApiModelProperty(value = "优惠券数量")
    @TableField(exist = false)
    private Integer couponNum;

    @ApiModelProperty(value = "获取方式  0=客户领取 1=平台发放")
    private Integer getWay;
}
