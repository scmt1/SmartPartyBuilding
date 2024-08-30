/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/7/28 11:18
 */
@Data
public class BrandSigningDTO {

    @ApiModelProperty("平台品牌列表")
    private List<BrandShopDTO> platformBrandList;

    @ApiModelProperty("店铺自定义品牌列表")
    private List<BrandShopDTO> customizeBrandList;

}
