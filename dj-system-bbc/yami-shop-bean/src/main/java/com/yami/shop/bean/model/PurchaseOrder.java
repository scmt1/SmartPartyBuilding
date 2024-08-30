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
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author YXF
 * @date 2021-09-08 11:06:50
 */
@Data
@TableName("tz_purchase_order")
public class PurchaseOrder implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "采购id")
    private Long purchaseOrderId;

    @ApiModelProperty(value = "采购编号")
    private String purchaseNumber;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @NotNull(message = "送达时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "送达时间")
    private Date deliverTime;

    @NotNull(message = "配送类型不能为空")
    @ApiModelProperty(value = "配送类型 1:快递 3无需快递")
    private Integer dvyType;

    @ApiModelProperty(value = "配送方式ID")
    private Long dvyId;

    @ApiModelProperty(value = "物流单号")
    private String dvyFlowId;

    @ApiModelProperty(value = "供应商id")
    @NotNull(message = "供应商不能为空")
    private Long supplierId;

    @ApiModelProperty(value = "总采购金额")
    private Double totalAmount;

    @ApiModelProperty(value = "总采购库存数量")
    private Integer totalStock;

    @ApiModelProperty(value = "实际总库存数量")
    private Integer actualTotalStock;

    @ApiModelProperty(value = "状态 0:已作废 1:待入库 2:部分入库 3:已完成")
    private Integer status;

    @ApiModelProperty(value = "操作员工id")
    private Long employeeId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "采购商品列表")
    @NotEmpty(message = "采购商品列表不能为空")
    @TableField(exist = false)
    private List<PurchaseProd> purchaseProds;

    @ApiModelProperty(value = "供应商")
    @TableField(exist = false)
    private String supplierName;
}
