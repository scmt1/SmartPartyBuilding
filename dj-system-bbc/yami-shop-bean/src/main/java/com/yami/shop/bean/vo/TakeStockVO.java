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

import java.util.Date;
import java.util.List;

/**
 * @author Citrus
 * @date 2021/9/15 13:06
 */
@Data
public class TakeStockVO {

    @ApiModelProperty(value = "盘点id")
    private Long takeStockId;

    @ApiModelProperty(value = "盘点单号")
    private String takeStockNo;

    @ApiModelProperty(value = "盘点状态 0已作废 1盘点中 2已完成")
    private Integer billStatus;

    @ApiModelProperty(value = "制单人手机号")
    private String makerMobile;

    @ApiModelProperty(value = "盘点时间")
    private Date createTime;

    @ApiModelProperty(value = "盘点区域名称")
    private String stockRegionName;

    @ApiModelProperty(value = "sku个数")
    private Integer skuCount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "盘点商品列表")
    private List<TakeStockProdVO> takeStockProdList;
}
