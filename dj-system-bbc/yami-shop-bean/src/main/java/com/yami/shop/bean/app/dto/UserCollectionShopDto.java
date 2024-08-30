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
 * @author Yami
 */
@ApiModel("店铺收藏对象")
@Data
public class UserCollectionShopDto {

    @ApiModelProperty(value = "收藏id")
    private Long collectionId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "店铺logo图片")
    private String shopLogo;

    @ApiModelProperty(value = "店铺状态(-1:已删除 0: 停业中 1:营业中 2:平台下线 3:平台下线待审核 4:开店申请中 5:开店申请待审核)")
    private Integer shopStatus;

    @ApiModelProperty("签约起始时间")
    private Date contractStartTime;

    @ApiModelProperty("签约终止时间")
    private Date contractEndTime;

    @ApiModelProperty(value = "收藏时间")
    private Date createTime;

}
