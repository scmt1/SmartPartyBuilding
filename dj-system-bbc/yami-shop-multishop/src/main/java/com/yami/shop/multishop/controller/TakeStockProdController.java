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

import com.yami.shop.bean.model.TakeStock;
import com.yami.shop.bean.vo.TakeStockProdVO;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.TakeStockExcelService;
import com.yami.shop.service.TakeStockProdService;
import com.yami.shop.service.TakeStockService;
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
 * 盘点商品列表
 * @author Citrus
 * @date 2021-09-15 11:18:33
 */
@RestController
@RequestMapping("/stock/takeStockProd" )
@Api(tags = "盘点商品接口")
public class TakeStockProdController {

    @Autowired
    private TakeStockProdService takeStockProdService;
    @Autowired
    private TakeStockService takeStockService;
    @Autowired
    private TakeStockExcelService takeStockExcelService;

    @GetMapping("/list" )
    @ApiOperation(value = "根据盘点id获取盘点商品列表", notes = "根据盘点id获取盘点商品列表")
    public ServerResponseEntity<List<TakeStockProdVO>> getTakeStockProdPage(Long takeStockId) {
        return ServerResponseEntity.success(takeStockProdService.listShopProd(takeStockId));
    }

    @GetMapping("/downLoadModel")
    @ApiModelProperty(value = "下载盘点商品导入模板", notes = "下载盘点商品导入模板")
    @PreAuthorize("@pms.hasPermission('multishop:takeStockProd:download')" )
    public void downLoadModel(HttpServletResponse response) {
        takeStockExcelService.downLoadModel(response);
    }

    @ApiOperation(value = "导入盘点商品", notes = "导入盘点商品")
    @PostMapping("/importExcel")
    @ResponseBody
    @PreAuthorize("@pms.hasPermission('multishop:takeStockProd:import')" )
    public ServerResponseEntity<Object> importExcel(@RequestParam("excelFile") MultipartFile excelFile,
                                              @RequestParam("takeStockId") Long takeStockId) throws Exception {
        if (Objects.isNull(excelFile)) {
            throw new YamiShopBindException("yami.network.busy");
        }
        if (Objects.isNull(takeStockId)){
            throw new YamiShopBindException("yami.supplier.not.null");
        }
        Object o = takeStockExcelService.parseFile(excelFile, SecurityUtils.getShopUser().getShopId(), takeStockId);
        return ServerResponseEntity.success(o);
    }

    @GetMapping("/exportTakeStockProd")
    @ApiModelProperty(value = "导出盘点商品", notes = "导出盘点商品")
    @PreAuthorize("@pms.hasPermission('multishop:takeStockProd:export')" )
    public void exportTakeStockProd(@RequestParam("takeStockId") Long takeStockId,HttpServletResponse response) {
        if (Objects.isNull(takeStockId)) {
            //盘点Id不能为空
            throw new YamiShopBindException("yami.take.stock.id.not.null");
        }
        TakeStock takeStock = takeStockService.getById(takeStockId);
        if (!Objects.equals(takeStock.getShopId(),SecurityUtils.getShopUser().getShopId())) {
            //当前盘点信息不属于你的店铺
            throw new YamiShopBindException("yami.take.stock.message.error");
        }
        takeStockExcelService.exportTakeStockProd(takeStockId,response);
    }
}
