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

import java.math.BigDecimal;

/**
 * @author Yami
 */
@Data
public class CustomerRetainRespParam {

    @ApiModelProperty("当前月")
    private String currentMonth;

    @ApiModelProperty("新访问/成交客户数")
    private Integer newCustomers;

    @ApiModelProperty("第1月留存")
    private Integer firstMonthRemain;
    @ApiModelProperty("第1月留存率")
    private BigDecimal firstMonthRemainRate;

    @ApiModelProperty("第2月留存")
    private Integer secondMonthRemain;
    @ApiModelProperty("第2月留存率")
    private BigDecimal secondMonthRemainRate;

    @ApiModelProperty("第3月留存")
    private Integer thirdMonthRemain;
    @ApiModelProperty("第3月留存率")
    private BigDecimal thirdMonthRemainRate;

    @ApiModelProperty("第4月留存")
    private Integer fourthMonthRemain;
    @ApiModelProperty("第4月留存率")
    private BigDecimal fourthMonthRemainRate;

    @ApiModelProperty("第5月留存")
    private Integer fifthMonthRemain;
    @ApiModelProperty("第5月留存率")
    private BigDecimal fifthMonthRemainRate;

    @ApiModelProperty("第6月留存")
    private Integer sixthMonthRemain;
    @ApiModelProperty("第6月留存率")
    private BigDecimal sixthMonthRemainRate;

}
