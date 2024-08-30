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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Yami
 */
@Data
@ApiModel("物流公司对象")
public class SimpleDeliveryDto {

   @ApiModelProperty("id")
    private Long dvyId;

    @ApiModelProperty("物流公司名称")
    private String dvyName;
}
