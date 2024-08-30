/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Yami
 */
@Data
@ApiModel(value = "订单参数")
public class OrderParam {

    @ApiModelProperty(value = "立即购买时提交的商品项")
    private OrderItemParam orderItem;

    @ApiModelProperty(value = "地址ID，0为默认地址", required = true)
    @NotNull(message = "地址不能为空")
    private Long addrId;

    @ApiModelProperty(value = "用户是否改变了优惠券的选择，如果用户改变了优惠券的选择，则完全根据传入参数进行优惠券的选择")
    private Integer userChangeCoupon;

    @ApiModelProperty(value = "优惠券id数组")
    private List<Long> couponIds;

    @ApiModelProperty(value = "用户优惠券id数组")
    private List<Long> couponUserIds;

    @ApiModelProperty(value = "用户是否选择积分抵现(0不使用 1使用 默认不使用)")
    private Integer isScorePay;

    @ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递 4:同城配送")
    private Integer dvyType;

    @ApiModelProperty(value = "用户是否自己选择使用多少积分，为空则为默认全部使用")
    private Long userUseScore;

    @ApiModelProperty(value = "是否预售订单 1：是 0：不是")
    private Integer preSellStatus;

}
