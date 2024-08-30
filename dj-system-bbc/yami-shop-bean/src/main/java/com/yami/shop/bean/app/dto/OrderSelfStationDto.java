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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 自提订单信息表
 *
 * @author LGH
 * @date 2020-04-23 15:18:29
 */
@Data
public class OrderSelfStationDto{

    /**
     *
     */
    @ApiModelProperty(value = "自提信息id")
    private Long id;

    private Long orderId;

    @ApiModelProperty(value = "自提点id")
    private Long stationId;

    @ApiModelProperty(value = "自提点名称")
    private String stationName;

    @ApiModelProperty(value = "自提人的手机")
    private String stationUserMobile;

    @ApiModelProperty(value = "自提人的名字")
    private String stationUserName;

    @ApiModelProperty(value = "自提时间(用户下单时选择)")
    private String stationTime;

    @ApiModelProperty(value = "提货码")
    private String stationCode;

    @ApiModelProperty(value = "自提点的地址")
    private String stationAddress;

    @ApiModelProperty(value = "自提点的联系电话")
    private String stationPhone;

    @ApiModelProperty(value = "经度")
    private Double lng;

    @ApiModelProperty(value = "纬度")
    private Double lat;
}
