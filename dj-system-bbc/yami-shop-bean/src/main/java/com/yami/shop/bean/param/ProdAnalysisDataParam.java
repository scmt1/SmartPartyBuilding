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
 * 商品分析data数据
 */
/**
 * @author Yami
 */
@Data
public class ProdAnalysisDataParam {

    @ApiModelProperty(value = "时间 ,例如：2020-07-04")
    private String currentDay;

    @ApiModelProperty(value = "新增商品数")
    private Long newProd = 0L;

    @ApiModelProperty(value = "被访问商品数")
    private Long visitedProd = 0L;

    @ApiModelProperty(value = "动销商品数")
    private Long dynamicSale = 0L;

    @ApiModelProperty(value = "商品曝光数")
    private Long expose = 0L;

    @ApiModelProperty(value = "商品浏览量")
    private Long browse = 0L;

    @ApiModelProperty(value = "商品访客数")
    private Long visitor = 0L;

    @ApiModelProperty(value = "加购件数")
    private Long addCart = 0L;

    @ApiModelProperty(value = "下单件数")
    private Long orderNum = 0L;

    @ApiModelProperty(value = "支付件数")
    private Long payNum = 0L;

    @ApiModelProperty(value = "分享访问次数")
    private Long shareVisit = 0L;

}
