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
import com.yami.shop.bean.model.ProdParameter;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.ProdParameterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Citrus
 * @date 2021-11-01 16:50:52
 */
@RestController
@RequestMapping("/prod/prodParameter")
@Api(tags = "商家端商品参数接口")
public class ProdParameterController {

    @Autowired
    private ProdParameterService prodParameterService;

    @GetMapping("/page")
    @ApiOperation(value = "返回商品参数分页数据")
    public ServerResponseEntity<IPage<ProdParameter>> getProdParameterPage(PageParam<ProdParameter> page, ProdParameter prodParameter) {
        return ServerResponseEntity.success(prodParameterService.page(page, new LambdaQueryWrapper<ProdParameter>()));
    }

    @GetMapping("/info/{prodParameterId}")
    @ApiOperation(value = "根据id查询")
    @ApiImplicitParam(name = "prodParameterId", value = "商品参数id", required = true, dataType = "Long")
    public ServerResponseEntity<ProdParameter> getById(@PathVariable("prodParameterId") Long prodParameterId) {
        return ServerResponseEntity.success(prodParameterService.getById(prodParameterId));
    }

    @PostMapping
    @PreAuthorize("@pms.hasPermission('multishop:prodParameter:save')")
    @ApiOperation(value = "新增商品参数")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid ProdParameter prodParameter) {
        return ServerResponseEntity.success(prodParameterService.save(prodParameter));
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('multishop:prodParameter:update')")
    @ApiOperation(value = "修改商品参数")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid ProdParameter prodParameter) {
        return ServerResponseEntity.success(prodParameterService.updateById(prodParameter));
    }

    @DeleteMapping("/{prodParameterId}")
    @PreAuthorize("@pms.hasPermission('multishop:prodParameter:delete')")
    @ApiOperation(value = "根据id删除商品参数")
    @ApiImplicitParam(name = "prodParameterId", value = "商品参数id", required = true, dataType = "Long")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long prodParameterId) {
        return ServerResponseEntity.success(prodParameterService.removeById(prodParameterId));
    }
}
