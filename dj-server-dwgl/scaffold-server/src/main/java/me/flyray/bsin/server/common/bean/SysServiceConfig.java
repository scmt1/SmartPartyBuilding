/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.common.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统服务条例开关配置
 * @author Citrus
 */
@Data
public class SysServiceConfig {

    @ApiModelProperty("是否开启服务条款")
    private Boolean serviceTermsSwitch;

    @ApiModelProperty("是否开启隐私策略")
    private Boolean privacyPolicySwitch;

    @ApiModelProperty("是否开启商家注册协议")
    private Boolean merchantRegisterProtocolSwitch;

    @ApiModelProperty("是否开启开店协议")
    private Boolean shopProtocolSwitch;
}
