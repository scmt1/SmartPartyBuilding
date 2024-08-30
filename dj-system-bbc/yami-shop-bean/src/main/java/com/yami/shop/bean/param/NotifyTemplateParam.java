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

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author lhd
 * @date 2020-07-01 15:44:27
 */
@Data
public class NotifyTemplateParam implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 1.订单催付 2.付款成功通知 3.商家同意退款 4.商家拒绝退款 5.核销提醒  6.发货提醒  7.拼团失败提醒 8.拼团成功提醒 9.拼团开团提醒 10.开通会员提醒
     * 101.退款临近超时提醒 102.确认收货提醒 103.买家发起退款提醒 104.买家已退货提醒
     */
    private Integer sendType;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 关联的商品id
     */
    private Long prodId;
    /**
     * 关联的活动id
     */
    private Long activityId;
    /**
     * 关联的活动名称
     */
    private String activityName;
    /**
     * 支付方式
     */
    private Integer payType = 0;
    /**
     * 等级名称
     */
    private String levelName;
    /**
     * 店铺id
     */
    private Long shopId = 0L;

    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 自提点名称
     */
    private String stationName;
    /**
     * 商品名称
     */
    private String prodName;
    /**
     * 退款备注
     */
    private String remark;

    /**
     * 退款原因
     */
    private String rejectMessage;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户openId
     */
    private String openId;

    /**
     * 手机号码
     */
    private String userMobile;
    /**
     * 商品数量
     */
    private Integer prodNum = 0;
    /**
     * 金额
     */
    private Double price = 0.0;

    /**
     * 退款超时时间，小时
     */
    private Long hour = 0L;
    /**
     * 开团人数
     */
    private Integer groupCount = 0;

    /**
     * 发货时间
     */
    private Date dvyTime;
    /**
     * 物流编号
     */
    private String dvyFlowId;
    /**
     * 物流公司名称
     */
    private String dvyName;
    /**
     * 取消时间
     */
    private String cancelTime;
    /**
     * 订单创建时间
     */
    private Date createTime;

}
