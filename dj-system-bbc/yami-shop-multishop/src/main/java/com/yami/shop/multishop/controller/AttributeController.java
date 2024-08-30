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
import com.yami.shop.bean.enums.ProdPropRule;
import com.yami.shop.bean.model.ProdProp;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ProdPropService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 参数管理
 * @author lgh
 */
@RestController
@RequestMapping("/admin/attribute")
@Api(tags = "商品规格属性接口")
public class AttributeController {

    @Autowired
    private ProdPropService prodPropService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页获取商品规格属性", notes = "分页获取商品规格属性")
    public ServerResponseEntity<IPage<ProdProp>> page(ProdProp prodProp,PageParam<ProdProp> page){
        prodProp.setRule(ProdPropRule.ATTRIBUTE.value());
        prodProp.setShopId(SecurityUtils.getShopUser().getShopId());
        IPage<ProdProp> iPage = prodPropService.pagePropAndValue(prodProp,page);
        return ServerResponseEntity.success(iPage);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据规格id获取规格", notes = "根据规格id获取规格")
    public ServerResponseEntity<ProdProp> info(@PathVariable("id") Long id){
        ProdProp prodProp = prodPropService.getById(id);
        if (!Objects.equals(SecurityUtils.getShopUser().getShopId(), prodProp.getShopId())) {
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(prodProp);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation(value = "新增商品规格", notes = "新增商品规格")
    public ServerResponseEntity<Void> save(@Valid ProdProp prodProp){
        prodProp.setRule(ProdPropRule.ATTRIBUTE.value());
        prodProp.setShopId(SecurityUtils.getShopUser().getShopId());
        prodPropService.saveProdPropAndValues(prodProp);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @ApiOperation(value = "修改商品规格", notes = "修改商品规格")
    public ServerResponseEntity<Void> update(@Valid ProdProp prodProp){
        ProdProp dbProdProp = prodPropService.getById(prodProp.getPropId());
        if (!Objects.equals(dbProdProp.getShopId(), SecurityUtils.getShopUser().getShopId())) {
            // 没有权限获取该商品规格信息
            throw new YamiShopBindException("yami.product.no.auth");
        }
        prodProp.setRule(ProdPropRule.ATTRIBUTE.value());
        prodProp.setShopId(SecurityUtils.getShopUser().getShopId());
        prodPropService.updateProdPropAndValues(prodProp);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据规格id删除商品规格", notes = "根据规格id删除商品规格")
    public ServerResponseEntity<Void> delete(@PathVariable Long id){
        prodPropService.deleteProdPropAndValues(id,ProdPropRule.ATTRIBUTE.value(),SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success();
    }
}
