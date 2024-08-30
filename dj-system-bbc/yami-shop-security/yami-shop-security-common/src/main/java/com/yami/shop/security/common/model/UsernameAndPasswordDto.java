/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author yami
 */
@Data
@ApiModel(value= "用户名和密码参数")
public class UsernameAndPasswordDto {

    @NotBlank(message="用户名不能为空")
    @Size(max = 50)
    @ApiModelProperty(value = "用户名",required=true)
    private String username;

    @NotBlank(message="密码不能为空")
    @Size(max = 50)
    @ApiModelProperty(value = "密码",required=true)
    private String password;

    @NotBlank(message="验证码不能为空")
    @Size(max = 50)
    @ApiModelProperty(value = "验证码",required=true)
    private String code;
}
