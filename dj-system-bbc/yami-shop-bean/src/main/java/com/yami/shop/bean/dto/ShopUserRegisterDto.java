/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto;

import com.yami.shop.bean.model.ShopDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author lth
 * @Date 2021/7/7 15:21
 */
@Data
@ApiModel("店铺用户注册信息")
public class ShopUserRegisterDto extends ShopDetail {

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "验证码", required = true)
    private String validCode;

    @ApiModelProperty(value = "店铺id，前端不传")
    private Long shopId;

    @ApiModelProperty(value = "校验登陆注册验证码成功的标识")
    private String checkRegisterSmsFlag;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("所属机构编号")
    private String orgId;
}
