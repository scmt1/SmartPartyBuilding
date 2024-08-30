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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author LGH
 */
@ApiModel("我的订单-订单项")
@Data
public class MyOrderItemDto {

    @ApiModelProperty(value = "商品图片", required = true)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "商品名称", required = true)
    private String prodName;

    @ApiModelProperty(value = "评论时间", required = true)
    private Date recTime;

    @ApiModelProperty(value = "付款时间")
    private Date payTime;

    @ApiModelProperty(value = "订单号",required=true)
    private String orderNumber;

    @ApiModelProperty(value = "商品数量", required = true)
    private Integer prodCount;

    @ApiModelProperty(value = "商品价格", required = true)
    private Double price;

    @ApiModelProperty(value = "产品购买花费积分",required=true)
    private Integer useScore;

    @ApiModelProperty(value = "skuId", required = true)
    private Long skuId;

    @ApiModelProperty(value = "skuName", required = true)
    private String skuName;

    @ApiModelProperty(value = "订单项id", required = true)
    private Long orderItemId;

    @ApiModelProperty(value = "商品id", required = true)
    private Long prodId;

    @ApiModelProperty(value = "评论状态： 0 未评价  1 已评价", required = true)
    private Integer commSts;

    @ApiModelProperty(value = "退款单类型（1:整单退款,2:单个物品退款）",required=true)
    private Integer refundType;

    @ApiModelProperty(value = "退款状态 1.买家申请 2.卖家接受 3.买家发货 4.卖家收货 5.退款成功 6.买家撤回申请 7.商家拒绝 -1.退款关闭")
    private Integer returnMoneySts;

    @ApiModelProperty(value = "赠品列表")
    private List<MyOrderItemDto> giveawayList;

    @ApiModelProperty(value = "订单类型1团购订单 2秒杀订单 3积分订单")
    private Integer orderType;

    /**
     * 赠品主订单项id
     */
    @ApiModelProperty(value = "赠品主订单项id")
    private Long giveawayOrderItemId;

    @ApiModelProperty(value = "主商品关联退款赠品id")
    private String returnGiveawayIds;
}
