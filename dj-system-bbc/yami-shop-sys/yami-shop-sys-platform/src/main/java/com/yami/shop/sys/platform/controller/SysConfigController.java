/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.platform.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.bean.SysConfig;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;


/**
 * 系统配置信息
 * @author lgh
 */
@Api(tags = "系统配置信息")
@ConditionalOnProperty(prefix = "yami", name = "expose.permission", havingValue = "true", matchIfMissing = true)
@RestController
@RequestMapping("/sys/config")
public class SysConfigController{
    @Autowired
    private SysConfigService sysConfigService;

    @GetMapping("/page")
    @ApiOperation(value = "所有配置列表", notes = "所有配置列表")
    @ApiImplicitParam(name = "paramKey", value = "参数名")
    @PreAuthorize("@pms.hasPermission('sys:config:page')")
    public ServerResponseEntity<IPage<SysConfig>> page(String paramKey,PageParam<SysConfig> page){
        IPage<SysConfig> sysConfigs = sysConfigService.page(page, new LambdaQueryWrapper<SysConfig>().like(StrUtil.isNotBlank(paramKey),SysConfig::getParamKey,paramKey));
        return ServerResponseEntity.success(sysConfigs);
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "配置信息", notes = "配置信息")
    @ApiImplicitParam(name = "id", value = "配置id")
    @PreAuthorize("@pms.hasPermission('sys:config:info')")
    public ServerResponseEntity<SysConfig> info(@PathVariable("id") Long id){
        SysConfig config = sysConfigService.getById(id);
        return ServerResponseEntity.success(config);
    }

    @SysLog("保存配置")
    @PostMapping
    @ApiOperation(value = "保存配置", notes = "保存配置")
    @PreAuthorize("@pms.hasPermission('sys:config:save')")
    public ServerResponseEntity<Void> save(@RequestBody @Valid SysConfig config){
        if (sysConfigService.count(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getParamKey, config.getParamKey())) > 0) {
            // 系统已存在相同配置的key
            throw new YamiShopBindException("yami.same.key");
        }
        sysConfigService.save(config);
        return ServerResponseEntity.success();
    }

    @SysLog("修改配置")
    @PutMapping
    @ApiOperation(value = "修改配置", notes = "修改配置")
    @PreAuthorize("@pms.hasPermission('sys:config:update')")
    public ServerResponseEntity<Void> update(@RequestBody @Valid SysConfig config){

        SysConfig dbSysConfig = sysConfigService.getOne(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getParamKey, config.getParamKey()));

        if (dbSysConfig != null && !Objects.equals(dbSysConfig.getParamKey(),config.getParamKey())) {
            // 系统已存在相同配置的key
            throw new YamiShopBindException("yami.same.key");
        }

        sysConfigService.updateById(config);
        return ServerResponseEntity.success();
    }

    @SysLog("删除配置")
    @DeleteMapping
    @ApiOperation(value = "删除配置", notes = "删除配置")
    @ApiImplicitParam(name = "configIds", value = "配置id列表")
    @PreAuthorize("@pms.hasPermission('sys:config:delete')")
    public ServerResponseEntity<Void> delete(@RequestBody Long[] configIds){
        sysConfigService.deleteBatch(configIds);
        return ServerResponseEntity.success();
    }

}
