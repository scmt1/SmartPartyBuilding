/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 店铺的头信息
 * @author LGH
 */
@Data
@ApiModel("店铺的头信息")
public class ShopHeadInfoDto {

    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;

    @ApiModelProperty(value = "店铺名称", required = true)
    private String shopName;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "店铺logo", required = true)
    private String shopLogo;

    @ApiModelProperty(value = "粉丝数量", required = true)
    private Integer fansCount;

    @ApiModelProperty(value = "商品数量", required = true)
    private Integer prodCount;

    @ApiModelProperty(value = "店铺简介", required = true)
    private String intro;

    @ApiModelProperty(value = "店铺主页id", required = true)
    private Long renovationId;

    @ApiModelProperty(value = "店铺联系电话", required = true)
    private String tel;

    @ApiModelProperty(value = "店铺状态(-1:未开通 0: 停业中 1:营业中 2:平台下线 3:平台下线待审核)，可修改")
    private Integer shopStatus;

    @ApiModelProperty("0普通店铺 1优选好店")
    private Integer type;

    @ApiModelProperty(value = "签约起始时间")
    private Date contractStartTime;

    @ApiModelProperty(value = "签约终止时间")
    private Date contractEndTime;
}
