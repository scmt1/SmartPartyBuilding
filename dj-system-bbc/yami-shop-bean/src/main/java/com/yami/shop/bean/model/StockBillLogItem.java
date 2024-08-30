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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 出入库商品项
 *
 * @author LGH
 * @date 2021-09-09 13:11:15
 */
@Data
@TableName("tz_stock_bill_log_item")
public class StockBillLogItem implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("出入库商品项id")
    private Long stockBillLogItemId;

    @ApiModelProperty("出入库明细id")
    private Long stockBillLogId;

    @ApiModelProperty("实际出入库数量")
    private Integer stockCount;

    @ApiModelProperty("商品库存剩余量")
    private Integer afterStock;

    @ApiModelProperty("出入库单号")
    @TableField(exist = false)
    private String stockBillNo;

    @ApiModelProperty("关联订单号")
    @TableField(exist = false)
    private String sourceOrderNo;

    @ApiModelProperty("单据类型")
    @TableField(exist = false)
    private Integer stockBillType;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date createTime;

    @ApiModelProperty("成本单价")
    private Double unitPrice;

    @ApiModelProperty("商品id")
    private Long prodId;

    @TableField(exist = false)
    @ApiModelProperty("商品名称")
    private String prodName;

    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("商品编码")
    @TableField(exist = false)
    private String partyCode;

    @ApiModelProperty("sku名称")
    @TableField(exist = false)
    private String skuName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("商品图片")
    @TableField(exist = false)
    private String pic;

    @ApiModelProperty("sku总库存")
    @TableField(exist = false)
    private Integer stocks;

    @ApiModelProperty("制单人手机号")
    @TableField(exist = false)
    private String employeeMobile;

    @ApiModelProperty("1：出库 2：入库")
    @TableField(exist = false)
    private Integer stockType;

    @ApiModelProperty("关联的商品状态")
    @TableField(exist = false)
    private Integer status;

    @ApiModelProperty("商品是否已删除")
    @TableField(exist = false)
    private Integer isDelete;
}
