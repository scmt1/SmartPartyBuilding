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

import com.yami.shop.security.common.dto.AuthenticationDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 用于登陆传递账号密码
 *
 * @author FrozenWatermelon
 * @date 2020/7/1
 */
@Data
public class SocialAuthenticationDTO extends AuthenticationDTO {


    @ApiModelProperty(value = "临时的uid，微信公众号支付需要openid，但用户又不绑定社交账号，所以这个openId是临时的")
    private String tempUid;

    @ApiModelProperty(value = "登录类型 0.普通的系统 1.小程序 2.公众号")
    private Integer socialType;
}
