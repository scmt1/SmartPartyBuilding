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

import com.yami.shop.bean.model.ShopRenovation;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ShopRenovationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@RequestMapping("/shop/shopRenovation" )
@ConditionalOnProperty(prefix = "yami", name = "expose.operation.auth", havingValue = "true", matchIfMissing = true)
@Api(tags = "店铺页面-修改、删除、设为主页")
public class ShopRenovationUpDelController {

    @Autowired
    private ShopRenovationService shopRenovationService;

    /**
     * 修改店铺装修信息
     * @param shopRenovation 店铺装修信息
     * @return 是否修改成功
     */
    @PutMapping("/updatePC")
    @PreAuthorize("@pms.hasPermission('shop:shopRenovation:updatePC')")
    @ApiOperation(value = "PC端修改店铺装修页面信息", notes = "PC端修改店铺装修页面信息")
    public ServerResponseEntity<Boolean> updatePCById(@RequestBody @Valid ShopRenovation shopRenovation) {
        ShopRenovation shopRenovationDb = shopRenovationService.getById(shopRenovation.getRenovationId());
        if (Objects.isNull(shopRenovationDb)) {
            // 查找不到该装修信息
            throw new YamiShopBindException("yami.renovation.not.exist");
        }
        if(!Objects.equals(shopRenovationDb.getShopId() , SecurityUtils.getShopUser().getShopId())){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        shopRenovationService.updateById(shopRenovation);
        shopRenovationService.removeCache(shopRenovationDb.getShopId(), shopRenovationDb.getRenovationType(), shopRenovationDb.getRenovationId());
        return ServerResponseEntity.success();
    }

    /**
     * 修改店铺装修信息
     * @param shopRenovation 店铺装修信息
     * @return 是否修改成功
     */
    @PutMapping("/updateMove")
    @PreAuthorize("@pms.hasPermission('shop:shopRenovation:updateMove')")
    @ApiOperation(value = "移动端修改店铺装修页面信息", notes = "移动端修改店铺装修页面信息")
    public ServerResponseEntity<Boolean> updateMoveById(@RequestBody @Valid ShopRenovation shopRenovation) {
        ShopRenovation shopRenovationDb = shopRenovationService.getById(shopRenovation.getRenovationId());
        if (Objects.isNull(shopRenovationDb)) {
            // 查找不到该装修信息
            throw new YamiShopBindException("yami.renovation.not.exist");
        }
        if(!Objects.equals(shopRenovationDb.getShopId() , SecurityUtils.getShopUser().getShopId())){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        shopRenovationService.updateById(shopRenovation);
        shopRenovationService.removeCache(shopRenovationDb.getShopId(), shopRenovationDb.getRenovationType(), shopRenovationDb.getRenovationId());
        return ServerResponseEntity.success();
    }


    /**
     * 通过id删除店铺装修信息
     * @param renovationId id
     * @return 是否删除成功
     */
    @SysLog("删除店铺装修信息" )
    @DeleteMapping("/deletePC/{renovationId}" )
    @PreAuthorize("@pms.hasPermission('shop:shopRenovation:deletePC')")
    @ApiOperation(value = "PC端通过id删除店铺装修页面信息", notes = "PC端通过id删除店铺装修页面信息")
    public ServerResponseEntity<Boolean> removePCById(@PathVariable Long renovationId) {
        ShopRenovation shopRenovation = shopRenovationService.getById(renovationId);
        if(!Objects.equals(shopRenovation.getShopId() ,SecurityUtils.getShopUser().getShopId())){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        shopRenovationService.removeCache(shopRenovation.getShopId(), shopRenovation.getRenovationType(), shopRenovation.getRenovationId());
        return ServerResponseEntity.success(shopRenovationService.removeById(renovationId));
    }

    /**
     * 通过id删除店铺装修信息
     * @param renovationId id
     * @return 是否删除成功
     */
    @SysLog("删除店铺装修信息" )
    @DeleteMapping("/deleteMove/{renovationId}" )
    @PreAuthorize("@pms.hasPermission('shop:shopRenovation:deleteMove')")
    @ApiOperation(value = "移动端通过id删除店铺装修页面信息", notes = "移动端通过id删除店铺装修页面信息")
    public ServerResponseEntity<Boolean> removeMoveById(@PathVariable Long renovationId) {
        ShopRenovation shopRenovation = shopRenovationService.getById(renovationId);
        if(!Objects.equals(shopRenovation.getShopId() ,SecurityUtils.getShopUser().getShopId())){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        shopRenovationService.removeCache(shopRenovation.getShopId(), shopRenovation.getRenovationType(), shopRenovation.getRenovationId());
        return ServerResponseEntity.success(shopRenovationService.removeById(renovationId));
    }


    /**
     * 修改主页标识
     * @param id id
     */
    @PutMapping("/updateHomePagePC/{id}" )
    @PreAuthorize("@pms.hasPermission('shop:shopRenovation:updateHomePC')")
    @ApiOperation(value = "PC端修改主页标识", notes = "Pc端修改主页标识")
    public ServerResponseEntity<Void> updateHomePagePC(@PathVariable Long id) {
        ShopRenovation shopRenovation = shopRenovationService.getById(id);
        if(!Objects.equals(shopRenovation.getShopId() ,SecurityUtils.getShopUser().getShopId())) {
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        shopRenovationService.updateToHomePage(shopRenovation);
        shopRenovationService.removeCache(shopRenovation.getShopId(), shopRenovation.getRenovationType(), shopRenovation.getRenovationId());
        return ServerResponseEntity.success();
    }

    /**
     * 修改主页标识
     * @param id id
     */
    @PutMapping("/updateHomePageMove/{id}" )
    @PreAuthorize("@pms.hasPermission('shop:shopRenovation:updateHomeMove')")
    @ApiOperation(value = "移动端修改主页标识", notes = "移动端修改主页标识")
    public ServerResponseEntity<Void> updateHomePageMove(@PathVariable Long id) {
        ShopRenovation shopRenovation = shopRenovationService.getById(id);
        if(!Objects.equals(shopRenovation.getShopId() ,SecurityUtils.getShopUser().getShopId())) {
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        shopRenovationService.updateToHomePage(shopRenovation);
        shopRenovationService.removeCache(shopRenovation.getShopId(), shopRenovation.getRenovationType(), shopRenovation.getRenovationId());
        return ServerResponseEntity.success();
    }
}
