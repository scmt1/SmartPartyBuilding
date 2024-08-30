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
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Yami
 */
@Data
@Accessors(chain = true)
public class StatisticsRefundParam {

    @ApiModelProperty(value = "退款金额")
    private double payActualTotal;

    @ApiModelProperty(value = "退款成功订单数")
    private int refundCount;

    @ApiModelProperty(value = "退款原因")
    private String buyerReason;

    @ApiModelProperty(value = "退款商品名称")
    private String refundPordName;

    @ApiModelProperty(value = "较前一日金额的比率")
    private Double yesterdayAmountRate;

    @ApiModelProperty(value = "较七天前金额的比率")
    private Double sevenDayAmountRate;

    @ApiModelProperty(value = "退款率")
    private Double refundRate;

    @ApiModelProperty(value = "较前一日退款率的比率")
    private Double yesterdayRefundRate;

    @ApiModelProperty(value = "较七天前退款率的比率")
    private Double sevenDayRefundRate;

    @ApiModelProperty(value = "退款日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date refundDate;

    @ApiModelProperty(value = "退款日期")
    private String refundDateToString;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "商品图片")
    private String pic;


}
