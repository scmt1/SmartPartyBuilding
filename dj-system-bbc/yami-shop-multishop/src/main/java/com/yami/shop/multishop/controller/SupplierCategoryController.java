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
import com.yami.shop.bean.model.SupplierCategory;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.SupplierCategoryService;
import com.yami.shop.service.SupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


/**
 * @author Citrus
 * @date 2021-09-07 10:12:32
 */
@RestController
@RequestMapping("/supplier/supplierCategory")
@Api(tags = "供应商分类接口")
public class SupplierCategoryController {

    @Autowired
    private SupplierCategoryService supplierCategoryService;

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/page")
    @ApiModelProperty(value = "获取供应商分类列表（分页）", notes = "获取供应商分类列表（分页）")
    public ServerResponseEntity<IPage<SupplierCategory>> getSupplierCategoryPage(PageParam<SupplierCategory> page, SupplierCategory supplierCategory) {
        return ServerResponseEntity.success(supplierCategoryService.page(page, new LambdaQueryWrapper<SupplierCategory>()
                .eq(SupplierCategory::getShopId, SecurityUtils.getShopUser().getShopId())));
    }

    @GetMapping("/list")
    @ApiModelProperty(value = "获取供应商分类列表（不分页）", notes = "获取供应商分类列表（不分页）")
    public ServerResponseEntity<List<SupplierCategory>> listSupplierCategory(@RequestParam("isAll") Integer isAll) {
        List<SupplierCategory> categoryList = supplierCategoryService.listSupplierCategory(SecurityUtils.getShopUser().getShopId(), isAll);
        return ServerResponseEntity.success(categoryList);
    }

    @GetMapping("/info/{supplierCategoryId}")
    @ApiModelProperty(value = "根据id查询供应商分类", notes = "根据id查询供应商分类")
    public ServerResponseEntity<SupplierCategory> getById(@PathVariable("supplierCategoryId") Long supplierCategoryId) {
        SupplierCategory supplierCategory = supplierCategoryService.getById(supplierCategoryId);
        if (!Objects.equals(SecurityUtils.getShopUser().getShopId(), supplierCategory.getShopId())) {
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(supplierCategory);
    }

    @PostMapping
    @PreAuthorize("@pms.hasPermission('multishop:supplierCategory:save')")
    @ApiOperation(value = "新增供应商分类", notes = "新增供应商分类")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid SupplierCategory supplierCategory) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        this.checkName(shopId, supplierCategory);
        supplierCategory.setShopId(shopId);
        boolean save = supplierCategoryService.save(supplierCategory);
        supplierCategoryService.removeSupplierCategoryCache(shopId, 1);
        supplierCategoryService.removeSupplierCategoryCache(shopId, 0);
        return ServerResponseEntity.success(save);
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('multishop:supplierCategory:update')")
    @ApiOperation(value = "修改供应商分类", notes = "修改供应商分类")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid SupplierCategory supplierCategory) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        this.checkName(shopId, supplierCategory);
        supplierCategory.setShopId(shopId);
        boolean update = supplierCategoryService.updateById(supplierCategory);
        supplierCategoryService.removeSupplierCategoryCache(shopId, 1);
        supplierCategoryService.removeSupplierCategoryCache(shopId, 0);
        return ServerResponseEntity.success(update);
    }

    @PutMapping("/changeCategoryStatus")
    @ApiModelProperty(value = "改变分类状态", notes = "改变分类状态")
    @PreAuthorize("@pms.hasPermission('multishop:supplierCategory:change')")
    public ServerResponseEntity<Void> disableCategory(@RequestParam("supplierCategoryId") Long supplierCategoryId) {
        supplierCategoryService.changeCategory(supplierCategoryId);
        supplierService.updateSupplierByCategoryId(supplierCategoryId);
        supplierCategoryService.removeSupplierCategoryCache(SecurityUtils.getShopUser().getShopId(), 1);
        supplierCategoryService.removeSupplierCategoryCache(SecurityUtils.getShopUser().getShopId(), 0);
        return ServerResponseEntity.success();
    }

    private void checkName(Long shopId, SupplierCategory supplierCategory) {
        List<SupplierCategory> categoryList;
        if (Objects.nonNull(supplierCategory.getSupplierCategoryId())) {
            categoryList = supplierCategoryService.list(new LambdaQueryWrapper<SupplierCategory>()
                    .eq(SupplierCategory::getName, supplierCategory.getName())
                    .eq(SupplierCategory::getShopId, shopId)
                    .ne(SupplierCategory::getSupplierCategoryId, supplierCategory.getSupplierCategoryId()));
        } else {
            categoryList = supplierCategoryService.list(new LambdaQueryWrapper<SupplierCategory>().
                    eq(SupplierCategory::getName, supplierCategory.getName())
                    .eq(SupplierCategory::getShopId, shopId));
        }
        if (CollectionUtils.isNotEmpty(categoryList)) {
            // 供应商分类已存在
            throw new YamiShopBindException("yami.supplier.category.already.exit");
        }
    }
}
