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
@TableName("tz_prod_lang")
public class ProdLang implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "商品id", required = true)
    private Long prodId;
    /**
     * 语言 0.中文 1.英文
     */
    @ApiModelProperty(value = "语言 0.中文 1.英文", required = true)
    private Integer lang;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String prodName;
    /**
     * 简要描述,卖点等
     */
    @ApiModelProperty(value = "简要描述,卖点等")
    private String brief;
    /**
     * 详细描述
     */
    @ApiModelProperty(value = "详细描述")
    private String content;

}
