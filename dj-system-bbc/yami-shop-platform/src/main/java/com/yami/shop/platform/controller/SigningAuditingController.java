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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.dto.BrandShopDTO;
import com.yami.shop.bean.model.*;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.platform.util.SecurityUtils;
import com.yami.shop.service.SigningAuditingService;
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
 * @Author lth
 * @Date 2021/8/19 13:51
 */
@RestController
@RequestMapping("/platform/signingAuditing")
@Api(tags="签约信息相关接口")
public class SigningAuditingController {

    @Autowired
    private SigningAuditingService signingAuditingService;

    @GetMapping("/listApplySigningCategory")
    @ApiOperation(value = "获取可以签约的平台分类列表（已经签约的平台分类不会返回）", notes = "获取可以签约的平台分类列表（已经签约的平台分类不会返回）")
    @ApiImplicitParam(name = "shopId", value = "店铺id")
    public ServerResponseEntity<List<Category>> listApplySigningCategory(@RequestParam("shopId") Long shopId) {
        List<Category> categoryList = signingAuditingService.listApplySigningCategory(shopId, I18nMessage.getLang());
        return ServerResponseEntity.success(categoryList);
    }

    @GetMapping("/listApplySigningBrand")
    @ApiOperation(value = "获取可以签约的平台品牌列表（已经签约的平台品牌不会返回)", notes = "获取可以签约的平台品牌列表（已经签约的平台品牌不会返回)")
    public ServerResponseEntity<List<Brand>> listApplySigningBrand(Brand brand) {
        Long shopId = brand.getShopId();
        if (Objects.isNull(shopId)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        brand.setShopId(null);
        List<Brand> brandList = signingAuditingService.listApplySigningBrand(shopId, brand, I18nMessage.getLang());
        return ServerResponseEntity.success(brandList);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页获取待审核的签约信息", notes = "分页获取待审核的签约信息")
    public ServerResponseEntity<IPage<SigningAuditing>> page(PageParam<SigningAuditing> page, SigningAuditing signingAuditing) {
        IPage<SigningAuditing> signingAuditingPage = signingAuditingService.pageSigningAuditing(page, signingAuditing);
        return ServerResponseEntity.success(signingAuditingPage);
    }

    @PutMapping("/audit")
    @ApiOperation(value = "审核签约信息", notes = "审核签约信息")
    public ServerResponseEntity<Void> audit(@Valid @RequestBody SigningAuditing signingAuditing) {
        signingAuditing.setAuditorId(SecurityUtils.getSysUser().getUserId());
        signingAuditingService.audit(signingAuditing);
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateCategoryRate")
    @ApiOperation(value = "更新店铺签约分类自定义扣率", notes = "更新店铺签约分类自定义扣率")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "categoryShopId", value = "店铺签约分类id"),
            @ApiImplicitParam(name = "rate", value = "扣率")
    })
    public ServerResponseEntity<Void> updateCategoryRate(@RequestParam("categoryShopId") Long categoryShopId, @RequestParam("rate") Double rate) {
        signingAuditingService.updateCategoryRate(categoryShopId, rate);
        return ServerResponseEntity.success();
    }

    @PostMapping("/addSigningCategory")
    @ApiOperation(value = "增加签约分类", notes = "增加签约分类")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "categoryShopList", value = "店铺签约分类id列表"),
            @ApiImplicitParam(name = "shopId", value = "店铺id")
    })
    public ServerResponseEntity<Void> addSigningCategory(@RequestBody List<CategoryShop> categoryShopList, @RequestParam("shopId") Long shopId) {
        signingAuditingService.addSigningCategory(categoryShopList, shopId);
        return ServerResponseEntity.success();
    }

    @PostMapping("/addSigningBrand")
    @ApiOperation(value = "增加签约品牌", notes = "增加签约品牌")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "brandShopList", value = "店铺签约品牌id列表"),
            @ApiImplicitParam(name = "shopId", value = "店铺id")
    })
    public ServerResponseEntity<Void> addSigningBrand(@RequestBody List<BrandShopDTO> brandShopList, @RequestParam("shopId") Long shopId) {
        signingAuditingService.addSigningBrand(brandShopList, shopId);
        return ServerResponseEntity.success();
    }
}
