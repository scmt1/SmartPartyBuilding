package com.yami.shop.bean.app.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author FrozenWatermelon
 * @date 2021-02-03 15:47:32
 */
@Data
public class CheckShopCartItemParam {

    @NotNull
    @ApiModelProperty(value = "购物车ID", required = true)
    private Long basketId;

    @NotNull
    @ApiModelProperty(value = "商品是否勾选 1:勾选 0:未勾选")
    private Integer isChecked;
}
