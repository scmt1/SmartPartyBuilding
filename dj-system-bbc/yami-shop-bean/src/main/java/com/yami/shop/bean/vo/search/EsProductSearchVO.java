/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yami
 */
@Data
public class EsProductSearchVO {


    /**
     * 暂时没用到，便于功能扩展
     */
    @ApiModelProperty(value = "店铺信息")
    private ShopSearchVO shopInfo;

    /**
     * 暂时没用到，便于功能扩展
     */
    @ApiModelProperty(value = "品牌列表信息")
    private List<BrandSearchVO> brands;

    /**
     * 暂时没用到，便于功能扩展
     */
    @ApiModelProperty(value = "分类列表信息")
    private List<CategorySearchVO> categories;

    @ApiModelProperty(value = "spu列表信息")
    private List<ProductSearchVO> products;

}
