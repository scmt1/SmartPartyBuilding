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
 * @date 2020-08-31 13:54:58
 */
@Data
@TableName("tz_sku_lang")
public class SkuLang implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * skuId
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "skuId", required = true)
    private Long skuId;

    /**
     * 语言
     */
    @ApiModelProperty(value = "语言")
    private Integer lang;
    /**
     * 销售属性组合字符串 格式是p1:v1;p2:v2
     */
    @ApiModelProperty(value = "销售属性组合字符串 格式是p1:v1;p2:v2")
    private String properties;
    /**
     * sku名称
     */
    @ApiModelProperty(value = "sku名称")
    private String skuName;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String prodName;

}
