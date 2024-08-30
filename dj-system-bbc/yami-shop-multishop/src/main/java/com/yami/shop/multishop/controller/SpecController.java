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
import com.yami.shop.bean.enums.ProdPropRule;
import com.yami.shop.bean.model.ProdProp;
import com.yami.shop.bean.model.ProdPropValue;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ProdPropService;
import com.yami.shop.service.ProdPropValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 规格管理
 *
 * @author lgh
 */
@RestController
@RequestMapping("/prod/spec")
@Api(tags = "规格管理接口")
public class SpecController {

    @Autowired
    private ProdPropService prodPropService;
    @Autowired
    private ProdPropValueService prodPropValueService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('prod:spec:page')")
    @ApiOperation(value = "分页查询规格", notes = "分页查询规格")
    public ServerResponseEntity<IPage<ProdProp>> page(ProdProp prodProp,PageParam<ProdProp> page) {
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(SecurityUtils.getShopUser().getShopId());
        IPage<ProdProp> list = prodPropService.pagePropAndValue(prodProp, page);
        return ServerResponseEntity.success(list);
    }


    /**
     * 获取所有的规格
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取所有的规格", notes = "获取所有的规格")
    public ServerResponseEntity<List<ProdProp>> list() {
        ProdProp prodProp = new ProdProp();
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(SecurityUtils.getShopUser().getShopId());
        List<ProdProp> list = prodPropService.listByLang(prodProp);
        return ServerResponseEntity.success(list);
    }

    /**
     * 根据规格id获取规格值
     */
    @GetMapping("/listSpecValue/{specId}")
    @ApiOperation(value = "根据规格id获取规格值", notes = "根据规格id获取规格值")
    public ServerResponseEntity<List<ProdPropValue>> listSpecValue(@PathVariable("specId") Long specId) {
        List<ProdPropValue> list = prodPropValueService.propValueListByPropId(specId);
        return ServerResponseEntity.success(list);
    }

    /**
     * 根据获取规格值最大的自增id
     */
    @GetMapping("/listSpecMaxValueId")
    @ApiOperation(value = "根据获取规格值最大的自增id", notes = "根据获取规格值最大的自增id")
    public ServerResponseEntity<Long> listSpecMaxValueId() {
        ProdPropValue propValue = prodPropValueService.getOne(new LambdaQueryWrapper<ProdPropValue>()
                .orderByDesc(ProdPropValue::getValueId).last("limit 1"));
        return ServerResponseEntity.success(Objects.isNull(propValue) ? 0L : propValue.getValueId());
    }
    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('prod:spec:save')")
    @ApiOperation(value = "新增规格", notes = "新增规格")
    public ServerResponseEntity<Void> save(@Valid @RequestBody ProdProp prodProp) {
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(SecurityUtils.getShopUser().getShopId());
        prodPropService.saveProdPropAndValues(prodProp);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('prod:spec:update')")
    @ApiOperation(value = "修改规格", notes = "修改规格")
    public ServerResponseEntity<Void> update(@Valid @RequestBody ProdProp prodProp) {
        ProdProp dbProdProp = prodPropService.getById(prodProp.getPropId());
        if (!Objects.equals(dbProdProp.getShopId(), SecurityUtils.getShopUser().getShopId())) {
            // 没有权限获取该商品规格信息
            throw new YamiShopBindException("yami.product.no.auth");
        }
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(SecurityUtils.getShopUser().getShopId());
        prodPropService.updateProdPropAndValues(prodProp);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('prod:spec:delete')")
    @ApiOperation(value = "根据id删除规格", notes = "根据id删除规格")
    public ServerResponseEntity<Void> delete(@PathVariable Long id) {
        prodPropService.deleteProdPropAndValues(id, ProdPropRule.SPEC.value(), SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success();
    }
}
