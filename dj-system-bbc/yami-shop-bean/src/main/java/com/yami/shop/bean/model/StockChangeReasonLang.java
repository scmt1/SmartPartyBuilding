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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author lhd
 * @date 2020-08-26 15:39:09
 */
@Data
@TableName("tz_stock_change_reason_lang")
public class StockChangeReasonLang implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 出入库原因id
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "出入库原因id", required = true)
    private Long stockChangeReasonId;
    /**
     * 语言 0.中文 1.英文
     */
    @ApiModelProperty(value = "语言 0.中文 1.英文", required = true)
    private Integer lang;
    /**
     * 出入库原因
     */
    @ApiModelProperty(value = "出入库原因")
    private String reason;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
