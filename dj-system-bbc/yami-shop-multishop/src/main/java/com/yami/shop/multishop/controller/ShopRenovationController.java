/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.multishop.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.RenovationType;
import com.yami.shop.bean.model.ShopRenovation;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ShopRenovationService;
import io.swagger.annotations.Api;
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
@RequestMapping("/shop/shopRenovation" )
@Api(tags = "店铺装修页面接口")
public class ShopRenovationController {

    private final ShopRenovationService shopRenovationService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param shopRenovation 店铺装修信息
     * @return 分页数据
     */
    @GetMapping("/pagePC" )
    @PreAuthorize("@pms.hasPermission('shop:shopRenovation:pagePC')")
    @ApiOperation(value = "PC端分页查询店铺装修页面信息", notes = "PC端分页查询店铺装修页面信息")
    public ServerResponseEntity<IPage<ShopRenovation>> getShopRenovationPagePC(PageParam<ShopRenovation> page, ShopRenovation shopRenovation) {
        return ServerResponseEntity.success(shopRenovationService.page(page, new LambdaQueryWrapper<ShopRenovation>()
                .eq(ShopRenovation::getShopId,SecurityUtils.getShopUser().getShopId())
                .eq(Objects.nonNull(shopRenovation.getRenovationType()), ShopRenovation::getRenovationType, shopRenovation.getRenovationType())
                .orderByDesc(ShopRenovation::getHomeStatus)
        ));
    }

    /**
     * 分页查询
     * @param page 分页对象
     * @param shopRenovation 店铺装修信息
     * @return 分页数据
     */
    @GetMapping("/pageMove" )
    @PreAuthorize("@pms.hasPermission('shop:shopRenovation:pageMove')")
    @ApiOperation(value = "移动端分页查询店铺装修页面信息", notes = "移动端分页查询店铺装修页面信息")
    public ServerResponseEntity<IPage<ShopRenovation>> getShopRenovationPageMove(PageParam<ShopRenovation> page, ShopRenovation shopRenovation) {
        return ServerResponseEntity.success(shopRenovationService.page(page, new LambdaQueryWrapper<ShopRenovation>()
                .eq(ShopRenovation::getShopId,SecurityUtils.getShopUser().getShopId())
                .eq(Objects.nonNull(shopRenovation.getRenovationType()), ShopRenovation::getRenovationType, shopRenovation.getRenovationType())
                .orderByDesc(ShopRenovation::getHomeStatus)
        ));
    }

    /**
     * 通过id查询店铺装修信息
     * @param renovationId id
     * @return 单个数据
     */
    @GetMapping("/info/{renovationId}" )
    @ApiOperation(value = "通过id查询店铺装修页面信息", notes = "通过id查询店铺装修页面信息")
    public ServerResponseEntity<ShopRenovation> getById(@PathVariable("renovationId") Long renovationId) {
        ShopRenovation shopRenovation = shopRenovationService.getById(renovationId);
        if(!Objects.equals(shopRenovation.getShopId() ,SecurityUtils.getShopUser().getShopId())){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(shopRenovation);
    }



    /**
     * 新增店铺装修信息
     * @param shopRenovation 店铺装修信息
     * @return 是否新增成功
     */
    @PostMapping("/savePC")
    @PreAuthorize("@pms.hasPermission('shop:shopRenovation:savePC')")
    @ApiOperation(value = "PC端新增店铺装修页面信息", notes = "PC端新增店铺装修页面信息")
    public ServerResponseEntity<Long> savePC(@RequestBody @Valid ShopRenovation shopRenovation) {
        shopRenovation.setShopId(SecurityUtils.getShopUser().getShopId());
        if (Objects.isNull(shopRenovation.getRenovationType())) {
            shopRenovation.setRenovationType(RenovationType.PC.value());
        }
        shopRenovationService.save(shopRenovation);
        return ServerResponseEntity.success(shopRenovation.getRenovationId());
    }

    /**
     * 新增店铺装修信息
     * @param shopRenovation 店铺装修信息
     * @return 是否新增成功
     */
    @PostMapping("/saveMove")
    @PreAuthorize("@pms.hasPermission('shop:shopRenovation:saveMove')")
    @ApiOperation(value = "移动端新增店铺装修页面信息", notes = "移动端新增店铺装修页面信息")
    public ServerResponseEntity<Long> saveMove(@RequestBody @Valid ShopRenovation shopRenovation) {
        shopRenovation.setShopId(SecurityUtils.getShopUser().getShopId());
        if (Objects.isNull(shopRenovation.getRenovationType())) {
            shopRenovation.setRenovationType(RenovationType.H5.value());
        }
        shopRenovationService.save(shopRenovation);
        return ServerResponseEntity.success(shopRenovation.getRenovationId());
    }

}
