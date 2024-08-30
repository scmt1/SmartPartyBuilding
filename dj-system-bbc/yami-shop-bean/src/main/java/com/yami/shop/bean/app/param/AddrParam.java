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

import javax.validation.constraints.NotNull;

/**
 * @author Yami
 */
@Data
@ApiModel(value= "地址参数")
public class AddrParam {

    @ApiModelProperty(value = "地址ID",required=true)
    private Long addrId;

    @ApiModelProperty(value = "邮编")
    private String postCode;

    @NotNull(message = "收货人不能为空")
    @ApiModelProperty(value = "收货人",required=true)
    private String receiver;

    @NotNull(message = "地址不能为空")
    @ApiModelProperty(value = "地址",required=true)
    private String addr;

    @NotNull(message = "手机不能为空")
    @ApiModelProperty(value = "手机",required=true)
    private String mobile;

    @NotNull(message = "省ID不能为空")
    @ApiModelProperty(value = "省ID",required=true)
    private Long provinceId;

    @NotNull(message = "城市ID不能为空")
    @ApiModelProperty(value = "城市ID",required=true)
    private Long cityId;

    @NotNull(message = "区ID不能为空")
    @ApiModelProperty(value = "区ID",required=true)
    private Long areaId;

    @NotNull(message = "省不能为空")
    @ApiModelProperty(value = "省",required=true)
    private String province;

    @NotNull(message = "城市不能为空")
    @ApiModelProperty(value = "城市",required=true)
    private String city;

    @NotNull(message = "区不能为空")
    @ApiModelProperty(value = "区",required=true)
    private String area;

    @ApiModelProperty(value = "纬度",required=true)
    private Double lat;

    @ApiModelProperty(value = "经度",required=true)
    private Double lng;

}
