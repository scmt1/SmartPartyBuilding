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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.enums.AuditStatus;
import com.yami.shop.bean.model.ShopCompany;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.ShopCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @Author lth
 * @Date 2021/8/4 14:44
 */
@RestController
@AllArgsConstructor
@RequestMapping("/platform/shopCompany")
@Api(tags = "商家工商信息")
public class ShopCompanyController {

    private final ShopCompanyService shopCompanyService;

    @GetMapping
    @ApiOperation(value = "根据店铺id获取店铺工商信息", notes = "根据店铺id获取店铺工商信息")
    @ApiImplicitParam(name = "shopId", value = "店铺id")
    public ServerResponseEntity<ShopCompany> getShopCompanyByShopId(@RequestParam("shopId") Long shopId) {
        ShopCompany shopCompany = shopCompanyService.getOne(Wrappers.lambdaQuery(ShopCompany.class).eq(ShopCompany::getShopId, shopId).eq(ShopCompany::getStatus, AuditStatus.SUCCESSAUDIT.value()));
        if (Objects.isNull(shopCompany)) {
            shopCompany = new ShopCompany();
        }
        return ServerResponseEntity.success(shopCompany);
    }

    @PutMapping
    @ApiOperation(value = "更新店铺工商信息", notes = "更新店铺工商信息")
    public ServerResponseEntity<Void> editShopCompany(@RequestBody @Valid ShopCompany shopCompany) {
        shopCompanyService.updateByShopId(shopCompany);
        return ServerResponseEntity.success();
    }

    @GetMapping("/checkCreditCode")
    @ApiOperation(value = "检查统一信用码是否已存在", notes = "检查统一信用码是否已存在")
    @ApiImplicitParam(name = "creditCode", value = "信用码")
    public ServerResponseEntity<Boolean> checkCreditCode(@RequestParam(value = "creditCode") String creditCode) {
         int count = shopCompanyService.count(Wrappers.lambdaQuery(ShopCompany.class)
                .eq(ShopCompany::getCreditCode, creditCode).eq(ShopCompany::getStatus, AuditStatus.SUCCESSAUDIT.value())
        );
        return ServerResponseEntity.success(count > 0);
    }
}
