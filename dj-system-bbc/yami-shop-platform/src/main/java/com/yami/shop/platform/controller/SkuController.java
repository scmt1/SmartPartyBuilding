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

import com.yami.shop.bean.model.Sku;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.SkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yami
 */
@RestController
@RequestMapping("/platform/sku")
@AllArgsConstructor
@Api(tags = "商品sku")
public class SkuController {

    private final SkuService skuService;

    @GetMapping("/getAllSkuList")
    @PreAuthorize("@pms.hasPermission('plateform:sku:list')")
    @ApiOperation(value = "获取指定商品sku列表", notes = "获取指定商品sku列表")
    @ApiImplicitParam(name = "prodId", value = "商品id")
    public ServerResponseEntity<List<Sku>> getSkuListByProdId(Long prodId) {
        List<Sku> skus = skuService.listSkuAndSkuStock(prodId, I18nMessage.getDbLang());
        return ServerResponseEntity.success(skus);
    }
}
