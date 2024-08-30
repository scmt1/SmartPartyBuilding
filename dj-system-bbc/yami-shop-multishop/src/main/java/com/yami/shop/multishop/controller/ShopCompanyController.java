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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.enums.AuditStatus;
import com.yami.shop.bean.model.ShopCompany;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ShopCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @author lth
 */
@RestController
@AllArgsConstructor
@RequestMapping("/shop/shopCompany")
@Api(tags="店铺工商信息接口")
public class ShopCompanyController {
    private final ShopCompanyService shopCompanyService;

    @PostMapping
    @ApiOperation(value = "新增店铺工商信息", notes = "新增店铺工商信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody ShopCompany shopCompany) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        shopCompany.setShopId(shopId);
        shopCompany.setStatus(AuditStatus.SUCCESSAUDIT.value());
        shopCompanyService.saveInfo(shopCompany);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新店铺工商信息", notes = "更新店铺工商信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody ShopCompany shopCompany) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        shopCompany.setShopId(shopId);
        shopCompany.setStatus(AuditStatus.SUCCESSAUDIT.value());
        shopCompanyService.updateByShopId(shopCompany);
        return ServerResponseEntity.success();
    }

    @PostMapping("/storage")
    @ApiOperation(value = "存储店铺工商信息", notes = "存储店铺工商信息,已存在则更新，不存在则新增")
    @PreAuthorize("@pms.hasPermission('shop:shopCompany:save')")
    public ServerResponseEntity<Void> storage(@Valid @RequestBody ShopCompany shopCompany) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        shopCompany.setShopId(shopId);
        shopCompany.setStatus(AuditStatus.SUCCESSAUDIT.value());
        if (shopCompanyService.count(Wrappers.lambdaQuery(ShopCompany.class).eq(ShopCompany::getShopId, shopId)) > 0) {
            shopCompanyService.updateByShopId(shopCompany);
        } else {
            shopCompanyService.saveInfo(shopCompany);
        }
        return ServerResponseEntity.success();
    }

    @GetMapping
    @ApiOperation(value = "获取店铺工商信息", notes = "获取店铺工商信息")
    public ServerResponseEntity<ShopCompany> getShopCompanyByShopId() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        ShopCompany shopCompany = shopCompanyService.getOne(Wrappers.lambdaQuery(ShopCompany.class).eq(ShopCompany::getShopId, shopId).eq(ShopCompany::getStatus, AuditStatus.SUCCESSAUDIT.value()));
        if (Objects.isNull(shopCompany)) {
            shopCompany = new ShopCompany();
        }
        return ServerResponseEntity.success(shopCompany);
    }
}
