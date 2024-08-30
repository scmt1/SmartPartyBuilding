/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Citrus
 * @date 2021/9/15 13:27
 */
@Data
public class TakeStockProdVO {

    @ApiModelProperty(value = "盘点商品id")
    private Long takeStockProdId;

    @ApiModelProperty(value = "盘点id")
    private Long takeStockId;

    @ApiModelProperty(value = "商品id")
    private Long prodId;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @ApiModelProperty(value = "skuId")
    private Long skuId;

    @ApiModelProperty(value = "规格名称")
    private String skuName;

    @ApiModelProperty(value = "规格编码")
    private String partyCode;

    @ApiModelProperty(value = "sku图片")
    private String pic;

    @ApiModelProperty(value = "账面库存")
    private Integer stocks;

    @ApiModelProperty(value = "实盘库存")
    private Integer totalStock;

    @ApiModelProperty(value = "盈亏类型 0盘平 1盘盈 2盘亏 -1异常")
    private Integer ioType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "异常原因 1.删除商品 2.盘点期间有库存变动")
    private Integer exceptionReason;
}
