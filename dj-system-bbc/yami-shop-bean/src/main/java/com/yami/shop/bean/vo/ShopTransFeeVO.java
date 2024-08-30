package com.yami.shop.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 菠萝凤梨
 */
@Data
@ApiModel("店铺运费信息")
public class ShopTransFeeVO {

    @ApiModelProperty(value = "免运费金额", required = true)
    private Double freeTransFee;

    @ApiModelProperty(value = "运费", required = true)
    private Double transFee;

    @ApiModelProperty(value = "运费模板id", required = true)
    private Long deliveryTemplateId;

}
