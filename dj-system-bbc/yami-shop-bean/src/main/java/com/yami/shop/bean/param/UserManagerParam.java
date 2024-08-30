/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 全部会员参数
 */
/**
 * @author Yami
 */
@Data
public class UserManagerParam {

    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户邮箱
     */

    private String userMail;


    /**
     * 手机号码
     */
    private String userMobile;

    /**
     * 修改时间
     *     @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
     */
    private Date modifyTime;

    /**
     * 注册时间
     */
    private Date userRegtime;

    /**
     * 注册IP
     */
    private String userRegip;

    /**
     * 备注
     */
    private String userMemo;

    /**
     * 禁用原因
     */
    private String disableRemark;

    /**
     * 0男 1女
     */
    private String sex;

    /**
     * 例如：2009-11-27
     */
    private String birthDate;

    /**
     * 头像图片路径
     */
    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    private Integer status;

    /**
     * 积分
     */
    private Integer score;

    /**
     * 会员成长值
     */
    private Long growth;

    /**
     * 会员等级
     */
    private Integer level;
    /**
     * 等级条件 0 普通会员 1 付费会员
     */
    private Integer levelType;

    /**
     * vip结束时间
     */
    private Date vipEndTime;

    /**
     * 会员等级名称
     */
    private String levelName;

    /**
     * bizUserId
     */
    private String bizUserId;

  // -----------------------------------------------------以上是user copy过来的信息
    /**
     * 最近消费时间
     */
    private Date reConsTime;
    /**
     * 消费金额
     */
    private Double consAmount;
    /**
     * 实付金额
     */
    private Double actualAmount;
    /**
     * 订单实付金额
     */
    private Double orderActualAmount;
    /**
     * 优惠总金额
     */
    private Double reduceAmount;
    /**
     * 消费次数
     */
    private Integer consTimes;
    /**
     * 平均折扣
     */
    private Double averDiscount;
    /**
     * 充值金额
     */
    private Double rechargeAmount;
    /**
     * 充值次数
     */
    private Integer rechargeTimes;
    /**
     * 售后金额
     */
    private Double afterSaleAmount;
    /**
     * 售后次数
     */
    private Integer afterSaleTimes;
    /**
     * 当前积分
     */
    private Integer currentScore;
    /**
     * 累积积分
     */
    private Integer sumScore;
    /**
     * 当前余额
     */
    private Double currentBalance;
    /**
     * 累计余额
     */
    private Double sumBalance;
    /**
     * 分销等级
     */
    private Integer distributionLevel;
    /**
     * 成为分销员的时间
     */
    private Date distributorTime;
    // ----以下用户详情----------------------------------------------------------------
    /**
     * 标签
     */
    private List<UserTagParam> userTagParam;
    /**
     * 用户优惠券 统计
     */
    private CouponUserParam couponUserParam;
}
