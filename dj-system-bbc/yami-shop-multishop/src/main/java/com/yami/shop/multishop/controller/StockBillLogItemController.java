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
import com.yami.shop.bean.model.StockBillLog;
import com.yami.shop.bean.model.StockBillLogItem;
import com.yami.shop.bean.vo.StockBillLogItemImportVO;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.StockBillLogItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;


/**
 * 出入库商品项
 *
 * @author LGH
 * @date 2021-09-09 13:11:15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/shop/stockBillLogItem" )
@Api(tags = "出入库明细项接口")
public class StockBillLogItemController {

    private final StockBillLogItemService stockBillLogItemService;

    @GetMapping("/list")
    @ApiOperation(value = "通过出入明细id查询关联的商品列表")
    public ServerResponseEntity<List<StockBillLogItem>> list(@RequestParam("stockBillLogId") Long stockBillLogId) {
        List<StockBillLogItem> stockBillLogItemList = stockBillLogItemService.listByStockBillLogId(stockBillLogId);
        return ServerResponseEntity.success(stockBillLogItemList);
    }

    @GetMapping("/downloadModel")
    @ApiOperation(value = "下载导入商品项模板", notes = "下载导入商品项模板")
    public void downloadModel(HttpServletResponse response, Integer type) {
        stockBillLogItemService.downloadModel(response, type);
    }

    @PostMapping("/importExcel")
    @ApiOperation(value = "批量导入商品项", notes = "批量导入商品项")
    public ServerResponseEntity<StockBillLogItemImportVO> importExcel(@RequestParam("excelFile") MultipartFile excelFile, @RequestParam("type") Integer type) throws Exception {
        if (Objects.isNull(excelFile)) {
            throw new YamiShopBindException("yami.network.busy");
        }
        Long shopId = SecurityUtils.getShopUser().getShopId();
        StockBillLogItemImportVO stockBillLogItemImportVO = stockBillLogItemService.parseFile(excelFile, shopId, type);
        return ServerResponseEntity.success(stockBillLogItemImportVO);
    }

    @GetMapping("/flow")
    @ApiOperation(value = "库存流水", notes = "库存流水")
    public ServerResponseEntity<IPage<StockBillLogItem>> flow(PageParam<StockBillLog> page, StockBillLog stockBillLog) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        stockBillLog.setShopId(shopId);
        IPage<StockBillLogItem> stockBillLogItemPage = stockBillLogItemService.flow(page, stockBillLog);
        return ServerResponseEntity.success(stockBillLogItemPage);
    }

    @GetMapping("/exportFlow")
    @ApiOperation(value = "导出库存流水", notes = "导出库存流水")
    public void exportFlow(StockBillLog stockBillLog, HttpServletResponse response) {
        Long shoId = SecurityUtils.getShopUser().getShopId();
        stockBillLog.setShopId(shoId);
        stockBillLogItemService.exportFlow(stockBillLog, response);
    }
}
