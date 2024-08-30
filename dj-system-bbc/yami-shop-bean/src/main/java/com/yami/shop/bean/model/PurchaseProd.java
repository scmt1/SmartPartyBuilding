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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 *
 * @author YXF
 * @date 2021-09-08 10:42:00
 */
@Data
@TableName("tz_purchase_prod")
public class PurchaseProd implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "id")
    private Long purchaseProdId;

    @ApiModelProperty(value = "采购编号")
    private String purchaseNumber;

    @NotNull(message = "采购商品不能为空")
    @ApiModelProperty(value = "供应商品id")
    private Long supplierProdId;

    @ApiModelProperty(value = "商品id")
    private Long prodId;

    @ApiModelProperty(value = "sku id")
    private Long skuId;

    @NotNull(message = "采购金额不能为空")
    @ApiModelProperty(value = "采购金额")
    private Double purchaseAmount;

    @NotNull(message = "采购价不能为空")
    @ApiModelProperty(value = "采购价")
    private Double purchasePrice;

    @NotNull(message = "采购数量不能为空")
    @Min(value = 1, message = "采购数量要大于0")
    @ApiModelProperty(value = "采购库存数量")
    private Integer purchaseStock;

    @ApiModelProperty(value = "实际库存数量")
    private Integer actualStock;

    @ApiModelProperty(value = "状态 0:已作废 1:待入库 2:部分入库 3:已完成")
    private Integer status;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品主图")
    private String pic;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @TableField(exist = false)
    @ApiModelProperty(value = "sku名称")
    private String skuName;

    @TableField(exist = false)
    @ApiModelProperty(value = "商家编码")
    private String partyCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品库存")
    private Integer stocks;

    @TableField(exist = false)
    @ApiModelProperty(value = "最小订货量")
    private Integer minOrderQuantity;

    @TableField(exist = false)
    @ApiModelProperty(value = "剩余入库量")
    private Integer remainingStock;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;

}
