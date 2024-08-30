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

import com.yami.shop.bean.vo.GiveawayVO;
import com.yami.shop.bean.vo.VirtualRemarkVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author LGH
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ShopCartItemDto extends ProductItemDto implements Serializable {
    private static final long serialVersionUID = -8284981156242930909L;

    @ApiModelProperty(value = "购物车ID", required = true)
    private Long basketId;

    @ApiModelProperty(value = "套餐id", required = true)
    private Long comboId;

    @ApiModelProperty(value = "是否为主商品（套餐）")
    private Integer isMainProd;

    @ApiModelProperty(value = "套餐数量")
    private Integer comboCount;

    @ApiModelProperty(value = "店铺ID", required = true)
    private Long shopId;

    @ApiModelProperty(value = "店铺名称", required = true)
    private String shopName;

    @ApiModelProperty(value = "商品原价", required = true)
    private Double oriPrice;

    @ApiModelProperty(value = "等级优惠金额", required = true)
    private Double levelReduce;

    @ApiModelProperty(value = "分类扣率", required = true)
    private Double rate;

    @ApiModelProperty(value = "平台佣金", required = true)
    private Double platformCommission;

    @ApiModelProperty(value = "平台优惠券优惠金额", required = true)
    private Double platformCouponAmount;

    @ApiModelProperty(value = "商家优惠券优惠金额", required = true)
    private Double shopCouponAmount;

    @ApiModelProperty(value = "满减优惠金额", required = true)
    private Double discountAmount;

    @ApiModelProperty(value = "套餐优惠金额", required = true)
    private Double comboAmount;

    @ApiModelProperty(value = "平台运费减免金额", required = true)
    private Double platformFreeFreightAmount;

    @ApiModelProperty(value = "商家运费减免金额", required = true)
    private Double freeFreightAmount;

    @ApiModelProperty(value = "推广员使用的推销卡号")
    private String distributionCardNo;

    @ApiModelProperty(value = "加入购物车的时间")
    private Date basketDate;

    @ApiModelProperty(value = "是否收藏")
    private Boolean isCollection;

    @ApiModelProperty(value = "购物车是否勾选")
    private Boolean isChecked;

    @ApiModelProperty(value = "同城配送启用状态 :  1启用 0未启用 ")
    private Integer shopCityStatus;

    @ApiModelProperty(value = "商品类别 0.实物商品 1. 虚拟商品")
    private Integer mold;

    @ApiModelProperty(value = "虚拟商品留言备注")
    private List<VirtualRemarkVO> virtualRemarkList;

    @ApiModelProperty(value = "核销次数 -1.多次核销 0.无需核销 1.单次核销")
    private Integer writeOffNum;

    @ApiModelProperty(value = "多次核销次数 -1.无限次")
    private Integer writeOffMultipleCount;

    @ApiModelProperty(value = "核销有效期 -1.长期有效 0.自定义  x.x天内有效")
    private Integer writeOffTime;

    @ApiModelProperty(value = "核销开始时间")
    private Date writeOffStart;

    @ApiModelProperty(value = "核销结束时间")
    private Date writeOffEnd;

    @ApiModelProperty(value = "是否可以退款 1.可以 0不可以")
    private Integer isRefund;

    @ApiModelProperty(value = "主购物车id（套餐）")
    private Long parentBasketId;

    @ApiModelProperty(value = "赠品信息")
    private GiveawayVO giveaway;

    @ApiModelProperty(value = "商品类型")
    private Integer prodType;

    @ApiModelProperty(value = "虚拟商品留言备注json")
    private String virtualRemark;

    @ApiModelProperty(value = "统一运费")
    private Double deliveryAmount;

    @ApiModelProperty(value = "标识id", required = true)
    private Integer itemId;

    @ApiModelProperty(value = "批发价")
    private Double wholesalePrice;
}
