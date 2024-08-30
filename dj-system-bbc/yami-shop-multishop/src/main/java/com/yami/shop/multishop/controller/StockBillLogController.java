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
import com.yami.shop.bean.enums.StockBillType;
import com.yami.shop.bean.enums.StockType;
import com.yami.shop.bean.model.StockBillLog;
import com.yami.shop.bean.model.StockBillLogItem;
import com.yami.shop.bean.param.ProductParam;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.StockBillLogService;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


/**
 * 出入库明细
 *
 * @author LGH
 * @date 2021-09-09 13:11:15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/shop/stockBillLog" )
@Api(tags = "出入库明细接口")
public class StockBillLogController {

    private final StockBillLogService stockBillLogService;
    private final ShopEmployeeService shopEmployeeService;

    @GetMapping("/page" )
    @ApiModelProperty(value = "分页获取出入库明细信息", notes = "分页获取出入库明细信息")
    public ServerResponseEntity<IPage<StockBillLog>> getStockBillLogPage(PageParam<StockBillLog> page, StockBillLog stockBillLog) {
        stockBillLog.setShopId(SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success(stockBillLogService.pageByParams(page, stockBillLog));
    }

    @GetMapping("/info/{stockBillLogId}" )
    @ApiModelProperty(value = "通过id查询出入库明细信息", notes = "通过id查询出入库明细信息")
    public ServerResponseEntity<StockBillLog> getById(@PathVariable("stockBillLogId") Long stockBillLogId) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        StockBillLog stockBillLog = stockBillLogService.getByStockBillLogId(stockBillLogId);
        if (!Objects.equals(shopId, stockBillLog.getShopId())) {
            throw new YamiShopBindException("当前出入库明细信息出错");
        }
        return ServerResponseEntity.success(stockBillLog);
    }

    @PostMapping
    @ApiOperation(value = "保存其他出入库明细", notes = "保存其他出入库明细")
    public ServerResponseEntity<Void> save(@RequestBody @Valid StockBillLog stockBillLog) {
        //出库操作下判断出库数是否超出库存数？执行出库 ： 返回错误消息
        if (stockBillLog.getType() == 1){
            List<StockBillLogItem> stockBillLogItems = stockBillLog.getStockBillLogItems();
            for (int i = 0; i < stockBillLogItems.size(); i++) {
                if (stockBillLogItems.get(i).getStockCount() > stockBillLogItems.get(i).getStocks()){
                    throw new YamiShopBindException("yami.deliverySum.exceed.stocks");
                }
            }
        }
        stockBillLog.setShopId(SecurityUtils.getShopUser().getShopId());
        stockBillLog.setEmployeeId(SecurityUtils.getShopUser().getEmployeeId());
        stockBillLog.setMakerMobile(shopEmployeeService.getShopEmployeeById(stockBillLog.getEmployeeId()).getMobile());
        if (!Objects.equals(stockBillLog.getStockBillType(), StockBillType.PURCHASE_STORAGE.value())) {
            stockBillLog.setStockBillType(Objects.equals(stockBillLog.getType(), StockType.OUT_OF_STOCK.value()) ? StockBillType.OTHER_OUTBOUND.value() : StockBillType.OTHER_STORAGE.value());
        }
        stockBillLogService.saveInfo(stockBillLog);
        return ServerResponseEntity.success();
    }

    @SysLog("修改出入库明细")
    @PutMapping
    @ApiOperation(value = "更新其他出入库明细", notes = "更新其他出入库明细")
    public ServerResponseEntity<Void> updateById(@RequestBody @Valid StockBillLog stockBillLog) {
        if (Objects.isNull(stockBillLog.getStockBillLogId())) {
            throw new YamiShopBindException("id不能为空");
        }
        stockBillLog.setShopId(SecurityUtils.getShopUser().getShopId());
        stockBillLog.setEmployeeId(SecurityUtils.getShopUser().getEmployeeId());
        stockBillLog.setMakerMobile(shopEmployeeService.getShopEmployeeById(stockBillLog.getEmployeeId()).getMobile());
        stockBillLogService.updateInfo(stockBillLog);
        return ServerResponseEntity.success();
    }

    @GetMapping("/exportStockBillLog")
    @ApiOperation(value = "导出出入库明细列表", notes = "导出出入库明细列表")
    public void exportStockBillLog(StockBillLog stockBillLog, HttpServletResponse response) {
        stockBillLog.setShopId(SecurityUtils.getShopUser().getShopId());
        stockBillLogService.exportStockBillLog(stockBillLog, response);
    }

    @PutMapping("/voided")
    @ApiOperation(value = "作废出入库明细单", notes = "作废出入库明细单")
    public ServerResponseEntity<Void> voided(@RequestParam("stockBillLogId") Long stockBillLogId) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        stockBillLogService.voided(shopId, stockBillLogId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/inquire")
    @ApiOperation(value = "库存查询", notes = "库存查询")
    public ServerResponseEntity<IPage<StockBillLogItem>> inquire(PageParam<StockBillLog> page, ProductParam productParam) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        productParam.setShopId(shopId);
        return ServerResponseEntity.success(stockBillLogService.inquire(page, productParam));
    }

    @GetMapping("/purchasePage" )
    @ApiModelProperty(value = "分页获取采购库存明细信息", notes = "分页获取采购库存明细信息")
    public ServerResponseEntity<IPage<StockBillLog>> purchasePage(PageParam<StockBillLog> page, StockBillLog stockBillLog) {
        stockBillLog.setShopId(SecurityUtils.getShopUser().getShopId());
        stockBillLog.setStockBillType(StockBillType.PURCHASE_STORAGE.value());
        return ServerResponseEntity.success(stockBillLogService.purchasePage(page, stockBillLog));
    }

    @GetMapping("/purchaseInfo/{stockBillLogId}" )
    @ApiModelProperty(value = "通过id查询出入库明细信息", notes = "通过id查询出入库明细信息")
    public ServerResponseEntity<StockBillLog> purchaseInfo(@PathVariable("stockBillLogId") Long stockBillLogId) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        StockBillLog stockBillLog = stockBillLogService.purchaseInfo(stockBillLogId);
        if (!Objects.equals(shopId, stockBillLog.getShopId())) {
            throw new YamiShopBindException("yami.data.deleted.or.not.exist");
        }
        return ServerResponseEntity.success(stockBillLog);
    }

}
