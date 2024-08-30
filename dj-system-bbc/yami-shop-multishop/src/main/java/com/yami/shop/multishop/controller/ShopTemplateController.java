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

import javax.validation.Valid;

import com.yami.shop.bean.enums.TemplateType;
import com.yami.shop.bean.model.ShopRenovation;
import com.yami.shop.bean.model.ShopTemplate;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ShopTemplateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.common.util.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Date;
import java.util.Objects;


/**
 *
 *
 * @author LGH
 * @date 2022-07-29 16:54:02
 */
@RestController
@RequestMapping("/shop/shopTemplate")
@Api(tags = "店铺装修模板接口")
public class ShopTemplateController {

    @Autowired
    private ShopTemplateService shopTemplateService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param shopTemplate 店铺装修模板信息
     * @return 分页数据
     */
    @GetMapping("/pagePC")
    @PreAuthorize("@pms.hasPermission('shop:shopTemplate:pagePC')")
    @ApiOperation(value = "PC端分页查询店铺模板信息", notes = "PC端分页查询店铺模板信息")
    public ServerResponseEntity<IPage<ShopTemplate>> getShopTemplatePagePC(PageParam<ShopTemplate> page, ShopTemplate shopTemplate) {
        return ServerResponseEntity.success(shopTemplateService.page(page, new LambdaQueryWrapper<ShopTemplate>()
                .eq(ShopTemplate::getShopId, SecurityUtils.getShopUser().getShopId())
                .eq(Objects.nonNull(shopTemplate.getType()), ShopTemplate::getType, shopTemplate.getType())
                .orderByDesc(ShopTemplate::getUpdateTime)
        ));
    }

    /**
     * 分页查询
     * @param page 分页对象
     * @param shopTemplate 店铺装修模板信息
     * @return 分页数据
     */
    @GetMapping("/pageMove")
    @PreAuthorize("@pms.hasPermission('shop:shopTemplate:pageMove')")
    @ApiOperation(value = "移动端分页查询店铺模板信息", notes = "移动端分页查询店铺模板信息")
    public ServerResponseEntity<IPage<ShopTemplate>> getShopTemplatePageMove(PageParam<ShopTemplate> page, ShopTemplate shopTemplate) {
        return ServerResponseEntity.success(shopTemplateService.page(page, new LambdaQueryWrapper<ShopTemplate>()
                .eq(ShopTemplate::getShopId, SecurityUtils.getShopUser().getShopId())
                .eq(Objects.nonNull(shopTemplate.getType()), ShopTemplate::getType, shopTemplate.getType())
                .orderByDesc(ShopTemplate::getUpdateTime)
        ));
    }

    /**
     * 通过id查询店铺模板信息
     * @param templateId id
     * @return 单个数据
     */
    @GetMapping("/info/{templateId}" )
    @ApiOperation(value = "通过id查询店铺装修模板信息", notes = "通过id查询店铺装修模板信息")
    public ServerResponseEntity<ShopTemplate> getById(@PathVariable("templateId") Long templateId) {
        ShopTemplate shopTemplate = shopTemplateService.getById(templateId);
        if(!Objects.equals(shopTemplate.getShopId() ,SecurityUtils.getShopUser().getShopId())){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(shopTemplate);
    }


    /**
     * 新增店铺模板信息
     * @param shopTemplate 店铺模板信息
     * @return 是否新增成功
     */
    @PostMapping("/savePC")
    @PreAuthorize("@pms.hasPermission('shop:shopTemplate:savePC')")
    @ApiOperation(value = "PC端新增店铺模板信息", notes = "PC端新增店铺模板信息")
    public ServerResponseEntity<Long> savePC(@RequestBody @Valid ShopTemplate shopTemplate) {
        shopTemplate.setShopId(SecurityUtils.getShopUser().getShopId());
        if (Objects.isNull(shopTemplate.getType())) {
            shopTemplate.setType(TemplateType.H5.value());
        }
        shopTemplateService.save(shopTemplate);
        return ServerResponseEntity.success(shopTemplate.getTemplateId());
    }

    /**
     * 新增店铺模板信息
     * @param shopTemplate 店铺模板信息
     * @return 是否新增成功
     */
    @PostMapping("/saveMove")
    @PreAuthorize("@pms.hasPermission('shop:shopTemplate:saveMove')")
    @ApiOperation(value = "移动端新增店铺模板信息", notes = "移动端新增店铺模板信息")
    public ServerResponseEntity<Long> saveMove(@RequestBody @Valid ShopTemplate shopTemplate) {
        shopTemplate.setShopId(SecurityUtils.getShopUser().getShopId());
        if (Objects.isNull(shopTemplate.getType())) {
            shopTemplate.setType(TemplateType.H5.value());
        }
        shopTemplateService.save(shopTemplate);
        return ServerResponseEntity.success(shopTemplate.getTemplateId());
    }



    @PostMapping("/copyPC/{templateId}")
    @PreAuthorize("@pms.hasPermission('shop:shopTemplate:copyPC')")
    @ApiOperation(value = "PC端复制模板信息", notes = "PC端复制模板信息")
    public ServerResponseEntity<Long> copyTemplatePC(@PathVariable Long templateId){
        ShopTemplate shopTemplate = shopTemplateService.getById(templateId);
        shopTemplate.setTemplateId(null);
        shopTemplate.setName(shopTemplate.getName()+"副本");
        shopTemplate.setCreateTime(new Date());
        shopTemplate.setUpdateTime(new Date());
        shopTemplateService.save(shopTemplate);
        return ServerResponseEntity.success(shopTemplate.getTemplateId());
    }

    @PostMapping("/copyMove/{templateId}")
    @PreAuthorize("@pms.hasPermission('shop:shopTemplate:copyMove')")
    @ApiOperation(value = "移动端复制模板信息", notes = "移动端复制模板信息")
    public ServerResponseEntity<Long> copyTemplateMove(@PathVariable Long templateId){
        ShopTemplate shopTemplate = shopTemplateService.getById(templateId);
        shopTemplate.setTemplateId(null);
        shopTemplate.setName(shopTemplate.getName()+"副本");
        shopTemplate.setCreateTime(new Date());
        shopTemplate.setUpdateTime(new Date());
        shopTemplateService.save(shopTemplate);
        return ServerResponseEntity.success(shopTemplate.getTemplateId());
    }

}
