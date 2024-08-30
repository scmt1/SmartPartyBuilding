/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.search.multishop.controller;

import com.yami.shop.bean.param.EsProductParam;
import com.yami.shop.bean.vo.search.ProductSearchVO;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.search.common.constant.EsConstant;
import com.yami.shop.search.common.param.EsPageParam;
import com.yami.shop.search.common.service.EsProductService;
import com.yami.shop.search.common.service.SearchProductService;
import com.yami.shop.search.common.vo.EsPageVO;
import com.yami.shop.security.multishop.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @author lgh
 */
@RestController
@RequestMapping("/admin/search/prod")
@Api(tags = "商家端商品搜索接口")
public class EsProductController {

    @Autowired
    private SearchProductService searchProductService;

    @GetMapping("/page")
    @ApiOperation(value = "商品信息列表", notes = "商品信息列表")
    public ServerResponseEntity<EsPageVO<ProductSearchVO>> page(@Valid EsPageParam pageParam, EsProductParam productParam) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        productParam.setShopId(shopId);
        productParam.setFetchSource(EsConstant.SHOP_FETCH_SOURCE);
        EsPageVO<ProductSearchVO> searchPage = searchProductService.adminPage(pageParam, productParam);
        return ServerResponseEntity.success(searchPage);
    }

    @GetMapping("/renovationPage")
    @ApiOperation(value = "商品信息列表(装修商品列表)", notes = "商品信息列表(装修商品列表)")
    public ServerResponseEntity<EsPageVO<ProductSearchVO>> renovationPage(@Valid EsPageParam pageParam, EsProductParam productParam) {
        productParam.setShopId(SecurityUtils.getShopUser().getShopId());
        EsPageVO<ProductSearchVO> searchPage = searchProductService.renovationPage(pageParam, productParam);
        return ServerResponseEntity.success(searchPage);
    }

    @GetMapping("/prodExport")
    @ApiOperation(value = "商品导出", notes = "商品导出")
    @PreAuthorize("@pms.hasPermission('prod:prod:exportProd')")
    public void prodExport(HttpServletResponse response, EsProductParam productParam) {
        productParam.setShopId(SecurityUtils.getShopUser().getShopId());
        searchProductService.export(response, productParam);
    }
}
