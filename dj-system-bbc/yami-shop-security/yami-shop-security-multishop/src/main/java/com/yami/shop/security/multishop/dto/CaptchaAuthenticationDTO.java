/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.multishop.dto;

import com.yami.shop.security.common.dto.AuthenticationDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 验证码登陆
 *
 * @author FrozenWatermelon
 * @date 2020/7/1
 */
@Data
public class CaptchaAuthenticationDTO extends AuthenticationDTO {

    @ApiModelProperty(value = "验证码", required = true)
    private String captchaVerification;


    @ApiModelProperty(value = "验证码id", required = true)
    private String uuid;
    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;
    public String getCaptchaVerification() {
        return captchaVerification;
    }

    public void setCaptchaVerification(String captchaVerification) {
        this.captchaVerification = captchaVerification;
    }


    @Override
    public String toString() {
        return "CaptchaAuthenticationDTO{" + "captchaVerification='" + captchaVerification + '\'' + "} "
                + super.toString();
    }

}
