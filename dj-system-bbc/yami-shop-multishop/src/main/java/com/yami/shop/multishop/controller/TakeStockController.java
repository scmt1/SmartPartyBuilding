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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.TakeStockBillStatusEnum;
import com.yami.shop.bean.model.TakeStock;
import com.yami.shop.bean.param.TakeStockParam;
import com.yami.shop.bean.vo.TakeStockVO;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.model.YamiShopUser;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.TakeStockExcelService;
import com.yami.shop.service.TakeStockService;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


/**
 * @author Citrus
 * @date 2021-09-15 11:18:33
 */
@RestController
@RequestMapping("/stock/takeStock")
@Api(tags = "库存盘点接口")
public class TakeStockController {

    @Autowired
    private TakeStockService takeStockService;
    @Autowired
    private Snowflake snowflake;
    @Autowired
    private ShopEmployeeService shopEmployeeService;
    @Autowired
    private TakeStockExcelService takeStockExcelService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询库存盘点信息", notes = "分页查询库存盘点信息")
    public ServerResponseEntity<IPage<TakeStockVO>> getTakeStockPage(PageParam<TakeStock> page, TakeStockParam takeStock) {
        takeStock.setShopId(SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success(takeStockService.pageByParams(page, takeStock));
    }

    @GetMapping("/info/{takeStockId}")
    @ApiOperation(value = "根据id查询库存盘点信息", notes = "根据id查询库存盘点信息")
    public ServerResponseEntity<TakeStock> getById(@PathVariable("takeStockId") Long takeStockId) {
        return ServerResponseEntity.success(takeStockService.getById(takeStockId));
    }

    @PostMapping
    @ApiOperation(value = "新增盘点", notes = "新增盘点")
    @PreAuthorize("@pms.hasPermission('multishop:takeStock:save')" )
    public ServerResponseEntity<Long> save(@RequestBody @Valid TakeStock takeStock) {
        if (Objects.isNull(takeStock.getCreateTime())) {
            //盘点开始时间不能为空
            throw new YamiShopBindException("yami.take.stock.time.not.null");
        }
        takeStock.setTakeStockNo(StringUtils.join("TSC", String.valueOf(snowflake.nextId())));
        takeStock.setShopId(SecurityUtils.getShopUser().getShopId());
        takeStock.setBillStatus(TakeStockBillStatusEnum.TAKING.value());
        Long employeeId = SecurityUtils.getShopUser().getEmployeeId();
        takeStock.setMaker(employeeId);
        takeStock.setMakerMobile(shopEmployeeService.getShopEmployeeById(employeeId).getMobile());
        takeStockService.save(takeStock);
        return ServerResponseEntity.success(takeStock.getTakeStockId());
    }

    @PutMapping
    @ApiOperation(value = "保存草稿", notes = "保存草稿")
    @PreAuthorize("@pms.hasPermission('multishop:takeStockProd:update')" )
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid TakeStock takeStock) {
        takeStock.setShopId(SecurityUtils.getShopUser().getShopId());
        if (CollectionUtil.isEmpty(takeStock.getTakeStockProdList())) {
            //盘点商品不能为空
            throw new YamiShopBindException("yami.take.stock.prod.null");
        }
        return ServerResponseEntity.success(takeStockService.updateTakeStock(takeStock));
    }

    @PutMapping("/finishInventory")
    @ApiOperation(value = "完成盘点", notes = "完成盘点")
    @PreAuthorize("@pms.hasPermission('multishop:takeStockProd:finish')" )
    public ServerResponseEntity<Boolean> finishInventory(@RequestBody @Valid TakeStock takeStock) {
        YamiShopUser shopUser = SecurityUtils.getShopUser();
        takeStock.setShopId(SecurityUtils.getShopUser().getShopId());
        takeStock.setMaker(shopUser.getEmployeeId());
        if (CollectionUtil.isEmpty(takeStock.getTakeStockProdList())) {
            //盘点商品不能为空
            throw new YamiShopBindException("yami.take.stock.prod.null");
        }
        return ServerResponseEntity.success(takeStockService.finishTakeStock(takeStock));
    }

    @PutMapping("/voidedInventory/{takeStockId}")
    @ApiOperation(value = "作废盘点", notes = "作废盘点")
    @PreAuthorize("@pms.hasPermission('multishop:takeStock:voided')" )
    public ServerResponseEntity<Boolean> voidedInventory(@PathVariable("takeStockId") Long takeStockId) {
        TakeStock takeStock = takeStockService.getById(takeStockId);
        return ServerResponseEntity.success(takeStockService.voidedTakeStock(takeStock));
    }

    @GetMapping("/exportTakeStock")
    @ApiModelProperty(value = "导出盘点信息", notes = "导出盘点信息")
    @PreAuthorize("@pms.hasPermission('multishop:takeStock:export')" )
    public void exportTakeStock(TakeStockParam takeStock, HttpServletResponse response) {
        takeStock.setShopId(SecurityUtils.getShopUser().getShopId());
        List<TakeStockVO> takeStocks = takeStockService.listByParams(takeStock);
        takeStockExcelService.createExcel(takeStocks, false, response);
    }
}
