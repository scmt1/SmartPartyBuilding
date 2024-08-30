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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.enums.ProdStatusEnums;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.Supplier;
import com.yami.shop.bean.model.SupplierCategory;
import com.yami.shop.bean.vo.SupplierVO;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ProductService;
import com.yami.shop.service.SupplierCategoryService;
import com.yami.shop.service.SupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author OPGame
 * @date 2020-08-25 15:50:06
 */
@RestController
@RequestMapping("/supplier/supplier")
@Api(tags = "供应商接口")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierCategoryService supplierCategoryService;
    @GetMapping("/page")
    @ApiModelProperty(value = "分页获取供应商信息", notes = "分页获取供应商信息")
    public ServerResponseEntity<IPage<Supplier>> getSupplierPage(PageParam<Supplier> page, Supplier supplier) {
        supplier.setShopId(SecurityUtils.getShopUser().getShopId());
        IPage<Supplier> suppliers = supplierService.pageShop(page, supplier);
        return ServerResponseEntity.success(suppliers);
    }

    @GetMapping("/prodSupplierPage")
    @ApiModelProperty(value = "分页获取供应商商品信息", notes = "分页获取供应商商品信息")
    public ServerResponseEntity<IPage<SupplierVO>> getProdSupplierPage(PageParam<Supplier> page, Supplier supplier) {
        supplier.setShopId(SecurityUtils.getShopUser().getShopId());
        IPage<SupplierVO> prodSupplier = supplierService.pageProdSupplier(page, supplier);
        SupplierVO supplierVO = null;
        // 获取自采供应商的数据
        for (SupplierVO record:prodSupplier.getRecords()) {
            if (Objects.equals(record.getIsDefault(),1)){
                supplierVO = record;
            }
        }
        // 如果有自采供应商，插入商品数量
        if (Objects.nonNull(supplierVO)) {
            int count = productService.getProductNum(supplier.getShopId(),ProdStatusEnums.DELETE.getValue());
            supplierVO.setSupplierProdCount(count);
        }
        return ServerResponseEntity.success(prodSupplier);
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取供应商品列表", notes = "获取供应商品列表")
    public ServerResponseEntity<List<Supplier>> listSupplier() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        List<Supplier> supplierList = supplierService.list(Wrappers.lambdaQuery(Supplier.class)
                .eq(Supplier::getShopId, shopId)
        );
        return ServerResponseEntity.success(supplierList);
    }

    @GetMapping("/info/{supplierId}")
    @ApiOperation(value = "根据id查询商品信息", notes = "根据id查询商品信息")
    public ServerResponseEntity<Supplier> getById(@PathVariable("supplierId") Long supplierId) {
        Supplier supplier = supplierService.getById(supplierId);
        if (!Objects.equals(SecurityUtils.getShopUser().getShopId(), supplier.getShopId())) {
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(supplier);
    }

    @PostMapping
    @PreAuthorize("@pms.hasPermission('supplier:supplier:save')")
    @ApiOperation(value = "新增供应商", notes = "新增供应商")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid Supplier supplier) {
        this.checkInfo(supplier);
        supplier.setShopId(SecurityUtils.getShopUser().getShopId());
        supplier.setCreateTime(new Date());
        supplier.setIsDefault(0);
        return ServerResponseEntity.success(supplierService.save(supplier));
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('supplier:supplier:update')")
    @ApiOperation(value = "修改供应商", notes = "修改供应商")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid Supplier supplier) {
        this.checkInfo(supplier);
        supplier.setUpdateTime(new Date());
        if (!Objects.equals(supplier.getIsDefault(),1)){
            supplier.setIsDefault(0);
        }
        return ServerResponseEntity.success(supplierService.updateSupplier(supplier));
    }

    @GetMapping("/exportSupplier")
    @ApiModelProperty(value = "导出供应商", notes = "导出供应商")
    @PreAuthorize("@pms.hasPermission('supplier:supplier:export')")
    public void exportSupplier(Supplier supplier, HttpServletResponse response) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        supplier.setShopId(shopId);
        supplierService.exportSupplier(supplier, response);
    }

    @GetMapping("/downLoadModel")
    @ApiModelProperty(value = "下载导入供应商模板", notes = "下载导入供应商模板")
    @PreAuthorize("@pms.hasPermission('supplier:supplier:downLoad')")
    public void downLoadModel(HttpServletResponse response) {
        supplierService.downLoadModel(SecurityUtils.getShopUser().getShopId(), response);
    }

    @ApiOperation(value = "导入供应商", notes = "导入供应商")
    @PostMapping("/importExcel")
    @ResponseBody
    @PreAuthorize("@pms.hasPermission('supplier:supplier:import')")
    public ServerResponseEntity<Object> importExcel(@RequestParam("excelFile") MultipartFile excelFile) throws Exception {
        if (Objects.isNull(excelFile)) {
            throw new YamiShopBindException("yami.network.busy");
        }
        Object o = supplierService.parseFile(excelFile, SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success(o);
    }

    private void checkInfo(Supplier supplier) {
        Integer nameNumber = supplierService.getCountByName(supplier.getSupplierName(), supplier.getSupplierId(),SecurityUtils.getShopUser().getShopId());
        if (nameNumber >= 1) {
            throw new YamiShopBindException("yami.supplier.supplierName.exist");
        }
        if (Objects.nonNull(supplier.getSupplierCategoryId())) {
            SupplierCategory supplierCategory = supplierCategoryService.getOne(new LambdaQueryWrapper<SupplierCategory>()
                    .eq(SupplierCategory::getSupplierCategoryId, supplier.getSupplierCategoryId()));
            if (Objects.equals(supplierCategory.getStatus(), 0)) {
                //供应商分类不存在或已被禁用
                throw new YamiShopBindException("yami.supplier.category.not.exit");
            }
        }
    }
}
