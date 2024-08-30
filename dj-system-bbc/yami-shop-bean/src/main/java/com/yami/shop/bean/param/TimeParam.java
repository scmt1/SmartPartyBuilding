/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Yami
 */
@Data
@AllArgsConstructor
public class TimeParam {

    @ApiModelProperty("当前日期")
    private String dateTime;

    @ApiModelProperty("当前营业时间集合")
    private List<String> hourTimes;
}
