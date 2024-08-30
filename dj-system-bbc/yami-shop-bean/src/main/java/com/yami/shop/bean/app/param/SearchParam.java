/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author LGH
 */
@Data
public class SearchParam {


    @ApiModelProperty(value = "商品名")
    private String prodName;

    @ApiModelProperty(value = "排序(0 默认排序 1销量排序 2价格排序)")
    private Integer sort;

    @ApiModelProperty(value = "排序(0升序 1降序)")
    private Integer orderBy;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "平台一级分类id")
    private Long primaryCategoryId;

    @ApiModelProperty(value = "平台二级分类id")
    private Long secondaryCategoryId;

    @ApiModelProperty(value = "平台三级分类id")
    private Long categoryId;

    @ApiModelProperty(value = "商品ids")
    private List<Long> prodIds;

    @ApiModelProperty(value = "店铺分类id")
    private Long shopCategoryId;

    @ApiModelProperty(value = "商品类型(0普通商品 1拼团 2秒杀 3积分)")
    private Integer prodType;

    @ApiModelProperty(value = "查询所有的商品类型")
    private Boolean isAllProdType;

    private Integer lang;

}
