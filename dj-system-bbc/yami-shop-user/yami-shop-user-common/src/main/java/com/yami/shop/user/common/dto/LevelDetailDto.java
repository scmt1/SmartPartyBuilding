/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 会员等级
 *
 * @author LHD
 * @date 2020-02-26 16:03:14
 */
@Data
public class LevelDetailDto implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 等级
     */
    @ApiModelProperty(value = "等级")
    private Integer level;
    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String levelName;
    /**
     * 等级条件 0 普通会员 1 付费会员
     */
    @ApiModelProperty(value = "等级条件 0 普通会员 1 付费会员")
    private Integer levelType;
    /**
     * 所需成长值
     */
    @ApiModelProperty(value = "所需成长值")
    private Integer needGrowth;
    /**
     * 付费会员价格
     */
    @ApiModelProperty(value = "付费会员价格")
    private Double needAmount;
    /**
     * 有效期（天）
     */
    @ApiModelProperty(value = "等级")
    private Integer term;
    /**
     * 期数类型
     */
    @ApiModelProperty(value = "等级")
    private Integer termType;
    /**
     * 背景图片
     */
    @ApiModelProperty(value = "等级")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String img;


    /**
     *  权益数组
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "权益数组")
    private List<UserRightsDto> userRights;

//    /**
//     * 折扣范围 0 全平台 1 自营店
//     */
//    @ApiModelProperty(value = "等级")
//    private Integer discountRange;
//    /**
//     * 折扣方式 0 全部商品 1 分类下的商品
//     */
//    @ApiModelProperty(value = "等级")
//    private Integer discountType;
//    /**
//     * 会员折扣
//     */
//    @ApiModelProperty(value = "等级")
//    private Double discount;
//    /**
//     * 赠送积分
//     */
//    @ApiModelProperty(value = "等级")
//    private Integer presScore;
//    /**
//     * 积分回馈倍率
//     */
//    @ApiModelProperty(value = "等级")
//    private Double rateScore;
//    /**
//     * 是否包邮 0:不包邮 1:包邮
//     */
//    private Integer isFreeFee;
//    /**
//     * 优惠券列表
//     */
//    @TableField(exist = false)
//    private List<Coupon> couponList;
//
//    @TableField(exist = false)
//    private List<Long> couponIds;
//
//    /**
//     * 等级分类列表
//     */
//    @TableField(exist = false)
//    private List<Long> categoryIds;
//
//    @TableField(exist = false)
//    private List<Long> categorys;
//
//    /**
//     *  权益id数组
//     */
//    @TableField(exist = false)
//    private List<Long> userRightsIds;
}
