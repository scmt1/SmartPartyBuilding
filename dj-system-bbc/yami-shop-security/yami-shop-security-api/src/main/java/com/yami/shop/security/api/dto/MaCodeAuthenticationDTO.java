/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 小程序通过code进行登录
 *
 * @author FrozenWatermelon
 * @date 2022/09/06
 */
@Data
public class MaCodeAuthenticationDTO{


    @NotBlank
    @ApiModelProperty(value = "临时的uid，微信公众号支付需要openid，但用户又不绑定社交账号，所以这个openId是临时的")
    private String tempUid;

    @NotBlank
    @ApiModelProperty(value = "小程序通过getPhoneNumber方法获取的code")
    private String code;
}
