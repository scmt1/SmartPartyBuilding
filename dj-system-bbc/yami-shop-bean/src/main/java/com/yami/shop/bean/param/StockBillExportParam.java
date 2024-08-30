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

import java.util.Date;

/**
 * @Author lth
 * @Date 2021/9/10 16:31
 */
@Data
public class StockBillExportParam {

    @ApiModelProperty("出入库单号")
    private String stockBillNo;

    @ApiModelProperty("关联订单号")
    private String sourceOrderNo;

    @ApiModelProperty("单据类型")
    private Integer stockBillType;

    @ApiModelProperty("原因")
    private String reason;

    @ApiModelProperty("状态，0：已作废，1：已出/入库，2：待提交")
    private Integer status;

    @ApiModelProperty("出入库时间")
    private Date businessTime;

    @ApiModelProperty("制单人手机号")
    private String employeeMobile;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("商品名称")
    private String prodName;

    @ApiModelProperty("商品编码")
    private String partyCode;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("实际出入库数量")
    private Integer stockCount;

    @ApiModelProperty("成本单价")
    private Double unitPrice;

    @ApiModelProperty("成本金额")
    private Double totalPrice;

    @ApiModelProperty("商品备注")
    private String prodRemark;
}
