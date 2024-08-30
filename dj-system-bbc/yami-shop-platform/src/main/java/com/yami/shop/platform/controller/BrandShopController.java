/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.controller;

import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.BrandShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lth
 * @Date 2021/8/23 11:18
 */
@RestController
@RequestMapping("/platform/brandShop")
@Api(tags="签约品牌相关接口")
public class BrandShopController {

    @Autowired
    private BrandShopService brandShopService;

    @DeleteMapping
    @ApiOperation(value = "删除签约品牌", notes = "删除签约品牌")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "shopId", value = "店铺id"),
            @ApiImplicitParam(name = "brandId", value = "品牌id")
    })
    public ServerResponseEntity<Void> delete(@RequestParam("shopId") Long shopId, @RequestParam("brandId") Long brandId) {
        brandShopService.deleteByShopIdAndBrandId(shopId, brandId);
        return ServerResponseEntity.success();
    }
}
