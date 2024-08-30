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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Yami
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "支付信息参数")
public class PayInfoParam {

    @ApiModelProperty(value = "1：支付成功，2: 用户信息异常，3：余额不足")
    private Integer type;

    @ApiModelProperty(value = "支付异常信息")
    private String message;

}
