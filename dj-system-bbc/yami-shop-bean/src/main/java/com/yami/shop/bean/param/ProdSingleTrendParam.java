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
public class ProdSingleTrendParam {

    @ApiModelProperty(value = "天数")
    private String currentDay;

    @ApiModelProperty(value = "访客数")
    private Integer visitor = 0;

    @ApiModelProperty(value = "浏览量")
    private Long browse = 0L;

    @ApiModelProperty(value = "支付人数")
    private Integer payPerson = 0;

    @ApiModelProperty(value = "支付商品件数")
    private Long payNum = 0L;
}
