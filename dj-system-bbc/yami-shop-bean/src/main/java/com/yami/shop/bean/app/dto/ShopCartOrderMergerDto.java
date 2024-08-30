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

import com.yami.shop.bean.enums.OrderType;
import com.yami.shop.bean.model.OrderInvoice;
import com.yami.shop.bean.vo.VirtualRemarkVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 多个店铺订单合并在一起的合并类
 * "/confirm" 使用
 *
 * @author Yami
 */
@Data
public class ShopCartOrderMergerDto implements Serializable {

    @ApiModelProperty(value = "实际总值", required = true)
    private Double actualTotal;

    @ApiModelProperty(value = "商品总值", required = true)
    private Double total;

    @ApiModelProperty(value = "商品总数", required = true)
    private Integer totalCount;

    @ApiModelProperty(value = "订单优惠金额(所有店铺优惠金额和使用积分抵现相加)", required = true)
    private Double orderReduce = 0.0;

    @ApiModelProperty(value = "订单所需积分", required = true)
    private Long scorePrice;

    @ApiModelProperty(value = "过滤掉的商品项", required = true)
    private List<ShopCartItemDto> filterShopItems;

    @ApiModelProperty(value = "整个订单可以使用的积分数", required = true)
    private Long totalUsableScore = 0L;

    @ApiModelProperty(value = "整个订单最多可以使用的积分数", required = true)
    private Long maxUsableScore = 0L;

    @ApiModelProperty(value = "用户使用积分数量", required = true)
    private Long usableScore;

    @ApiModelProperty(value = "积分抵扣金额", required = true)
    private Double totalScoreAmount;

    @ApiModelProperty(value = "购物积分抵现比例", required = true)
    private Double shopUseScore;

    @ApiModelProperty(value = "等级折扣金额", required = true)
    private Double totalLevelAmount;

    @ApiModelProperty(value = "免运费金额", required = true)
    private Double freeTransFee = 0.0;

    @ApiModelProperty(value = "总运费", required = true)
    private Double totalTransFee;

    @ApiModelProperty(value = "地址Dto")
    private UserAddrDto userAddr;

    @ApiModelProperty(value = "自提信息Dto")
    private OrderSelfStationDto orderSelfStationDto;

    @ApiModelProperty(value = "常用自提点信息")
    private List<OrderSelfStationDto> orderSelfStationList;

    @ApiModelProperty(value = "每个店铺的购物车信息", required = true)
    private List<ShopCartOrderDto> shopCartOrders;

    @ApiModelProperty(value = "整个订单可以使用的优惠券列表", required = true)
    private List<CouponOrderDto> coupons;

    @ApiModelProperty(value = "用户是否选择积分抵现(0不使用 1使用 默认不使用)")
    private Integer isScorePay;

    @ApiModelProperty(value = "配送类型 1:快递 2:自提 3:无需快递")
    private Integer dvyType;

    @ApiModelProperty(value = "用户选择的自提id")
    private Long stationId;

    @ApiModelProperty(value = "同城配送可用状态 : 1 可用 -1 不在范围内 -2 商家没有配置同城配送信息 -3 起送费不够", required = true)
    private Integer shopCityStatus;

    @ApiModelProperty(value = "同城配送起送费")
    private Double startDeliveryFee;

    @ApiModelProperty(value = "是否预售订单 1：是 0：不是")
    private Integer preSellStatus;

    @ApiModelProperty(value = "发票信息")
    private List<OrderInvoice> orderInvoiceList;

    @ApiModelProperty(value = "订单店铺优惠金额(所有店铺优惠金额)", required = true)
    private Double orderShopReduce;

    @ApiModelProperty(value = "商品类别 0.实物商品 1. 虚拟商品")
    private Integer mold = 0;

    @ApiModelProperty(value = "秒杀商品skuId")
    private Long seckillSkuId;

    @ApiModelProperty(value = "秒杀id")
    private Long seckillId;

    @ApiModelProperty(value = "虚拟商品留言备注")
    private List<VirtualRemarkVO> virtualRemarkList;

    @ApiModelProperty(value = "订单类型")
    private OrderType orderType;

    @ApiModelProperty(value = "", required = true)
    private String userId;

    @ApiModelProperty(value = "立减批发价总值")
    private Double wholesalePriceTotal;
}
