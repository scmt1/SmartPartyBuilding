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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Yami
 */
@Data
public class OrderRefundParam {

    @NotEmpty(message = "退款ID不能为空")
    @ApiModelProperty(value = "退款ID")
    private Long refundId;

    @NotEmpty(message = "退款编号不能为空")
    @ApiModelProperty(value = "退款编号")
    private String refundSn;

    @NotNull(message = "退款编号不能为空")
    @ApiModelProperty(value = "处理状态（2:同意，3:不同意）")
    private Integer refundSts;

    @ApiModelProperty(value = "退款原因")
    private String rejectMessage;

    @ApiModelProperty(value = "退款地址标识（ID）")
    private Long refundAddrId;

    @ApiModelProperty(value = "商家备注")
    private String sellerMsg;

    @ApiModelProperty(value = "是否收到货")
    private Boolean isReceiver;
}
