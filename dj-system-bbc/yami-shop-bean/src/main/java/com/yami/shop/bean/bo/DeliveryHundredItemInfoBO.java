/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单快递信息项VO
 *
 * @author lhd
 * @date 2020-05-18 15:10:00
 */
@Data
public class DeliveryHundredItemInfoBO {

    @ApiModelProperty(value = "接受站点", required = true)
    private String context;
    @ApiModelProperty(value = "接受时间", required = true)
    private String ftime;
}
