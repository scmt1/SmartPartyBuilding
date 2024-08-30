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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.dto.BrandShopDTO;
import com.yami.shop.bean.dto.BrandSigningDTO;
import com.yami.shop.bean.enums.SigningStatus;
import com.yami.shop.bean.model.*;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.common.util.AuthUserContext;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ProductService;
import com.yami.shop.service.SigningAuditingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/8/19 10:19
 */
@RestController
@RequestMapping("/shop/signingAuditing")
@Api(tags="签约信息接口")
public class SigningAuditingController {

    @Autowired
    private SigningAuditingService signingAuditingService;

    @Autowired
    private ProductService productService;

    @GetMapping("/listApplySigningCategory")
    @ApiOperation(value = "获取可以签约的平台分类列表（已经签约的平台分类不会返回）", notes = "获取可以签约的平台分类列表（已经签约的平台分类不会返回）")
    public ServerResponseEntity<List<Category>> listApplySigningCategory() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        List<Category> categoryList = signingAuditingService.listApplySigningCategory(shopId, I18nMessage.getLang());
        return ServerResponseEntity.success(categoryList);
    }

    @GetMapping("/listApplySigningBrand")
    @ApiOperation(value = "获取可以签约的平台品牌列表（已经签约的平台品牌不会返回)", notes = "获取可以签约的平台品牌列表（已经签约的平台品牌不会返回)")
    public ServerResponseEntity<List<Brand>> listApplySigningBrand(Brand brand) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        List<Brand> brandList = signingAuditingService.listApplySigningBrand(shopId, brand, I18nMessage.getLang());
        return ServerResponseEntity.success(brandList);
    }

    @PostMapping("/addSigningCategory")
    @ApiOperation(value = "增加签约分类", notes = "增加签约分类")
    @ApiImplicitParams(value = @ApiImplicitParam(name = "categoryShopList", value = "店铺签约分类id列表"))
    public ServerResponseEntity<Void> addSigningCategory(@RequestBody List<CategoryShop> categoryShopList) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        signingAuditingService.addSigningCategory(categoryShopList, shopId);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/deleteSigningCategory")
    @ApiOperation(value = "根据分类id删除签约分类", notes = "根据分类id删除签约分类")
    @ApiImplicitParam(name = "categoryId", value = "分类Id")
    public ServerResponseEntity<Void> deleteSigningCategory(@RequestParam("categoryId") Long categoryId) {
        int categoryProdCount = productService.count(new LambdaQueryWrapper<Product>().eq(Product::getShopCategoryId, categoryId).eq(Product::getStatus, 1));
        if (categoryProdCount > 0){
            // 该分类下还有商品，请先删除该分类下的商品
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.category.delete.check"));
        }

        Long shopId = SecurityUtils.getShopUser().getShopId();
        signingAuditingService.deleteSigningCategory(shopId, categoryId);
        return ServerResponseEntity.success();
    }
    @PostMapping("/addSigningBrand")
    @ApiOperation(value = "增加签约品牌", notes = "增加签约品牌")
    @ApiImplicitParams(value = @ApiImplicitParam(name = "brandSigningDTO", value = "店铺签约品牌id列表"))
    public ServerResponseEntity<Void> addSigningBrand(@RequestBody BrandSigningDTO brandSigningDTO) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        signingAuditingService.addSigningBrand(brandSigningDTO.getPlatformBrandList(), shopId);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/deleteSigningBrand")
    @ApiOperation(value = "根据签约品牌Id删除签约品牌", notes = "根据签约品牌Id删除签约品牌")
    @ApiImplicitParam(name = "brandShopId", value = "签约品牌Id")
    public ServerResponseEntity<Void> deleteSigningBrand(@RequestParam("brandShopId") Long brandShopId) {
        signingAuditingService.deleteSigningBrand(brandShopId);
        return ServerResponseEntity.success();
    }

    @PostMapping("/applyCategory")
    @ApiOperation(value = "申请签约分类", notes = "申请签约分类")
    @PreAuthorize("@pms.hasPermission('shop:shopCategory:apply')")
    public ServerResponseEntity<Void> applyCategory(@RequestBody List<CategoryShop> categoryShopList) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        Long userId = SecurityUtils.getShopUser().getEmployeeId();
        signingAuditingService.applyCategory(shopId, userId, categoryShopList);
        return ServerResponseEntity.success();
    }

    @PostMapping("/applyBrand")
    @ApiOperation(value = "申请签约品牌", notes = "申请签约品牌")
    @PreAuthorize("@pms.hasPermission('shop:shopBrand:apply')")
    public ServerResponseEntity<Void> applyBrand(@RequestBody BrandSigningDTO brandSigningDTO) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        Long userId = SecurityUtils.getShopUser().getEmployeeId();
        signingAuditingService.applyBrand(shopId, userId, brandSigningDTO.getPlatformBrandList());
        return ServerResponseEntity.success();
    }

    @PutMapping("/revoke")
    @ApiOperation(value = "撤销申请", notes = "撤销申请")
    @ApiImplicitParam(name = "type", value = "类型：1：分类 2：品牌")
    public ServerResponseEntity<Void> revoke(@RequestBody Integer type) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        signingAuditingService.revoke(shopId, type);
        return ServerResponseEntity.success();
    }

    @GetMapping("/auditInfo")
    @ApiOperation(value = "查看签约审核信息", notes = "查看签约审核信息")
    @ApiImplicitParam(name = "type", value = "类型：1：分类 2：品牌")
    public ServerResponseEntity<SigningAuditing> auditInfo(@RequestParam(value = "type") Integer type) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        SigningAuditing signingAuditing = signingAuditingService.getOne(Wrappers.lambdaQuery(SigningAuditing.class)
                .eq(SigningAuditing::getShopId, shopId)
                .eq(SigningAuditing::getType, type)
                .ne(SigningAuditing::getStatus, SigningStatus.SUCCESS.value())
        );
        return ServerResponseEntity.success(signingAuditing);
    }
}
