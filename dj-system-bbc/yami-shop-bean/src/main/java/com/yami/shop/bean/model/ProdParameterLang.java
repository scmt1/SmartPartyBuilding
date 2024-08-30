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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品参数语言表
 * @author Citrus
 * @date 2021-11-02 11:26:08
 */
@Data
@TableName("tz_prod_parameter_lang")
public class ProdParameterLang implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "商品参数id")
    private Long prodParameterId;

    @ApiModelProperty(value = "语言")
    private Integer lang;

    @ApiModelProperty(value = "参数名")
    private String parameterKey;

    @ApiModelProperty(value = "参数值")
    private String parameterValue;
}
