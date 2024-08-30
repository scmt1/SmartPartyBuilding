package com.yami.shop.bean.vo;

import com.yami.shop.bean.app.dto.OrderSelfStationDto;
import com.yami.shop.bean.model.UserAddr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 菠萝凤梨
 */
@Data
@ApiModel("运费信息")
public class UserDeliveryInfoVO {

    @ApiModelProperty(value = "常用自提点信息")
    private List<OrderSelfStationDto> orderSelfStation;

    @ApiModelProperty(value = "用户地址信息")
    private UserAddr userAddr;

    @ApiModelProperty(value = "免运费金额")
    private Double totalFreeTransFee;

    @ApiModelProperty(value = "总运费", required = true)
    private Double totalTransFee;

    @ApiModelProperty(value = "同城配送可用状态 : 1 可用 -1 不在范围内 -2 商家没有配置同城配送信息 -3 起送费不够", required = true)
    private Integer shopCityStatus;

    @ApiModelProperty(value = "同城配送起送费")
    private Double startDeliveryFee;

    private Map<Long, ShopTransFeeVO> shopIdWithShopTransFee;
}
