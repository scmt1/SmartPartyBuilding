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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author lth
 * @Date 2021/8/12 14:16
 */
@Data
public class ShopSigningInfoDTO {
    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("签约起始时间")
    @NotNull(message = "签约起始时间不能为空")
    private Date contractStartTime;

    @ApiModelProperty("签约终止时间")
    @NotNull(message = "签约终止时间不能为空")
    private Date contractEndTime;

    @ApiModelProperty("0普通店铺 1优选好店")
    @Max(value = 1, message = "只能为0或1")
    @Min(value = 0, message = "只能为0或1")
    private Integer type;
}
