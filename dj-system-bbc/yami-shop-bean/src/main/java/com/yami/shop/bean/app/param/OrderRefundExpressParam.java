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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Yami
 */
@Data
public class OrderRefundExpressParam {

    @ApiModelProperty(value = "退款编号名称", required = true)
    @NotEmpty(message = "退款编号不能为空")
    private String refundSn;

    @ApiModelProperty(value = "物流公司id", required = true)
    @NotNull(message = "物流公司id不能为空")
    private Long expressId;

    @ApiModelProperty(value = "物流公司名称", required = true)
    @NotEmpty(message = "物流公司名称不能为空")
    private String expressName;

    @ApiModelProperty(value = "物流单号", required = true)
    @NotEmpty(message = "物流单号不能为空")
    private String expressNo;

    @ApiModelProperty(value = "手机号码", required = true)
    private String mobile;

    @ApiModelProperty(value = "备注信息", required = true)
    private String senderRemarks;

    @ApiModelProperty(value = "图片举证", required = true)
    private String imgs;


}
