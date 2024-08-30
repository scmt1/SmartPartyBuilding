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
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author LHD on 2020/03/02
 */
@Data
@ApiModel(value = "会员支付参数")
public class PayUserParam {

    @NotBlank(message = "会员等级id不能为空")
    @ApiModelProperty(value = "会员等级id，当id=-1时，充值自定义金额", required = true)
    private Long id;

    @ApiModelProperty(value = "二维码携带的参数")
    private String scene;

    @ApiModelProperty(value = "支付方式 (1:微信小程序支付 2:支付宝 3微信扫码支付 4 微信h5支付 5微信公众号支付 6支付宝H5支付 7支付宝APP支付 8微信APP支付 9余额支付)", required = true)
    private Integer payType = 1;

    @ApiModelProperty(value = "支付完成回跳地址", required = true)
    private String returnUrl;

    @ApiModelProperty(value = "自定义充值金额")
    private Double customRechargeAmount;

    @ApiModelProperty(value = "用户等级id/余额支付id")
    private Long orderIds;

    @ApiModelProperty(value = "支付单号")
    private String payNo;

    @ApiModelProperty(value = "需要支付价格")
    private Double needAmount;

}
