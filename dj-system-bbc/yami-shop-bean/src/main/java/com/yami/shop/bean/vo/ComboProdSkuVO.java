/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 套装商品sku项
 *
 * @author LGH
 * @date 2021-11-05 09:23:32
 */
@Data
@TableName("tz_combo_prod_sku")
public class ComboProdSkuVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "套餐商品sku项id")
    private Long comboProdId;

    @ApiModelProperty(value = "skuId")
    @NotNull(message = "skuId不能为空")
    private Long skuId;

    @ApiModelProperty(value = "搭配价格")
    @NotNull(message = "搭配价格不能为空")
    private Double matchingPrice;

    @ApiModelProperty(value = "sku名称")
    @TableField(exist = false)
    private String skuName;

    @ApiModelProperty(value = "sku图片")
    @TableField(exist = false)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "sku价格")
    @TableField(exist = false)
    private Double price;

    @ApiModelProperty(value = "sku库存")
    @TableField(exist = false)
    private Integer stocks;

    @ApiModelProperty(value = "sku状态 0 禁用 1 启用 -1 删除")
    @TableField(exist = false)
    private Integer skuStatus;

    @ApiModelProperty(value = "销售属性组合字符串 格式是p1:v1;p2:v2")
    @TableField(exist = false)
    private String properties;

    /**
     * 商品是否必选：1：是 0：否
     */
    @TableField(exist = false)
    private Integer prodRequired;

}
