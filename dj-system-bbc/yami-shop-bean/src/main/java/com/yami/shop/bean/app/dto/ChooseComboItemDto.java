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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
 * 购物车中选中的套餐活动项信息
 * @author Yami
 */
@Data
public class ChooseComboItemDto implements Serializable {

    @ApiModelProperty(value = "套餐id")
    private Long comboId;

    @ApiModelProperty(value = "主商品购物车id")
    private Long mainProdBasketId;

    @ApiModelProperty(value = "套餐总金额")
    private Double comboTotalAmount;

    @ApiModelProperty(value = "套餐金额(套餐单价)")
    private Double comboAmount;

    @ApiModelProperty(value = "套餐优惠金额")
    private Double preferentialAmount;

    @ApiModelProperty(value = "套餐数量")
    private Integer comboCount;

    @ApiModelProperty(value = "套餐名称")
    private String name;

    /**
     * 套餐序号
     * 套餐可能会进行分单操作，且一个订单中可能包含多个相同套餐不同规格，所以需要一个独立的参数来标识套餐
     */
    private Integer index;
}
