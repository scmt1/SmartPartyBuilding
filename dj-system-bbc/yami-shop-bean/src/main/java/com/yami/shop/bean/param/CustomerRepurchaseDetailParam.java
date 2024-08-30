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
public class CustomerRepurchaseDetailParam {


    private String level = "-99";

    @ApiModelProperty("成交金额")
    private Double payAmount = 0.0;

    @ApiModelProperty("成交购买客户数")
    private Integer payBuyers = 0;

    @ApiModelProperty("成交购买数量")
    private Integer payProdCount = 0;

    @ApiModelProperty("回购次数")
    private Integer repurchaseCount = 0;
}
