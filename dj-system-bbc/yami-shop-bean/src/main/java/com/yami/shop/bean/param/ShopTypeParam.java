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
import lombok.Data;

import java.util.List;

/**
 * @author cl
 * @date 2021-06-05 11:12:40
 */
@Data
public class ShopTypeParam {

    @ApiModelProperty("店铺id")
    private List<Long> shopIds;
    /**
     * 店铺类型
     * @see com.yami.shop.bean.enums.ShopType
     */
    @ApiModelProperty("店铺类型")
    private Integer type;
}
