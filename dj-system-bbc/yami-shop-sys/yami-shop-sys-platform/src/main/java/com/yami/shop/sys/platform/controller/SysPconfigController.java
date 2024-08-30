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


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.bean.SysConfig;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author chenlin
 * @DateTime: 2020/3/19 8:29
 * @description: 支付配置、文件上传配置、短信配置、快递配置、小程序配置、公众号配置
 */
@Api(tags = "系统配置")
@ConditionalOnProperty(prefix = "yami", name = "expose.permission", havingValue = "true", matchIfMissing = true)
@RestController
@RequestMapping("/sys/pconfig")
public class SysPconfigController {


    @Autowired
    private SysConfigService sysConfigService;

    @SysLog("获取配置信息")
    @GetMapping("/info/{key}")
    @ApiOperation(value = "获取配置信息", notes = "获取配置信息")
    @ApiImplicitParam(name = "key", value = "参数名")
    public ServerResponseEntity<String> info(@PathVariable("key")String key){
        return ServerResponseEntity.success(sysConfigService.getValue(key));
    }

    @SysLog("保存配置")
    @PostMapping("/save")
    @ApiOperation(value = "保存配置", notes = "保存配置")
    public ServerResponseEntity<Void> save(@RequestBody @Valid SysConfig sysConfig){
        String paramValue = sysConfig.getParamValue();
        String paramKey = sysConfig.getParamKey();
        int count = sysConfigService.count(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getParamKey,paramKey));
        if (count>0){
            sysConfigService.updateValueByKey(paramKey,paramValue);
        }else {
            sysConfigService.save(sysConfig);
        }
        sysConfigService.removeSysConfig(paramKey);
        return ServerResponseEntity.success();
    }

    @PostMapping("/saveDeliveryConfig")
    @ApiOperation(value = "保存物流查询配置信息", notes = "保存物流查询配置信息")
    public ServerResponseEntity<Void> saveDeliveryConfig(@RequestBody @Valid SysConfig sysConfig) {
        List<String> invalidKeys = sysConfigService.saveDeliveryConfig(sysConfig);
        invalidKeys.forEach(key -> {
            sysConfigService.removeSysConfig(key);
        });
        return ServerResponseEntity.success();
    }

    @PostMapping("/saveOssConfig")
    @ApiOperation(value = "保存文件上传配置信息", notes = "保存文件上传配置信息")
    public ServerResponseEntity<Void> saveOssConfig(@RequestBody @Valid SysConfig sysConfig) {
        List<String> invalidKeys = sysConfigService.saveOssConfig(sysConfig);
        invalidKeys.forEach(key -> {
            sysConfigService.removeSysConfig(key);
        });
        return ServerResponseEntity.success();
    }
}
