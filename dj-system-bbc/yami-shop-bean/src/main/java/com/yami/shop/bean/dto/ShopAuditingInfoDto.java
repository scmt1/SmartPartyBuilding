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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yami
 */
@Data
@ApiModel("审核信息")
public class ShopAuditingInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺审核id")
    private Long shopAuditingId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "申请时间")
    private Date createTime;

    @ApiModelProperty(value = "审核状态")
    private Integer status;

    @ApiModelProperty(value = "店铺名字")
    private String shopName;

    @ApiModelProperty(value = "店铺详细地址")
    private String shopAddress;

    @ApiModelProperty(value = "店铺所在省份")
    private String province;

    @ApiModelProperty(value = "店铺所在城市")
    private String city;

    @ApiModelProperty(value = "店铺所在区域")
    private String area;

    @ApiModelProperty(value = "店铺logo")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String shopLogo;

    @ApiModelProperty(value = "店铺简介")
    private String intro;

    @ApiModelProperty(value = "店铺状态")
    private Integer shopStatus;

    @ApiModelProperty(value = "店铺账号")
    private String mobile;

    @ApiModelProperty("0普通店铺 1优选好店")
    private Integer type;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "商家名称")
    private String merchantName;
}
