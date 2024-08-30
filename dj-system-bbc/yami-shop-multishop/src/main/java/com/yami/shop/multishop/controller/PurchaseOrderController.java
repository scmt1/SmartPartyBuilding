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
import com.yami.shop.bean.model.PurchaseOrder;
import com.yami.shop.bean.vo.ExcelInfoVO;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.model.YamiShopUser;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.PurchaseOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Objects;

/**
 * 采购订单
 *
 * @author YXF
 * @date 2021-09-08 10:42:01
 */
@RestController
@AllArgsConstructor
@RequestMapping("/purchase/order" )
@Api(tags = "采购订单接口")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param purchaseOrder
     * @return 分页数据
     */
    @GetMapping("/page" )
    @ApiOperation(value = "分页查询采购订单", notes = "分页查询采购订单")
    public ServerResponseEntity<IPage<PurchaseOrder>> getPurchaseOrderPage(PageParam<PurchaseOrder> page, PurchaseOrder purchaseOrder) {
        purchaseOrder.setShopId(SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success(purchaseOrderService.pagePurchaseOrder(page, purchaseOrder));
    }


    /**
     * 通过id查询
     * @param purchaseOrderId id
     * @return 单个数据
     */
    @GetMapping("/info/{purchaseOrderId}")
    @ApiOperation(value = "根据id查询采购订单", notes = "根据id查询采购订单")
    public ServerResponseEntity<PurchaseOrder> getById(@PathVariable("purchaseOrderId") Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderService.info(purchaseOrderId);
        if (!Objects.equals(SecurityUtils.getShopUser().getShopId(), purchaseOrder.getShopId())) {
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(purchaseOrder);
    }

    /**
     * 新增
     * @param purchaseOrder
     * @return 是否新增成功
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('purchase:purchaseOrder:save')" )
    @ApiOperation(value = "新增采购订单", notes = "新增采购订单")
    public ServerResponseEntity<Void> save(@RequestBody @Valid PurchaseOrder purchaseOrder) {
        YamiShopUser shopUser = SecurityUtils.getShopUser();
        purchaseOrder.setShopId(shopUser.getShopId());
        purchaseOrder.setEmployeeId(shopUser.getEmployeeId());
        purchaseOrderService.savePurchaseOrder(purchaseOrder);
        return ServerResponseEntity.success();
    }

    /**
     * 通过id删除
     * @param purchaseOrderId id
     * @return 是否删除成功
     */
    @DeleteMapping("/{purchaseOrderId}" )
    @ApiOperation(value = "根据id删除采购订单", notes = "根据id删除采购订单")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long purchaseOrderId) {
        return ServerResponseEntity.success(purchaseOrderService.removeById(purchaseOrderId));
    }

    /**
     * 入库
     * @param purchaseOrder
     * @return 是否修改成功
     */
    @PutMapping("/inbound")
    @PreAuthorize("@pms.hasPermission('purchase:purchaseOrder:inbound')" )
    @ApiOperation(value = "入库采购订单", notes = "入库采购订单")
    public ServerResponseEntity inbound(@RequestBody PurchaseOrder purchaseOrder) {
        purchaseOrder.setShopId(SecurityUtils.getShopUser().getShopId());
        purchaseOrderService.inbound(purchaseOrder);
        return ServerResponseEntity.success();
    }

    /**
     * 作废
     * @param purchaseOrderId
     */
    @DeleteMapping("/nullify/{purchaseOrderId}" )
    @PreAuthorize("@pms.hasPermission('purchase:purchaseOrder:nullify')" )
    @ApiOperation(value = "作废采购订单", notes = "作废采购订单")
    public ServerResponseEntity nullify(@PathVariable Long purchaseOrderId) {
        purchaseOrderService.nullify(SecurityUtils.getShopUser().getShopId(), purchaseOrderId);
        return ServerResponseEntity.success();
    }

    /**
     * 完成
     * @param purchaseOrder
     */
    @PutMapping("/complete" )
    @PreAuthorize("@pms.hasPermission('purchase:purchaseOrder:complete')" )
    @ApiOperation(value = "完成采购订单", notes = "完成采购订单")
    public ServerResponseEntity finish(@RequestBody PurchaseOrder purchaseOrder) {
        purchaseOrder.setShopId(SecurityUtils.getShopUser().getShopId());
        purchaseOrderService.complete(SecurityUtils.getShopUser().getShopId(), purchaseOrder.getPurchaseOrderId());
        return ServerResponseEntity.success();
    }

    /**
     * 下载模板
     * @param response
     */
    @GetMapping(value = "/downloadModel")
    @ResponseBody
    @ApiOperation(value = "下载导入采购商品模板", notes = "下载导入采购商品模板")
    public void downloadModel(HttpServletResponse response) {
        purchaseOrderService.downloadModel(response);
    }


    /**
     * 导入文件
     */
    @PostMapping("/exportExcel/{supplierId}")
    @ResponseBody
    @ApiOperation(value = "导入采购商品", notes = "导入采购商品")
    public ServerResponseEntity<ExcelInfoVO> exportExcel(@RequestParam("excelFile") MultipartFile excelFile, @PathVariable Long supplierId) throws Exception {
        if (excelFile == null) {
            // 网络繁忙，请稍后重试
            throw new YamiShopBindException("yami.network.busy");
        }

        Long shopId = SecurityUtils.getShopUser().getShopId();
        ExcelInfoVO excelInfoVO = purchaseOrderService.parseFile(excelFile, shopId, supplierId);
        return ServerResponseEntity.success(excelInfoVO);
    }

    @GetMapping("/inbound/export")
    @ApiModelProperty(value = "导出入库商品", notes = "导出入库商品")
    public void export(PurchaseOrder purchaseOrder, HttpServletResponse response) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        purchaseOrder.setShopId(shopId);
        purchaseOrderService.export(purchaseOrder,response);
    }

    /**
     * 下载入库模板
     * @param response
     */
    @GetMapping(value = "/inbound/downloadModel")
    @ResponseBody
    @ApiModelProperty(value = "下载入库模板", notes = "下载入库模板")
    public void inboundDownloadModel(HttpServletResponse response) {
        purchaseOrderService.inboundDownloadModel(response);
    }


    /**
     * 导入入库文件
     */
    @PostMapping("/inbound/exportExcel/{purchaseOrderId}")
    @ResponseBody
    @ApiModelProperty(value = "导入入库文件", notes = "导入入库文件")
    public ServerResponseEntity<ExcelInfoVO> inboundExportExcel(@RequestParam("excelFile") MultipartFile excelFile, @PathVariable Long purchaseOrderId) throws Exception {
        if (excelFile == null) {
            // 网络繁忙，请稍后重试
            throw new YamiShopBindException("yami.network.busy");
        }
        Long shopId = SecurityUtils.getShopUser().getShopId();
        ExcelInfoVO excelInfoVO = purchaseOrderService.inboundParseFile(excelFile, shopId, purchaseOrderId);
        return ServerResponseEntity.success(excelInfoVO);
    }

}
