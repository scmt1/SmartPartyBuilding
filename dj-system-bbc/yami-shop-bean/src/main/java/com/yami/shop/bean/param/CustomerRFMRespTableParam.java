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
public class CustomerRFMRespTableParam{

    @ApiModelProperty("频次")
    private Integer frequency = 0;

    @ApiModelProperty("支付金额")
    private Double payAmount1 = 0.0;
    @ApiModelProperty("购买客户数")
    private Integer payBuyers1 = 0;
    private Double payBuyers1Rate = 0.0;
    @ApiModelProperty("客单价")
    private Double priceSingle1 = 0.0;

    private Double payAmount2 = 0.0;
    private Integer payBuyers2 = 0;
    private Double payBuyers2Rate = 0.0;
    private Double priceSingle2 = 0.0;

    private Double payAmount3 = 0.0;
    private Integer payBuyers3 = 0;
    private Double payBuyers3Rate = 0.0;
    private Double priceSingle3 = 0.0;


    private Double payAmount4 = 0.0;
    private Integer payBuyers4 = 0;
    private Double payBuyers4Rate = 0.0;
    private Double priceSingle4 = 0.0;

    private Double payAmount5 = 0.0;
    private Integer payBuyers5 = 0;
    private Double payBuyers5Rate = 0.0;
    private Double priceSingle5 = 0.0;

    private Double payAmountTotal = 0.0;
    private Integer payBuyersTotal = 0;
    private Double payBuyersTotalRate = 0.0;
    private Double priceSingleTotal = 0.0;

    @ApiModelProperty("近期时间；1：R<=30 2：30<R<=90 3：90<R<=180 4：180<R<=365 5：R>365")
    private Integer recency;
    private String recencyName;

    /**
     * 总金额
     */
    private Double amount;

    /**
     * 用户数量
     */
    private Integer userNum;


}
