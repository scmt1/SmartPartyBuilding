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

/**
 * 商品分析参数
 */
/**
 * @author Yami
 */
@Data
public class ProdAnalysisParam{

    /**
     * 当前数据
     */
    @ApiModelProperty("当前数据")
    private ProdAnalysisDataParam data;
    /**
     * 之前数据
     */
    @ApiModelProperty("之前数据")
    private ProdAnalysisDataParam lastData;
    /**
     * 数据比较百分比
     */
    @ApiModelProperty("数据比较百分比")
    private ProdAnalysisRateParam rate;

}
