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
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 会员等级
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Data
@TableName("tz_user_level")
public class UserLevel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 等级名称
     */
    private String levelName;
    /**
     * 等级条件 0 普通会员 1 付费会员
     */
    private Integer levelType;
    /**
     * 所需成长值
     */
    private Integer needGrowth;
    /**
     * 付费会员价格
     */
    private Double needAmount;
    /**
     * 有效期（天）
     */
    private Integer term;
    /**
     * 期数类型
     */
    private Integer termType;
    /**
     * 背景图片
     */
    private String img;
    /**
     * 折扣范围 0 全平台 1 自营店
     */
    private Integer discountRange;
    /**
     * 折扣方式 0 全部商品 1 分类下的商品
     */
    private Integer discountType;
    /**
     * 会员折扣
     */
    private Double discount;
    /**
     * 赠送积分
     */
    private Long presScore;
    /**
     * 积分回馈倍率
     */
    private Double rateScore;
    /**
     * 是否包邮 0:不包邮 1:包邮
     */
    private Integer isFreeFee;
    /**
     * 1:已更新 0:停用 -1:未更新(等级修改后，用户等级的更新)
     */
    private Integer status;
    /**
     * 优惠券列表
     */
    @TableField(exist = false)
    private List<Coupon> couponList;

    @TableField(exist = false)
    private List<Long> couponIds;

    /**
     * 等级分类列表
     */
    @TableField(exist = false)
    private List<Long> categoryIds;

    @TableField(exist = false)
    private List<Long> categorys;

    /**
     *  权益id数组
     */
    @TableField(exist = false)
    private List<Long> userRightsIds;

    /**
     *  权益数组
     */
    @TableField(exist = false)
    private List<UserRights> userRights;
}
