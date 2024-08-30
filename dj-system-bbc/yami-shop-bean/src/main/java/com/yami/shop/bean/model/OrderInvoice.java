/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Citrus
 * @date 2021-08-16 14:22:47
 */
@Data
@TableName("tz_order_invoice")
@ApiModel("订单发票表")
public class OrderInvoice implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("订单发票ID")
    private Long orderInvoiceId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("发票类型 1.电子普通发票")
    private Integer invoiceType;

    @ApiModelProperty("抬头类型 1.单位 2.个人")
    private Integer headerType;

    @ApiModelProperty("抬头名称")
    private String headerName;

    @ApiModelProperty("发票税号")
    private String invoiceTaxNumber;

    @ApiModelProperty("发票内容 1.商品明细")
    private Integer invoiceContext;

    @ApiModelProperty("发票状态 1.申请中 2.已开票 3.失败")
    private Integer invoiceState;

    @ApiModelProperty("文件id")
    private Long fileId;

    @ApiModelProperty("申请时间")
    private Date applicationTime;

    @ApiModelProperty("上传时间")
    private Date uploadTime;

    @TableField(exist = false)
    @ApiModelProperty("店铺名称")
    private String shopName;

    @TableField(exist = false)
    @ApiModelProperty("用户id")
    private String userId;

    @TableField(exist = false)
    @ApiModelProperty("订单状态 1:待付款 2:待发货 3:待收货 4:待评价 5:成功 6:失败 7:待成团")
    private Integer orderStatus;
}
