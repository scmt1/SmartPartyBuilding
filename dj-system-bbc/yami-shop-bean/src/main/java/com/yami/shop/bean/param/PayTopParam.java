/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Yami
 */
@Data
public class PayTopParam {

    @ApiModelProperty(value = "商品id")
    private Long prodId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "支付金额")
    private Double payAmount;

    @ApiModelProperty(value = "支付数量")
    private Double payCount;
}
