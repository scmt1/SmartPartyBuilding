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

import java.util.List;

/**
 * @author Yami
 */
@Data
public class DeliveryDto {

    @ApiModelProperty(value = "物流公司名称",required=true)
    private String companyName;

    @ApiModelProperty(value = "物流公司官网",required=true)
    private String companyHomeUrl;

    @ApiModelProperty(value = "物流订单号",required=true)
    private String dvyFlowId;

    @ApiModelProperty(value = "物流状态 0:没有记录 1:已揽收 2:运输途中 201:达到目的城市 3:已签收 4:问题件",required=true)
    private Integer State;

    @ApiModelProperty(value = "查询出的物流信息",required=true)
    private List<DeliveryInfoDto> Traces;

}
