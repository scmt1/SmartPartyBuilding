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

/**
 * @author FrozenWatermelon
 */
@Data
public class ShopSearchVO {

    @ApiModelProperty(value = "店铺名称 搜索华为的时候，可以把华为的旗舰店搜索出来")
    private String shopName;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "店铺logo")
    private String shopLogo;

    @ApiModelProperty("店铺商品总销量")
    private Integer saleNum;

    @ApiModelProperty("店铺类型1自营店 2普通店")
    private Integer type;

    @ApiModelProperty("用户总收藏量")
    private Integer collectionNum;
}
