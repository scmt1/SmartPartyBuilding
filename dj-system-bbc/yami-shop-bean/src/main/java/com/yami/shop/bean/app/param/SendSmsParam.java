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

import com.yami.shop.common.util.PrincipalUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @author Yami
 */
@Data
public class SendSmsParam {

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp= PrincipalUtil.MOBILE_REGEXP,message = "请输入正确的手机号")
    private String mobile;

    @ApiModelProperty(value = "是否店铺账号，1是")
    private Integer shopAccount;

}
