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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.model.StockChangeReason;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.StockChangeReasonService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 *
 * 出入库原因
 * @author LGH
 * @date 2021-09-07 16:04:18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/platform/stockChangeReason" )
@Api(tags = "platform")
public class StockChangeReasonController {

    private final StockChangeReasonService stockChangeReasonService;

    @GetMapping("/page" )
    @ApiOperation(value = "分页获取出入库原因", notes = "分页获取出入库原因")
    public ServerResponseEntity<IPage<StockChangeReason>> getStockChangeReasonPage(PageParam<StockChangeReason> page, StockChangeReason stockChangeReason) {
        stockChangeReason.setShopId(Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success(stockChangeReasonService.pageByParams(page, stockChangeReason));
    }

    @GetMapping("/list")
    @ApiModelProperty(value = "获取出入库原因列表(不分页)", notes = "获取出入库原因列表")
    public ServerResponseEntity<List<StockChangeReason>> listResponse(StockChangeReason stockChangeReason) {
        stockChangeReason.setShopId(Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success(stockChangeReasonService.listByParams(stockChangeReason));
    }

    @GetMapping("/info/{stockChangeReasonId}" )
    @ApiOperation(value = "根据id查询出入库原因", notes = "根据id查询出入库原因")
    public ServerResponseEntity<StockChangeReason> getById(@PathVariable("stockChangeReasonId") Long stockChangeReasonId) {
        Long shopId = Constant.PLATFORM_SHOP_ID;
        StockChangeReason stockChangeReason = stockChangeReasonService.getOne(Wrappers.lambdaQuery(StockChangeReason.class)
                .eq(StockChangeReason::getStockChangeReasonId, stockChangeReasonId)
                .eq(StockChangeReason::getShopId, shopId)
        );
        stockChangeReason.setShopId(null);
        return ServerResponseEntity.success(stockChangeReason);
    }

    @PostMapping
    @ApiOperation(value = "新增出入库原因", notes = "新增出入库原因")
    public ServerResponseEntity<Void> save(@RequestBody @Valid StockChangeReason stockChangeReason) {
        stockChangeReason.setShopId(Constant.PLATFORM_SHOP_ID);
        stockChangeReasonService.saveInfo(stockChangeReason);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "修改出入库原因", notes = "修改出入库原因")
    public ServerResponseEntity<Void> updateById(@RequestBody @Valid StockChangeReason stockChangeReason) {
        stockChangeReason.setShopId(Constant.PLATFORM_SHOP_ID);
        stockChangeReasonService.updateInfo(stockChangeReason);
        return ServerResponseEntity.success();
    }

    @PutMapping("/changeStatus" )
    @ApiModelProperty("修改出入库原因状态")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "stockChangeReasonId", value = "出入库原因id"),
            @ApiImplicitParam(name = "status", value = "状态，1：启用 0：禁用")
    })
    public ServerResponseEntity<Void> removeById(@RequestParam("stockChangeReasonId") Long stockChangeReasonId, @RequestParam("status") Integer status) {
        Long shopId = Constant.PLATFORM_SHOP_ID;
        stockChangeReasonService.update(Wrappers.lambdaUpdate(StockChangeReason.class)
                .set(StockChangeReason::getStatus, status)
                .eq(StockChangeReason::getStockChangeReasonId, stockChangeReasonId)
                .eq(StockChangeReason::getShopId, shopId)
        );
        return ServerResponseEntity.success();
    }

}
