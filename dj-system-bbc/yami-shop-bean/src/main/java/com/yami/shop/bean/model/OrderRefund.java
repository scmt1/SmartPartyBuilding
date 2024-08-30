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
import com.yami.shop.bean.app.dto.DeliveryDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author LGH
 * @date 2019-08-20 09:55:01
 */
@Data
@TableName("tz_order_refund")
public class OrderRefund implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "记录ID")
    private Long refundId;

    @ApiModelProperty(value = "退款编号")
    private String refundSn;

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "买家ID")
    private String userId;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "退款单类型（1:整单退款,2:单个物品退款）")
    private Integer refundType;

    @ApiModelProperty(value = "订单项ID")
    private Long orderItemId;

    @ApiModelProperty(value = "退货数量(0:为全部订单项)")
    private Integer goodsNum;

    @ApiModelProperty(value = "退款金额")
    private Double refundAmount;

    @ApiModelProperty(value = "最大退款金额")
    private Double maxRefundAmount;

    @ApiModelProperty(value = "当前退款的实付金额（实付金额减去不退回赠品的金额）")
    private Double refundActualTotal;

    @TableField(exist = false)
    @ApiModelProperty(value = "退还积分")
    private Long refundScore;

    @TableField(exist = false)
    @ApiModelProperty(value = "运费")
    private Double freightAmount;

    @ApiModelProperty(value = "申请类型:1,仅退款,2退款退货")
    private Integer applyType;

    @ApiModelProperty(value = "是否接收到商品(1:已收到,0:未收到)")
    private Boolean isReceiver;

    @ApiModelProperty(value = "申请原因")
    private String buyerReason;

    @ApiModelProperty(value = "申请说明")
    private String buyerDesc;

    @ApiModelProperty(value = "联系方式")
    private String buyerMobile;

    @ApiModelProperty(value = "文件凭证json")
    private String photoFiles;

    @ApiModelProperty(value = "处理退款状态:(1.买家申请 2.卖家接受 3.买家发货 4.卖家收货 5.退款成功 6.买家撤回申请 7.商家拒绝 -1.退款关闭)详情见ReturnMoneyStsType")
    private Integer returnMoneySts;

    @ApiModelProperty(value = "拒绝原因")
    private String rejectMessage;

    @ApiModelProperty(value = "卖家备注")
    private String sellerMsg;

    @ApiModelProperty(value = "申请时间")
    private Date applyTime;

    @ApiModelProperty(value = "受理时间")
    private Date handelTime;

    @ApiModelProperty(value = "发货时间")
    private Date shipTime;

    @ApiModelProperty(value = "收货时间")
    private Date receiveTime;

    @ApiModelProperty(value = "撤销时间")
    private Date cancelTime;

    @ApiModelProperty(value = "退款时间")
    private Date refundTime;

    @ApiModelProperty(value = "同意退款时间")
    private Date decisionTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "拒绝时间")
    private Date rejectTime;

    @ApiModelProperty(value = "退款单总分销金额")
    private Double distributionTotalAmount = 0.0;

    @ApiModelProperty(value = "平台退款金额（退款时将这部分钱退回给平台，所以商家要扣除从平台这里获取的金额）")
    private Double platformRefundAmount = 0.0;

    @ApiModelProperty(value = "平台佣金退款金额")
    private Double platformRefundCommission = 0.0;

    @ApiModelProperty(value = "退款退回的赠品订单项ids")
    private String returnGiveawayIds;

    @ApiModelProperty("物流信息")
    @TableField(exist = false)
    private DeliveryDto deliveryDto;

}
