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

import com.yami.shop.bean.model.ShopTemplate;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.multishop.util.SecurityUtils;
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
@RequestMapping("/shop/shopTemplate")
@ConditionalOnProperty(prefix = "yami", name = "expose.operation.auth", havingValue = "true", matchIfMissing = true)
@Api(tags = "店铺模板-修改、删除、设为主页")
public class ShopTemplateUpDelController {

    @Autowired
    private ShopTemplateService shopTemplateService;

    /**
     * 修改店铺模板信息
     * @param shopTemplate 店铺模板信息
     * @return 是否修改成功
     */
    @PutMapping("/updatePC")
    @PreAuthorize("@pms.hasPermission('shop:shopTemplate:updatePC')")
    @ApiOperation(value = "PC端修改店铺模板信息", notes = "PC端修改店铺模板信息")
    public ServerResponseEntity<Boolean> updatePCById(@RequestBody @Valid ShopTemplate shopTemplate) {
        ShopTemplate shopTemplateDb = shopTemplateService.getById(shopTemplate.getTemplateId());
        if (Objects.isNull(shopTemplateDb)) {
            //查找不到该模板信息
            throw new YamiShopBindException("yami.template.not.exist");
        }
        if(!Objects.equals(shopTemplateDb.getShopId() , SecurityUtils.getShopUser().getShopId())){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        shopTemplate.setUpdateTime(new Date());
        return ServerResponseEntity.success(shopTemplateService.updateById(shopTemplate));

    }

    /**
     * 修改店铺模板信息
     * @param shopTemplate 店铺模板信息
     * @return 是否修改成功
     */
    @PutMapping("/updateMove")
    @PreAuthorize("@pms.hasPermission('shop:shopTemplate:updateMove')")
    @ApiOperation(value = "移动端修改店铺模板信息", notes = "移动端修改店铺模板信息")
    public ServerResponseEntity<Boolean> updateMoveById(@RequestBody @Valid ShopTemplate shopTemplate) {
        ShopTemplate shopTemplateDb = shopTemplateService.getById(shopTemplate.getTemplateId());
        if (Objects.isNull(shopTemplateDb)) {
            //查找不到该模板信息
            throw new YamiShopBindException("yami.template.not.exist");
        }
        if(!Objects.equals(shopTemplateDb.getShopId() , SecurityUtils.getShopUser().getShopId())){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        shopTemplate.setUpdateTime(new Date());
        return ServerResponseEntity.success(shopTemplateService.updateById(shopTemplate));

    }


    /**
     * 通过id删除店铺模板信息
     * @param templateId id
     * @return 是否删除成功
     */
    @DeleteMapping("/deletePC/{templateId}")
    @PreAuthorize("@pms.hasPermission('shop:shopTemplate:deletePC')")
    @ApiOperation(value = "PC端通过id删除店铺模板信息", notes = "PC端通过id删除店铺模板信息")
    @ApiImplicitParam(name = "templateId", value = "模板id")
    public ServerResponseEntity<Boolean> removePCById(@PathVariable Long templateId) {
        ShopTemplate shopTemplate = shopTemplateService.getById(templateId);
        if(!Objects.equals(shopTemplate.getShopId() ,SecurityUtils.getShopUser().getShopId())){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(shopTemplateService.removeById(templateId));
    }

    /**
     * 通过id删除店铺模板信息
     * @param templateId id
     * @return 是否删除成功
     */
    @DeleteMapping("/deleteMove/{templateId}")
    @PreAuthorize("@pms.hasPermission('shop:shopTemplate:deleteMove')")
    @ApiOperation(value = "移动端通过id删除店铺模板信息", notes = "移动端通过id删除店铺模板信息")
    @ApiImplicitParam(name = "templateId", value = "模板id")
    public ServerResponseEntity<Boolean> removeMoveById(@PathVariable Long templateId) {
        ShopTemplate shopTemplate = shopTemplateService.getById(templateId);
        if(!Objects.equals(shopTemplate.getShopId() ,SecurityUtils.getShopUser().getShopId())){
            // 没有权限进行操作
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(shopTemplateService.removeById(templateId));
    }
}
