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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户标签
 *
 * @author LGH
 * @date 2020-09-09 15:40:59
 */
@Data
@TableName("tz_user_tag")
public class UserTag implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId
    private Long userTagId;
    /**
     * 标签名字
     */
    private String tagName;
    /**
     * 标签类型0手动1条件2系统
     */
    private Integer tagType;
    /**
     * 系统标签是否开启
     */
    private Integer isSysTurnOn;
    /**
     * 成为客户开始时间
     */
    private LocalDateTime registerMinTime;
    /**
     * 成为客户结束时间
     */
    private LocalDateTime registerMaxTime;
    /**
     * 关注开始时间
     */
    private LocalDateTime subscribeMinTime;
    /**
     * 关注结束时间
     */
    private LocalDateTime subscribeMaxTime;
    /**
     * 成为会员开始时间
     */
    private LocalDateTime toBeMemberMinTime;
    /**
     * 成为会员结束时间
     */
    private LocalDateTime toBeMemberMaxTime;
    /**
     * 最近消费时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)
     */
    private Integer recentPurchaseTime;
    /**
     * 消费次数时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)
     */
    private Integer purchaseNumTime;
    /**
     * 消费次数最小次数
     */
    private Long purchaseNumMinNum;
    /**
     * 消费次数最大次数
     */
    private Long purchaseNumMaxNum;
    /**
     * 消费金额时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)
     */
    private Integer purchaseAmountTime;
    /**
     * 消费金额最小金额
     */
    private BigDecimal purchaseAmountMinAmount;
    /**
     * 消费金额最大金额
     */
    private BigDecimal purchaseAmountMaxAmount;
    /**
     * 订单均价时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)
     */
    private Integer orderAveragePriceTime;
    /**
     * 订单均价最小金额
     */
    private BigDecimal orderAveragePriceMinAmount;
    /**
     * 订单均价最大金额
     */
    private BigDecimal orderAveragePriceMaxAmount;
    /**
     * 充值金额时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)
     */
    private Integer rechargeAmountTime;
    /**
     * 充值金额最小金额
     */
    private BigDecimal rechargeAmountMinAmount;
    /**
     * 充值金额最大金额
     */
    private BigDecimal rechargeAmountMaxAmount;
    /**
     * 充值次数时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)
     */
    private Integer rechargeNumTime;
    /**
     * 充值次数最小次数
     */
    private Long rechargeNumMinNum;
    /**
     * 充值次数最大次数
     */
    private Long rechargeNumMaxNum;
    /**
     * 符合标签的人数
     */
    private Long userNum;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 统计更新时间
     */
    private LocalDateTime statisticUpdateTime;

}
