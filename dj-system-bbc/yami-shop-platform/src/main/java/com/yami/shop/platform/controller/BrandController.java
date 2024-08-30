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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.dto.BrandShopDTO;
import com.yami.shop.bean.dto.BrandSigningDTO;
import com.yami.shop.bean.model.Brand;
import com.yami.shop.bean.vo.BrandShopVO;
import com.yami.shop.bean.vo.BrandSigningVO;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 品牌管理
 *
 * @author lgh
 */
@RestController
@RequestMapping("/platform/brand")
@Api(tags="品牌相关接口")
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandShopService brandShopService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryBrandService categoryBrandService;

    @GetMapping("/page")
    @ApiOperation(value = "分页获取品牌信息列表", notes = "分页获取品牌信息列表")
    public ServerResponseEntity<IPage<Brand>> page( PageParam<Brand> pageDTO, Brand brandDTO) {
        brandDTO.setShopId(Constant.PLATFORM_SHOP_ID);
        IPage<Brand> brandPage = brandService.page(pageDTO, brandDTO);
        return ServerResponseEntity.success(brandPage);
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取平台品牌列表（不分页）", notes = "获取平台品牌列表（不分页）")
    public ServerResponseEntity<List<Brand>> list(Brand brand) {
        brand.setShopId(Constant.PLATFORM_SHOP_ID);
        List<Brand> brandList = brandService.listByParams(brand);
        return ServerResponseEntity.success(brandList);
    }

    @GetMapping("/info/{brandId}")
    @ApiOperation(value = "获取品牌信息", notes = "根据brandId获取品牌信息")
    @ApiImplicitParam(name = "brandId", value = "品牌id")
    public ServerResponseEntity<Brand> getByBrandId(@PathVariable Long brandId) {
        Brand brand = brandService.getInfo(brandId);
        categoryService.getPathNames(brand.getCategories());
        return ServerResponseEntity.success(brand);
    }

    @PostMapping
    @ApiOperation(value = "保存品牌信息", notes = "保存品牌信息")
    public ServerResponseEntity<Void> save(@Valid @RequestBody Brand brand) {
        brand.setShopId(Constant.PLATFORM_SHOP_ID);
        brandService.saveBrand(brand);
        brandService.removeCache(brand.getCategoryIds());
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新品牌信息", notes = "更新品牌信息")
    public ServerResponseEntity<Void> update(@Valid @RequestBody Brand brand) {
        if (Objects.isNull(brand.getCategoryIds())) {
            brand.setCategoryIds(new ArrayList<>());
        }
        List<Long> prodIds = brandService.updateBrand(brand);
        // 清除缓存
        List<Long> categoryIds = categoryBrandService.getCategoryIdBrandId(brand.getBrandId());
        categoryIds.addAll(brand.getCategoryIds());
        productService.removeProductCacheByProdIds(prodIds);
        brandService.removeCache(categoryIds);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除品牌信息", notes = "根据品牌信息id删除品牌信息")
    @ApiImplicitParam(name = "brandId", value = "品牌id")
    public ServerResponseEntity<Void> delete(@RequestParam Long brandId) {
        List<Long> prodIds = brandService.deleteById(brandId);
        productService.removeProductCacheByProdIds(prodIds);
        brandService.removeCache(categoryBrandService.getCategoryIdBrandId(brandId));
        return ServerResponseEntity.success();
    }

    @PutMapping(value = "/updateBrandStatus")
    @ApiOperation(value = "更新品牌状态（启用或禁用）", notes = "更新品牌状态（启用或禁用）")
    public ServerResponseEntity<Void> updateBrandStatus(@RequestBody Brand brand) {
        if (Objects.isNull(brand.getStatus())) {
            throw new YamiShopBindException("状态不能为空");
        }
        if (Objects.isNull(brand.getBrandId())) {
            throw new YamiShopBindException("品牌id不能为空");
        }
        List<Long> prodIds = brandService.updateBrandStatus(brand);
        productService.removeProductCacheByProdIds(prodIds);
        // 清楚缓存
        List<Long> categoryIds = categoryBrandService.getCategoryIdBrandId(brand.getBrandId());
        // 获取当前节点所有父节点的分类ids，以及当前分类节点的父级节点的父级几点的分类ids
        List<Long> parentCategoryIds = categoryService.getParentIdsByCategoryId(categoryIds);
        if (CollUtil.isNotEmpty(parentCategoryIds)) {
            categoryIds.addAll(parentCategoryIds);
        }
        brandService.removeCache(CollUtil.isNotEmpty(categoryIds) ? categoryIds : new ArrayList<>());
        return ServerResponseEntity.success();
    }

    @GetMapping("/listSigningByShopId")
    @ApiOperation(value = "根据店铺id获取签约的品牌列表", notes = "根据店铺id获取签约的品牌列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(name = "status", value = "状态 1:启用, 0:禁用")
    })
    public ServerResponseEntity<BrandSigningVO> listSigningByShopId(@RequestParam(value = "shopId") Long shopId, @RequestParam(value = "status", required = false) Integer status) {
        BrandSigningVO brandSigningVO = brandShopService.listSigningByShopId(shopId, status);
        return ServerResponseEntity.success(brandSigningVO);
    }

    @GetMapping("/pageSigningByShopId")
    @ApiOperation(value = "根据店铺id分页获取签约的品牌列表", notes = "根据店铺id分页获取签约的品牌列表")
    public ServerResponseEntity<IPage<BrandShopVO>> pageSigningByShopId(PageParam<BrandShopVO> page, BrandShopDTO brandShop) {
        IPage<BrandShopVO> brandShopPage = brandShopService.pageSigningByShopId(page, brandShop);
        return ServerResponseEntity.success(brandShopPage);
    }

    @PutMapping("/signing")
    @ApiOperation(value = "根据店铺id更新店铺下的签约品牌", notes = "根据店铺id更新店铺下的签约品牌")
    @ApiImplicitParam(name = "shopId", value = "店铺id")
    public ServerResponseEntity<Void> signingBrands(@RequestBody BrandSigningDTO brandSigningDTO, @RequestParam(value = "shopId") Long shopId) {
        brandShopService.signingBrands(brandSigningDTO, shopId, true);
        return ServerResponseEntity.success();
    }

}
