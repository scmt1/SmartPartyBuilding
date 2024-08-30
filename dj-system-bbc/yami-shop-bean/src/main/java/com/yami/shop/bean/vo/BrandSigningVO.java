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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/8/9 13:06
 */
@Data
public class BrandSigningVO {
    @ApiModelProperty(value = "签约的平台品牌列表")
    private List<BrandShopVO> platformBrandList;

    @ApiModelProperty(value = "签约的自定义品牌列表")
    private List<BrandShopVO> customizeBrandList;
}
