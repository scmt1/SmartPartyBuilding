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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 套餐商品项
 *
 * @author LGH
 * @date 2021-11-02 10:35:08
 */
@Data
public class ComboProdVO implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "套餐商品项id")
    private Long comboProdId;

    @ApiModelProperty(value = "套餐id")
    private Long comboId;

    @ApiModelProperty(value = "商品id")
    @NotNull(message = "商品id不能为空")
    private Long prodId;

    @ApiModelProperty(value = "套餐价格")
    private Double comboPrice;

    @ApiModelProperty(value = "商品价格")
    @TableField(exist = false)
    private Double price;

    @ApiModelProperty(value = "类型：1：主商品 2：搭配商品")
    private Integer type;

    @ApiModelProperty(value = "是否必选：1：是 0：否")
    @NotNull(message = "是否必选不能为空")
    private Integer required;

    @ApiModelProperty(value = "起搭数量")
    @NotNull(message = "起搭数量不能为空")
    private Integer leastNum;

    @ApiModelProperty(value = "状态， -1：已删除 1：正常")
    private Integer status;

    @ApiModelProperty(value = "商品图片")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @ApiModelProperty(value = "商品状态")
    private Integer prodStatus;

    @ApiModelProperty(value = "sku项列表")
    private List<ComboProdSkuVO> skuList;

}
