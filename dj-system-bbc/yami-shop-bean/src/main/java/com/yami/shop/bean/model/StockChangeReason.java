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

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 *
 *
 * @author LGH
 * @date 2021-09-07 16:04:18
 */
@Data
@TableName("tz_stock_change_reason")
public class StockChangeReason implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    private Long stockChangeReasonId;

    @ApiModelProperty(value = "出入库类别，1：出库 2：入库")
    @NotNull(message = "出入库类别不能为空")
    private Integer type;

    @ApiModelProperty(value = "原因")
    @TableField(exist=false)
    private String reason;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "状态，1：启用 0：禁用")
    private Integer status;

    @ApiModelProperty(value = "备注")
    @TableField(exist=false)
    private String remark;

    @ApiModelProperty(value = "是否系统内置")
    @TableField(exist=false)
    private Integer sysSet;

    @TableField(exist = false)
    @ApiModelProperty(value = "国际化信息列表")
    private List<StockChangeReasonLang> stockChangeReasonLangList;

}
