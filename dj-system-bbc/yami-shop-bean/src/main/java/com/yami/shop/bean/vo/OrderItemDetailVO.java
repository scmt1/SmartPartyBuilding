/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Pineapple
 * @date 2021/6/9 9:25
 */
@Data
public class OrderItemDetailVO {

    @ApiModelProperty("订单项id")
    private Long orderItemId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("商品名称")
    private String prodName;

    @ApiModelProperty("规格名称")
    private String skuName;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("购物车产品个数")
    private Integer prodCount;

    @ApiModelProperty("商品总金额")
    private Double productTotalAmount;

    @ApiModelProperty("赠送主订单项id")
    private Long giveawayOrderItemId;

    /**
     * 商品实际金额 = 商品总金额 - 分摊的优惠金额
     */
    @ApiModelProperty("商品实际金额")
    private Double actualTotal;

    /**
     * 商家优惠金额[shareReduce-platformShareReduce]
     */
    @ApiModelProperty("商家优惠金额")
    private Double multishopReduce;

    @ApiModelProperty("平台优惠金额")
    private Double platformShareReduce;

    /**
     * 分销金额[推广员佣金+上级推广员佣金]
     */
    @ApiModelProperty("分销金额")
    private Double distributionAmount;

    @ApiModelProperty("使用积分")
    private Long useScore;

    @ApiModelProperty("分账比例")
    private Double rate;

    /**
     * 平台佣金(商品实际金额 * 分账比例)
     */
    @ApiModelProperty("平台佣金")
    private Double platformCommission;

    @ApiModelProperty("积分抵扣金额")
    private Double scoreAmount;

    @ApiModelProperty("会员折扣金额")
    private Double memberAmount;

    @ApiModelProperty("平台优惠券优惠金额")
    private Double platformCouponAmount;

    @ApiModelProperty("商家优惠券优惠金额")
    private Double shopCouponAmount;

    @ApiModelProperty("满减优惠金额")
    private Double discountAmount;

    @ApiModelProperty("商家运费减免金额")
    private Double freeFreightAmount;

    @ApiModelProperty("平台运费减免金额")
    private Double platformFreeFreightAmount;

    @ApiModelProperty("店铺改价优惠金额")
    private Double shopChangeFreeAmount;

    @ApiModelProperty("店铺改价平台优惠减少金额")
    private Double platformShopChangeAmount;

    @ApiModelProperty("退款金额")
    private Double refundAmount;

    @ApiModelProperty("退货数量")
    private Integer refundCount;

    @ApiModelProperty("套餐优惠金额")
    private Double comboAmount = 0.00;

    @ApiModelProperty("秒杀优惠金额")
    private Double seckillAmount = 0.00;

    @ApiModelProperty("拼团优惠金额")
    private Double groupAmount = 0.00;

}
