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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 出入库明细
 *
 * @author LGH
 * @date 2021-09-09 13:11:15
 */
@Data
@TableName("tz_stock_bill_log")
public class StockBillLog implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    private Long stockBillLogId;

    @ApiModelProperty("出入库单id列表")
    @TableField(exist = false)
    private List<Long> stockBillLogIds;

    @ApiModelProperty("出入库项id列表")
    @TableField(exist = false)
    private List<Long> stockBillLogItemIds;

    @ApiModelProperty("出入库单号")
    private String stockBillNo;

    @ApiModelProperty("关联订单号")
    private String sourceOrderNo;

    @ApiModelProperty("单据类型")
    private Integer stockBillType;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("创建开始时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createStartTime;

    @ApiModelProperty("创建结束时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createEndTime;

    @ApiModelProperty("更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("出入库原因id")
    private Integer stockChangeReasonId;

    @ApiModelProperty("操作员工id")
    private Long employeeId;

    @ApiModelProperty("状态，0：已作废，1：已出/入库，2：待提交")
    private Integer status;

    @ApiModelProperty("出入库时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date businessTime;

    @ApiModelProperty("出入库开始时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date businessStartTime;

    @ApiModelProperty("出入库结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date businessEndTime;

    @ApiModelProperty("商品名称")
    @TableField(exist = false)
    private String prodName;

    @ApiModelProperty("1：商品名称 2：商品编码")
    @TableField(exist = false)
    private Integer prodKeyType;

    @ApiModelProperty("搜索商品关键词(0:商品名称 1：商品编码)")
    @TableField(exist = false)
    private String prodKey;

    @ApiModelProperty("商品编码")
    @TableField(exist = false)
    private String partyCode;

    @ApiModelProperty("总出入库数量")
    private Integer totalCount;

    @ApiModelProperty("总出入库金额")
    private Double totalAmount;

    @ApiModelProperty("出入库凭证")
    private String qualifications;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("供应商id")
    private Long supplierId;


    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("类型，1：出库 2：入库")
    @Max(value = 2, message = "只能为1或2")
    @Min(value = 1, message = "只能为1或2")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @TableField(exist = false)
    @ApiModelProperty("出入库商品项列表")
    private List<StockBillLogItem> stockBillLogItems;

    @ApiModelProperty("制单人手机号")
    @TableField(exist = false)
    private String employeeMobile;

    @ApiModelProperty("其他出/入库原因")
    @TableField(exist = false)
    private String reason;

    @ApiModelProperty("供应商名称")
    @TableField(exist = false)
    private String supplierName;

    @ApiModelProperty("制单人手机号")
    private String makerMobile;
}
