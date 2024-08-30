/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.search.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/11/16
 */
@Data
public class EsPageVO<T> {

    @ApiModelProperty("总页数")
    private Integer pages;

    @ApiModelProperty("总条目数")
    private Long total;

    @ApiModelProperty("结果集")
    private List<T> records;
}
