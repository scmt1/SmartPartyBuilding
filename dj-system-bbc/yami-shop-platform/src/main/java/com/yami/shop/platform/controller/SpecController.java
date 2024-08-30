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
import com.yami.shop.bean.enums.ProdPropRule;
import com.yami.shop.bean.model.ProdProp;
import com.yami.shop.bean.model.ProdPropValue;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.ProdPropService;
import com.yami.shop.service.ProdPropValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 规格管理
 *
 * @author lgh
 */
@RestController
@RequestMapping("/prod/spec")
@Api(tags = "规格")
public class SpecController {

    @Autowired
    private ProdPropService prodPropService;
    @Autowired
    private ProdPropValueService prodPropValueService;

    @GetMapping("/list")
    @ApiOperation(value = "获取所有的规格", notes = "获取所有的规格")
    public ServerResponseEntity<List<ProdProp>> list() {
//        List<ProdProp> list = prodPropService.list(new LambdaQueryWrapper<ProdProp>()
//                .eq(ProdProp::getRule, ProdPropRule.SPEC.value()).
//                eq(ProdProp::getShopId, Constant.PLATFORM_SHOP_ID));
        ProdProp prodProp = new ProdProp();
        prodProp.setShopId( Constant.PLATFORM_SHOP_ID);
        prodProp.setRule(ProdPropRule.SPEC.value());
        List<ProdProp> list = prodPropService.listByLang(prodProp);
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页获取", notes = "分页获取")
    public ServerResponseEntity<IPage<ProdProp>> page(ProdProp prodProp, PageParam<ProdProp> page) {
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId( Constant.PLATFORM_SHOP_ID);
        IPage<ProdProp> list = prodPropService.pagePropAndValue(prodProp, page);
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/listSpecValue/{specId}")
    @ApiOperation(value = "根据规格id获取规格值", notes = "根据规格id获取规格值")
    @ApiImplicitParam(name = "specId", value = "规格id")
    public ServerResponseEntity<List<ProdPropValue>> listSpecValue(@PathVariable("specId") Long specId) {
//        List<ProdPropValue> list = prodPropValueService.list(new LambdaQueryWrapper<ProdPropValue>().eq(ProdPropValue::getPropId, specId))
        List<ProdPropValue> list = prodPropValueService.propValueListByPropId(specId);
        return ServerResponseEntity.success(list);
    }

    @PostMapping
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody ProdProp prodProp) {
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(Constant.PLATFORM_SHOP_ID);
        prodPropService.saveProdPropAndValues(prodProp);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "修改", notes = "修改")
    public ServerResponseEntity<Void> update(@Valid @RequestBody ProdProp prodProp) {
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodPropService.updateProdPropAndValues(prodProp);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "id", value = "规格id")
    public ServerResponseEntity<Void> delete(@PathVariable Long id) {
        prodPropService.deleteProdPropAndValues(id, ProdPropRule.SPEC.value(), Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success();
    }

}
