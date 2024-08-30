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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@TableName("tz_order")
public class Order implements Serializable {
    private static final long serialVersionUID = 6222259729062826852L;
    /**
     * 订单ID
     */
    @TableId
    private Long orderId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 产品名称,多个产品将会以逗号隔开
     */
    private String prodName;

    /**
     * 订购用户ID
     */
    private String userId;

    /**
     * 订购流水号
     */
    private String orderNumber;

    /**
     * 总值
     */
    private Double total;

    /**
     * 实际总值
     */
    private Double actualTotal;

    /**
     * 支付方式 1 微信支付 2 支付宝
     */
    private Integer payType;

    /**
     * 用户备注
     */
    private String remarks;

    /**
     * 卖家备注
     */
    private String shopRemarks;

    /**
     * 订单状态 参考com.yami.shop.bean.enums.OrderStatus
     */
    private Integer status;

    /**
     * 配送类型
     */
    private Integer dvyType;

    /**
     * 配送方式ID
     */
    private Long dvyId;

    /**
     * 物流单号
     */
    private String dvyFlowId;

    /**
     * 订单运费
     */
    private Double freightAmount;

    /**
     * 用户订单地址Id
     */
    private Long addrOrderId;

    /**
     * 订单商品总数
     */
    private Integer productNums;

    /**
     * 收件人姓名
     */
    private String receiverName;
    /**
     * 收件人电话
     */
    private String receiverMobile;

    /**
     * 订购时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 订单更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 付款时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * 发货时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dvyTime;

    /**
     * 完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finallyTime;


    /**
     * 取消时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;
    /**
     * 预售时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date preSaleTime;

    /**
     * 结算时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date settledTime;

    /**
     * 是否已经支付，1：已经支付过，0：，没有支付过
     */
    private Integer isPayed;

    /**
     * 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
     */
    private Integer deleteStatus;

    /**
     * 订单退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）
     */
    private Integer refundStatus;

    /**
     * 平台优惠总额
     */
    private Double platformAmount;

    /**
     * 优惠总额
     */
    private Double reduceAmount;
    /**
     * 积分抵扣金额
     */
    private Double scoreAmount;

    /**
     * 会员折扣金额
     */
    private Double memberAmount;

    /**
     * 平台优惠券优惠金额
     */
    private Double platformCouponAmount;

    /**
     * 商家优惠券优惠金额
     */
    private Double shopCouponAmount;

    /**
     * 满减优惠金额
     */
    private Double discountAmount;

    /**
     * 店铺套餐优惠金额
     */
    private Double shopComboAmount;

    /**
     * 店铺改价优惠金额
     */
    private Double shopChangeFreeAmount;
    /**
     * 分销佣金
     */
    private Double distributionAmount;
    /**
     * 平台佣金
     */
    private Double platformCommission;

    /**
     * 平台运费减免金额
     */
    private Double platformFreeFreightAmount;
    /**
     * 是否已经进行结算
     */
    private Integer isSettled;

    /**
     * 订单类型参考orderType ,1团购订单 2秒杀订单,3积分订单
     */
    private Integer orderType;

    /**
     * 订单类别 0.实物商品订单 1. 虚拟商品订单
     */
    private Integer orderMold;

    /**
     * 订单关闭原因 (1:超时未支付 2:退款关闭 4:买家取消 15:已通过货到付款交易)
     */
    private Integer closeType;

    /**
     * 虚拟商品的留言备注
     */
    private String virtualRemark;

    /**
     * 核销次数 -1.多次核销 0.无需核销 1.单次核销
     */
    private Integer writeOffNum;

    /**
     * 多次核销次数
     */
    private Integer writeOffMultipleCount;

    /**
     * 订单核销状态 0.待核销 1.核销完成
     */
    private Integer writeOffStatus;

    /**
     * 核销开始时间
     */
    private Date writeOffStart;

    /**
     * 核销结束时间
     */
    private Date writeOffEnd;

    /**
     * 是否可以退款 1.可以 0不可以
     */
    @ApiModelProperty(value = "是否可以退款 1.可以 0不可以")
    private Integer isRefund;

    /**
     * 店铺名称
     */
    @TableField(exist = false)
    private String shopName;
    /**
     * 商品中文名称
     */
    @TableField(exist = false)
    private String prodNameCn;
    /**
     * 商品英文名称
     */
    @TableField(exist = false)
    private String prodNameEn;

    /**
     * 买家昵称
     */
    @TableField(exist = false)
    private String nickName;
    /**
     * 买家手机号
     */
    @TableField(exist = false)
    private String userMobile;

    @TableField(exist = false)
    private List<OrderItem> orderItems;

    @TableField(exist = false)
    private List<String> writeOffCodes;

    /**
     * 用户订单地址
     */
    @TableField(exist = false)
    private UserAddrOrder userAddrOrder;

    /**
     * 退款订单编号
     */
    @TableField(exist = false)
    private String refundSn;

    /**
     * 订单所用积分
     */
    @TableField(exist = false)
    private Long score;

    /**
     * 退款状态
     */
    @TableField(exist = false)
    private Integer returnMoneySts;

    /**
     * 退款类型
     */
    @TableField(exist = false)
    private Integer refundType;

    /**
     * 亏损金额
     */
    @TableField(exist = false)
    private Double lossAmount;

    @TableField(exist = false)
    private Long payScore;

    /**
     * 商家部分地区包邮减免的运费   旧字段（用户等级免运费金额）
     */
    private Double freeTransfee;

    /**
     * 支付金额版本号
     */
    private Integer changeAmountVersion;

    /**
     * 用户地址
     */
    @TableField(exist = false)
    private String userAddr;

    @ApiModelProperty("自提点名称")
    @TableField(exist = false)
    private String stationName;

    /**
     * 立减批发价总值
     */
    private Double wholesalePriceTotal;
}
