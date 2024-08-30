package com.yami.shop.bean.vo;

import com.yami.shop.bean.app.dto.ShopCartDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 菠萝凤梨
 */
@Data
@ApiModel("购物车合计")
public class ShopCartWithAmountVO {

    @ApiModelProperty("总额")
    private Double totalMoney;

    @ApiModelProperty("总计")
    private Double finalMoney;

    @ApiModelProperty("减额")
    private Double subtractMoney;

    @ApiModelProperty("商品数量")
    private Integer count;

    @ApiModelProperty(value = "运费",required=true)
    private Double freightAmount;

    @ApiModelProperty(value = "等级免运费金额", required = true)
    private Double freeTransFee;

    @ApiModelProperty("多个店铺的购物车信息")
    private List<ShopCartDto> shopCarts;

    @ApiModelProperty("运费信息")
    private UserDeliveryInfoVO userDeliveryInfo;
}
