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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.RenovationType;
import com.yami.shop.bean.model.ShopRenovation;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.ShopRenovationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;


/**
 * 店铺装修信息
 *
 * @author lhd
 * @date 2021-01-05 11:03:38
 */
@RestController
@AllArgsConstructor
@RequestMapping("/platform/shopRenovation" )
@Api(tags = "店铺装修页面接口")
public class ShopRenovationController {


    private final ShopRenovationService shopRenovationService;

    @GetMapping("/pagePC" )
    @PreAuthorize("@pms.hasPermission('platform:shopRenovation:pagePC')")
    @ApiOperation(value = "PC端分页获取店铺装修信息", notes = "PC端分页获取店铺装修信息")
    public ServerResponseEntity<IPage<ShopRenovation>> getShopRenovationPagePC(PageParam<ShopRenovation> page, ShopRenovation shopRenovation) {
        return ServerResponseEntity.success(shopRenovationService.page(page, new LambdaQueryWrapper<ShopRenovation>()
                .eq(ShopRenovation::getShopId,Constant.PLATFORM_SHOP_ID)
                .eq(Objects.nonNull(shopRenovation.getRenovationType()), ShopRenovation::getRenovationType, shopRenovation.getRenovationType())
                .eq(ShopRenovation::getRenovationType,shopRenovation.getRenovationType())
                .orderByDesc(ShopRenovation::getHomeStatus)
                ));
    }

    @GetMapping("/pageMove" )
    @PreAuthorize("@pms.hasPermission('platform:shopRenovation:pageMove')")
    @ApiOperation(value = "移动端分页获取店铺装修信息", notes = "移动端分页获取店铺装修信息")
    public ServerResponseEntity<IPage<ShopRenovation>> getShopRenovationPageMove(PageParam<ShopRenovation> page, ShopRenovation shopRenovation) {
        return ServerResponseEntity.success(shopRenovationService.page(page, new LambdaQueryWrapper<ShopRenovation>()
                .eq(ShopRenovation::getShopId,Constant.PLATFORM_SHOP_ID)
                .eq(Objects.nonNull(shopRenovation.getRenovationType()), ShopRenovation::getRenovationType, shopRenovation.getRenovationType())
                .eq(ShopRenovation::getRenovationType,shopRenovation.getRenovationType())
                .orderByDesc(ShopRenovation::getHomeStatus)
        ));
    }

    @GetMapping("/info/{renovationId}" )
    @ApiOperation(value = "查询店铺装修信息", notes = "查询店铺装修信息")
    @ApiImplicitParam(name = "renovationId", value = "店铺装修id")
    public ServerResponseEntity<ShopRenovation> getById(@PathVariable("renovationId") Long renovationId) {
        return ServerResponseEntity.success(shopRenovationService.getById(renovationId));
    }

    @SysLog("新增店铺装修信息" )
    @PostMapping("/savePC")
    @PreAuthorize("@pms.hasPermission('platform:shopRenovation:savePC')")
    @ApiOperation(value = "PC端新增店铺装修信息", notes = "PC端新增店铺装修信息")
    public ServerResponseEntity<Long> savePC(@RequestBody @Valid ShopRenovation shopRenovation) {
        shopRenovation.setShopId(Constant.PLATFORM_SHOP_ID);
        if (Objects.isNull(shopRenovation.getRenovationType())) {
            shopRenovation.setRenovationType(RenovationType.H5.value());
        }
        shopRenovationService.save(shopRenovation);
        return ServerResponseEntity.success(shopRenovation.getRenovationId());
    }

    @SysLog("新增店铺装修信息" )
    @PostMapping("/saveMove")
    @PreAuthorize("@pms.hasPermission('platform:shopRenovation:saveMove')")
    @ApiOperation(value = "移动端新增店铺装修信息", notes = "移动端新增店铺装修信息")
    public ServerResponseEntity<Long> saveMove(@RequestBody @Valid ShopRenovation shopRenovation) {
        shopRenovation.setShopId(Constant.PLATFORM_SHOP_ID);
        if (Objects.isNull(shopRenovation.getRenovationType())) {
            shopRenovation.setRenovationType(RenovationType.H5.value());
        }
        shopRenovationService.save(shopRenovation);
        return ServerResponseEntity.success(shopRenovation.getRenovationId());
    }
}
