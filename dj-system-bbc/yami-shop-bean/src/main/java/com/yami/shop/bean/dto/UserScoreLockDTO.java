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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author FrozenWatermelon
 * @date 2020/12/22
 */
@Data
public class UserScoreLockDTO {

    @NotNull(message = "orderNumber不能为空")
    @ApiModelProperty(value = "orderNumber",required=true)
    private String orderNumber;

    @NotNull(message = "积分数量不能为空")
    @Min(value = 1,message = "积分数量不能为空")
    @ApiModelProperty(value = "积分数量",required=true)
    private Long score;

    private Integer orderType;

    public UserScoreLockDTO(String orderNumber, Long score, Integer orderType) {
        this.orderNumber = orderNumber;
        this.score = score;
        this.orderType = orderType;
    }

}
