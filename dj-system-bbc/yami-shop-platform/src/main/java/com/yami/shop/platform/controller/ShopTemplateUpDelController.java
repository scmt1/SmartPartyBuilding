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

import com.yami.shop.bean.model.ShopRenovation;
import com.yami.shop.bean.model.ShopTemplate;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.ShopTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix = "yami", name = "expose.operation.auth", havingValue = "true", matchIfMissing = true)
@Api(tags = "店铺模板-修改、删除、设为主页")
public class ShopTemplateUpDelController {

    @Autowired
    private ShopTemplateService shopTemplateService;

    @SysLog("修改店铺模板信息" )
    @PutMapping("/updatePC")
    @PreAuthorize("@pms.hasPermission('platform:shopTemplate:updatePC')")
    @ApiOperation(value = "PC端修改店铺模板信息", notes = "PC端修改店铺模板信息")
    public ServerResponseEntity<Boolean> updatePCById(@RequestBody @Valid ShopTemplate shopTemplate) {
        ShopTemplate shopTemplateDb = shopTemplateService.getById(shopTemplate.getTemplateId());
        if(!Objects.equals(shopTemplateDb.getShopId() , Constant.PLATFORM_SHOP_ID)){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        shopTemplate.setUpdateTime(new Date());
        return ServerResponseEntity.success(shopTemplateService.updateById(shopTemplate));
    }

    @SysLog("修改店铺模板信息" )
    @PutMapping("/updateMove")
    @PreAuthorize("@pms.hasPermission('platform:shopTemplate:updateMove')")
    @ApiOperation(value = "移动端修改店铺模板信息", notes = "移动端修改店铺模板信息")
    public ServerResponseEntity<Boolean> updateMoveById(@RequestBody @Valid ShopTemplate shopTemplate) {
        ShopTemplate shopTemplateDb = shopTemplateService.getById(shopTemplate.getTemplateId());
        if(!Objects.equals(shopTemplateDb.getShopId() , Constant.PLATFORM_SHOP_ID)){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        shopTemplate.setUpdateTime(new Date());
        return ServerResponseEntity.success(shopTemplateService.updateById(shopTemplate));
    }

    @SysLog("删除店铺模板信息" )
    @DeleteMapping("/deletePC/{templateId}")
    @PreAuthorize("@pms.hasPermission('platform:shopTemplate:deletePC')")
    @ApiOperation(value = "PC端通过id删除店铺模板信息", notes = "PC端通过id删除店铺模板信息")
    @ApiImplicitParam(name = "templateId", value = "店铺模板id")
    public ServerResponseEntity<Boolean> removePCById(@PathVariable Long templateId) {
        ShopTemplate shopTemplate = shopTemplateService.getById(templateId);
        if(!Objects.equals(shopTemplate.getShopId() ,Constant.PLATFORM_SHOP_ID)){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(shopTemplateService.removeById(templateId));
    }

    @SysLog("删除店铺模板信息" )
    @DeleteMapping("/deleteMove/{templateId}")
    @PreAuthorize("@pms.hasPermission('platform:shopTemplate:deleteMove')")
    @ApiOperation(value = "移动端通过id删除店铺模板信息", notes = "移动端通过id删除店铺模板信息")
    @ApiImplicitParam(name = "templateId", value = "店铺模板id")
    public ServerResponseEntity<Boolean> removeMoveById(@PathVariable Long templateId) {
        ShopTemplate shopTemplate = shopTemplateService.getById(templateId);
        if(!Objects.equals(shopTemplate.getShopId() ,Constant.PLATFORM_SHOP_ID)){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(shopTemplateService.removeById(templateId));
    }
}
