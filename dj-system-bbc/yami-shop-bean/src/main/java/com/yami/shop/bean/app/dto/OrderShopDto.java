/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.dto;

import com.yami.shop.bean.model.OrderVirtualInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单下的每个店铺
 *
 * @author YaMi
 */
@Data
public class OrderShopDto implements Serializable {

    /**
     * 店铺ID
     **/
    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;

    /**
     * 店铺名称
     **/
    @ApiModelProperty(value = "店铺名称", required = true)
    private String shopName;

    @ApiModelProperty(value = "实际总值", required = true)
    private Double actualTotal;

    @ApiModelProperty(value = "商品总值", required = true)
    private Double total;

    @ApiModelProperty(value = "商品总数", required = true)
    private Integer totalNum;

    @ApiModelProperty(value = "地址Dto", required = true)
    private UserAddrDto userAddrDto;

    @ApiModelProperty(value = "支付方式",required=true)
    private Integer payType;

    @ApiModelProperty(value = "产品信息", required = true)
    private List<OrderItemDto> orderItemDtos;

    @ApiModelProperty(value = "运费", required = true)
    private Double transfee;

    @ApiModelProperty(value = "优惠总额", required = true)
    private Double reduceAmount;

    @ApiModelProperty(value = "促销活动优惠金额", required = true)
    private Double discountMoney;

    @ApiModelProperty(value = "店铺优惠金额")
    private Double shopAmount;

    @ApiModelProperty(value = "店铺优惠券优惠金额", required = true)
    private Double shopCouponMoney;
    @ApiModelProperty(value = "积分抵扣金额")
    private Double scoreAmount;

    @ApiModelProperty(value = "会员折扣金额")
    private Double memberAmount;

    @ApiModelProperty(value = "店铺套餐优惠金额")
    private Double shopComboAmount;

    @ApiModelProperty(value = "平台优惠券优惠金额")
    private Double platformCouponAmount;

    @ApiModelProperty(value = "平台运费减免金额")
    private Double platformFreeFreightAmount;

    @ApiModelProperty(value = "店铺改价优惠金额")
    private Double shopChangeFreeAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单创建时间", required = true)
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单付款时间", required = false)
    private Date payTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单发货时间", required = false)
    private Date dvyTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单完成时间", required = false)
    private Date fianllyTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单取消时间", required = false)
    private Date cancelTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单更新时间", required = false)
    private Date updateTime;

    /**
     * 订单备注信息
     */
    @ApiModelProperty(value = "订单备注信息", required = true)
    private String remarks;

    /**
     * 配送类型
     */
    @ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递", required = true)
    private Integer dvyType;

    /**
     * 订单类型1团购订单 2秒杀订单
     */
    @ApiModelProperty(value = "订单类型（1团购订单 2秒杀订单）", required = true)
    private Integer orderType;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态", required = true)
    private Integer status;

    @ApiModelProperty(value = "能否退款", required = true)
    private Boolean canRefund = false;

    @ApiModelProperty(value = "能否整单退款", required = true)
    private Boolean canAllRefund = false;

    @ApiModelProperty(value = "当前可退款金额")
    private Double canRefundAmount;

    @ApiModelProperty(value = "订单积分")
    private Long orderScore = 0L;

    @ApiModelProperty(value = "秒杀id")
    private Long seckillId;

    @ApiModelProperty(value = "店铺免运费金额")
    private Double freeTransfee;

    @ApiModelProperty(value = "订单发票id")
    private Long orderInvoiceId;

    @ApiModelProperty(value = "订单退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）")
    private Integer refundStatus;

    @ApiModelProperty(value = "订单类别 0.实物商品订单 1. 虚拟商品订单")
    private Integer orderMold;

    @ApiModelProperty(value = "订单留言信息")
    private String virtualRemark;

    @ApiModelProperty(value = "订单卡券信息")
    private List<OrderVirtualInfo> virtualInfoList;

    @ApiModelProperty(value = "核销次数 -1.多次核销 0.无需核销 1.单次核销")
    private Integer writeOffNum;

    @ApiModelProperty(value = "多次核销次数 -1.无限次")
    private Integer writeOffMultipleCount;

    @ApiModelProperty(value = "核销开始时间")
    private Date writeOffStart;

    @ApiModelProperty(value = "核销结束时间")
    private Date writeOffEnd;

    @ApiModelProperty(value = "立减批发价总值")
    private Double wholesalePriceTotal;
}
