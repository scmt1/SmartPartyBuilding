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
import com.yami.shop.bean.model.PurchaseProd;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.PurchaseProdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *
 *
 * @author YXF
 * @date 2021-09-08 10:42:00
 */
@RestController
@AllArgsConstructor
@RequestMapping("/purchase/prod" )
@Api(tags = "采购商品接口")
public class PurchaseProdController {

    private final PurchaseProdService purchaseProdService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param purchaseProd
     * @return 分页数据
     */
    @GetMapping("/page" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<PurchaseProd>> getPurchaseProdPage(PageParam<PurchaseProd> page, PurchaseProd purchaseProd) {
        return ServerResponseEntity.success(purchaseProdService.page(page, new LambdaQueryWrapper<PurchaseProd>()));
    }


    /**
     * 通过id查询
     * @param purchaseProdId id
     * @return 单个数据
     */
    @GetMapping("/info/{purchaseProdId}" )
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    public ServerResponseEntity<PurchaseProd> getById(@PathVariable("purchaseProdId") Long purchaseProdId) {
        return ServerResponseEntity.success(purchaseProdService.getById(purchaseProdId));
    }

    /**
     * 新增
     * @param purchaseProd
     * @return 是否新增成功
     */
    @SysLog("新增" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('api:purchaseProd:save')" )
    @ApiOperation(value = "新增", notes = "新增")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid PurchaseProd purchaseProd) {
        return ServerResponseEntity.success(purchaseProdService.save(purchaseProd));
    }

    /**
     * 修改
     * @param purchaseProd
     * @return 是否修改成功
     */
    @SysLog("修改" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('api:purchaseProd:update')" )
    @ApiOperation(value = "修改", notes = "修改")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid PurchaseProd purchaseProd) {
        return ServerResponseEntity.success(purchaseProdService.updateById(purchaseProd));
    }

    /**
     * 通过id删除
     * @param purchaseProdId id
     * @return 是否删除成功
     */
    @SysLog("删除" )
    @DeleteMapping("/{purchaseProdId}" )
    @PreAuthorize("@pms.hasPermission('api:purchaseProd:delete')" )
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long purchaseProdId) {
        return ServerResponseEntity.success(purchaseProdService.removeById(purchaseProdId));
    }

}
