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
import com.yami.shop.bean.model.Supplier;
import com.yami.shop.bean.model.SupplierProd;
import com.yami.shop.bean.vo.SupplierProdVO;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.SupplierProdService;
import com.yami.shop.service.SupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;


/**
 * @author Citrus
 * @date 2021-09-07 10:12:32
 */
@RestController
@RequestMapping("/supplier/supplierProd")
@Api(tags = "供应商商品接口")
public class SupplierProdController {

    @Autowired
    private SupplierProdService supplierProdService;
    @Autowired
    private SupplierService supplierService;

    @GetMapping("/listSupplierProd")
    @ApiOperation(value = "根据供应商id获取供应商商品列表", notes = "根据供应商id获取供应商商品列表")
    public ServerResponseEntity<List<SupplierProdVO>> listSupplierProd(@RequestParam("supplierId") Long supplierId) {
        Supplier supplier = supplierService.getById(supplierId);
        if (!Objects.equals(supplier.getShopId(), SecurityUtils.getShopUser().getShopId())) {
            // 无法查看非本店铺的商品
            throw new YamiShopBindException("yami.supplier.prod.not.shop");
        }
        List<SupplierProdVO> supplierProdList = supplierProdService.listSupplierProd(supplierId);
        return ServerResponseEntity.success(supplierProdList);
    }

    @GetMapping("/info/{supplierProdId}")
    @ApiOperation(value = "根据供应商商品id获取供应商商品信息", notes = "根据供应商商品id获取供应商商品信息")
    public ServerResponseEntity<SupplierProd> getById(@PathVariable("supplierProdId") Long supplierProdId) {
        return ServerResponseEntity.success(supplierProdService.getById(supplierProdId));
    }

    @PutMapping
    @ApiOperation(value = "修改供应商商品", notes = "修改供应商商品")
    @PreAuthorize("@pms.hasPermission('multishop:supplierProd:update')")
    public ServerResponseEntity<Boolean> updateBatch(@RequestBody List<SupplierProd> supplierProds,
                                               @RequestParam("supplierId") Long supplierId) {
        boolean update = supplierProdService.addOrUpdate(supplierProds, supplierId);
        supplierProdService.removeCacheBySupplierId(supplierId);
        return ServerResponseEntity.success(update);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页获取供应商商品列表", notes = "分页获取供应商商品列表")
    public ServerResponseEntity<IPage<SupplierProdVO>> page(PageParam<SupplierProdVO> pageParam, @RequestParam("supplierId") Long supplierId,
                                                      @RequestParam("partyCode") String partyCode) {
        Supplier supplier = supplierService.getById(supplierId);
        if (!Objects.equals(supplier.getShopId(), SecurityUtils.getShopUser().getShopId())) {
            // 无法查看非本店铺的商品
            throw new YamiShopBindException("yami.supplier.prod.not.shop");
        }
        IPage<SupplierProdVO> page = null;
        if (Objects.equals(supplier.getIsDefault(), 1)) {
            page = supplierProdService.defaultSupplierProdPage(pageParam, supplier, partyCode);
        } else {
            page = supplierProdService.supplierProdPage(pageParam, supplier, partyCode);
        }
        return ServerResponseEntity.success(page);
    }

    @GetMapping("/downLoadModel")
    @ApiModelProperty(value = "下载导入供应商商品模板", notes = "下载导入供应商商品模板")
    @PreAuthorize("@pms.hasPermission('multishop:supplierProd:downLoad')")
    public void downLoadModel(HttpServletResponse response) {
        supplierProdService.downLoadModel(response);
    }

    @ApiOperation(value = "导入供应商商品", notes = "导入供应商商品")
    @PostMapping("/importExcel")
    @ResponseBody
    @PreAuthorize("@pms.hasPermission('multishop:supplierProd:import')")
    public ServerResponseEntity<Object> importExcel(@RequestParam("excelFile") MultipartFile excelFile, @RequestParam("supplierId") Long supplierId) throws Exception {
        if (Objects.isNull(excelFile)) {
            throw new YamiShopBindException("yami.network.busy");
        }
        if (Objects.isNull(supplierId)) {
            throw new YamiShopBindException("yami.supplier.not.null");
        }
        Object o = supplierProdService.parseFile(excelFile, SecurityUtils.getShopUser().getShopId(), supplierId);
        supplierProdService.removeCacheBySupplierId(supplierId);
        return ServerResponseEntity.success(o);
    }

    @GetMapping("/exportSupplierProd")
    @ApiModelProperty(value = "导出供应商商品", notes = "导出供应商商品")
    @PreAuthorize("@pms.hasPermission('multishop:supplierProd:export')")
    public void exportSupplierProd(@RequestParam("supplierId") Long supplierId, HttpServletResponse response) {
        supplierProdService.exportSupplierProd(supplierId, response);
    }
}
