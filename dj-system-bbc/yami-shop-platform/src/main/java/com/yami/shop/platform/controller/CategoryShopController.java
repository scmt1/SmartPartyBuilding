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
import com.yami.shop.service.CategoryShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author lth
 * @Date 2021/8/23 9:44
 */
@RestController
@RequestMapping("/platform/categoryShop")
@Api(tags="签约分类相关接口")
public class CategoryShopController {

    @Autowired
    private CategoryShopService categoryShopService;

    @DeleteMapping
    @ApiOperation(value = "删除签约分类", notes = "删除签约分类")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "shopId", value = "店铺id"),
            @ApiImplicitParam(name = "categoryId", value = "分类id")
    })
    public ServerResponseEntity<Void> delete(@RequestParam("shopId") Long shopId, @RequestParam("categoryId") Long categoryId) {
        categoryShopService.delete(shopId, categoryId);
        return ServerResponseEntity.success();
    }

    @PutMapping("updateRate")
    @ApiOperation(value = "更新自定义扣率", notes = "更新自定义扣率")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "shopId", value = "店铺id"),
            @ApiImplicitParam(name = "rate", value = "扣率")
    })
    public ServerResponseEntity<Void> updateRate(@RequestParam("shopId") Long shopId, @RequestParam("categoryId") Long categoryId,
                                           @RequestParam(value = "rate", required = false) Double rate) {
        categoryShopService.updateRate(shopId, categoryId, rate);
        return ServerResponseEntity.success();
    }
}
