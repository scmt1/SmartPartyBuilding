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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.Sku;
import com.yami.shop.bean.param.ProductParam;
import com.yami.shop.common.enums.StatusEnum;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.SkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author person
 */
@RestController
@RequestMapping("/sku")
@Api(tags = "sku规格接口")
@AllArgsConstructor
public class SkuController {

    private final SkuService skuService;

    @GetMapping("/getAllSkuList")
    @ApiOperation(value = "通过prodId获取全部的规格列表", notes = "通过prodId获取全部的规格列表")
    @ApiImplicitParam(name = "prodId", value = "商品id", dataType = "Long")
    public ServerResponseEntity<List<Sku>> getSkuListByProdId(Long prodId) {
        List<Sku> skus = skuService.listSkuAndSkuStock(prodId, I18nMessage.getDbLang());
        return ServerResponseEntity.success(skus);
    }

    @GetMapping("/getEnableSkuList")
    @ApiOperation(value = "通过prodId获取启用的规格列表", notes = "通过prodId获取启用的规格列表")
    @ApiImplicitParam(name = "prodId", value = "商品id", dataType = "Long")
    public ServerResponseEntity<List<Sku>> getEnableSkuList(Long prodId) {
        List<Sku> skus = skuService.listPutOnSkuAndSkuStock(prodId, I18nMessage.getDbLang());
        skus = skus.stream().filter(item -> Objects.equals(item.getStatus(), StatusEnum.ENABLE.value())).collect(Collectors.toList());
        return ServerResponseEntity.success(skus);
    }

    @GetMapping("/pageSku")
    @ApiOperation(value = "分页查询sku信息", notes = "分页查询sku信息")
    public ServerResponseEntity<IPage<Sku>> pageSku(PageParam<Sku> page, ProductParam product) {
        product.setLang(I18nMessage.getLang());
        product.setShopId(SecurityUtils.getShopUser().getShopId());
        IPage<Sku> skuPage = skuService.pageSku(page, product);
        return ServerResponseEntity.success(skuPage);
    }

    @GetMapping("/exportSkuList")
    @ApiOperation(value = "导出sku信息", notes = "导出sku信息")
    public void exportSkuList(HttpServletResponse response, ProductParam product) {
        product.setShopId(SecurityUtils.getShopUser().getShopId());
        skuService.exportSkuList(product, response);
    }
}
