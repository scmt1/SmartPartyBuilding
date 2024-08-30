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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.TemplateType;
import com.yami.shop.bean.model.ShopTemplate;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.ShopTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

/**
 * 
 *
 * @author LGH
 * @date 2022-07-29 16:54:02
 */
@RestController
@RequestMapping("/platform/shopTemplate")
@Api(tags = "店铺装修模板接口")
public class ShopTemplateController {

    @Autowired
    private ShopTemplateService shopTemplateService;

    @GetMapping("/pagePC")
    @PreAuthorize("@pms.hasPermission('platform:shopTemplate:pagePC')")
    @ApiOperation(value = "PC端分页查询店铺模板信息", notes = "PC端分页查询店铺模板信息")
    public ServerResponseEntity<IPage<ShopTemplate>> getShopTemplatePagePC(PageParam<ShopTemplate> page, ShopTemplate shopTemplate) {
        return ServerResponseEntity.success(shopTemplateService.page(page, new LambdaQueryWrapper<ShopTemplate>()
                .eq(ShopTemplate::getShopId, Constant.PLATFORM_SHOP_ID)
                .eq(Objects.nonNull(shopTemplate.getType()), ShopTemplate::getType, shopTemplate.getType())
                .eq(ShopTemplate::getType,shopTemplate.getType())
                .orderByDesc(ShopTemplate::getUpdateTime)
        ));
    }

    @GetMapping("/pageMove")
    @PreAuthorize("@pms.hasPermission('platform:shopTemplate:pageMove')")
    @ApiOperation(value = "移动端分页查询店铺模板信息", notes = "移动端分页查询店铺模板信息")
    public ServerResponseEntity<IPage<ShopTemplate>> getShopTemplatePageMove(PageParam<ShopTemplate> page, ShopTemplate shopTemplate) {
        return ServerResponseEntity.success(shopTemplateService.page(page, new LambdaQueryWrapper<ShopTemplate>()
                .eq(ShopTemplate::getShopId, Constant.PLATFORM_SHOP_ID)
                .eq(Objects.nonNull(shopTemplate.getType()), ShopTemplate::getType, shopTemplate.getType())
                .eq(ShopTemplate::getType,shopTemplate.getType())
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
        return ServerResponseEntity.success(shopTemplateService.getById(templateId));
    }

    @SysLog("新增店铺模板信息" )
    @PostMapping("/savePC")
    @PreAuthorize("@pms.hasPermission('platform:shopTemplate:savePC')")
    @ApiOperation(value = "PC端新增店铺模板信息", notes = "PC端新增店铺模板信息")
    public ServerResponseEntity<Long> savePC(@RequestBody @Valid ShopTemplate shopTemplate) {
        shopTemplate.setShopId(Constant.PLATFORM_SHOP_ID);
        if (Objects.isNull(shopTemplate.getType())) {
            shopTemplate.setType(TemplateType.PC.value());
        }
        shopTemplateService.save(shopTemplate);
        return ServerResponseEntity.success(shopTemplate.getTemplateId());
    }

    @SysLog("新增店铺模板信息" )
    @PostMapping("/saveMove")
    @PreAuthorize("@pms.hasPermission('platform:shopTemplate:saveMove')")
    @ApiOperation(value = "移动端新增店铺模板信息", notes = "移动端新增店铺模板信息")
    public ServerResponseEntity<Long> saveMove(@RequestBody @Valid ShopTemplate shopTemplate) {
        shopTemplate.setShopId(Constant.PLATFORM_SHOP_ID);
        if (Objects.isNull(shopTemplate.getType())) {
            shopTemplate.setType(TemplateType.H5.value());
        }
        shopTemplateService.save(shopTemplate);
        return ServerResponseEntity.success(shopTemplate.getTemplateId());
    }

    @PostMapping("/copyPC/{templateId}")
    @PreAuthorize("@pms.hasPermission('platform:shopTemplate:copyPC')")
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
    @PreAuthorize("@pms.hasPermission('platform:shopTemplate:copyMove')")
    @ApiOperation(value = "PC端复制模板信息", notes = "PC端复制模板信息")
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
