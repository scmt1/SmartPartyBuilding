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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Yami
 */
@Data
public class CustomerConsumeRespParam {

    @ApiModelProperty("结束范围")
    private Double end = 0.0;

    @ApiModelProperty("开始范围")
    private Double start = 0.0;

    @ApiModelProperty("新成交客户支付金额，保留两位小数的数字")
    private Double newPayAmount = 0.0;

    @ApiModelProperty("新成交客户，有多少人购买")
    private Integer newPayBuyers = 0;

    @ApiModelProperty("新成交客户，商品支付件数")
    private Integer newPayProdCount = 0;

    @ApiModelProperty("老成交客户支付金额，保留两位小数的数字")
    private Double oldPayAmount = 0.0;

    @ApiModelProperty("老成交客户，有多少人购买")
    private Integer oldPayBuyers = 0;

    @ApiModelProperty("老成交客户，商品支付件数")
    private Integer oldPayProdCount = 0;

}
