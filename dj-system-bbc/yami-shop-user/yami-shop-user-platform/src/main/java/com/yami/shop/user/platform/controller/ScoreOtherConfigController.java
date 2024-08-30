/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.bean.SysConfig;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;


/**
 * 其他积分配置
 * @author lhd
 */
@Api(tags = "其他积分配置")
@RestController
@RequestMapping("/user/scoreExplainConfig")
public class ScoreOtherConfigController {
    @Autowired
    private SysConfigService sysConfigService;

    @GetMapping("/info/{key}")
    @PreAuthorize("@pms.hasPermission('score:explainConfig:info')")
    @ApiOperation(value = "配置信息", notes = "配置信息")
    @ApiImplicitParam(name = "key", value = "参数名")
    public ServerResponseEntity<SysConfig> info(@PathVariable("key") String key){
        SysConfig config = sysConfigService.getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getParamKey,key));
        if(Objects.isNull(config)){
            return ServerResponseEntity.success(new SysConfig());
        }
        return ServerResponseEntity.success(config);
    }

    @SysLog("保存配置")
    @PostMapping
    @ApiOperation(value = "保存配置", notes = "保存配置")
    @PreAuthorize("@pms.hasPermission('score:explainConfig:save')")
    public ServerResponseEntity<Void> save(@RequestBody  SysConfig config){
        if (sysConfigService.count(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getParamKey, config.getParamKey())) > 0) {
            // 系统已存在相同配置的key
            throw new YamiShopBindException("yami.same.key");
        }
        sysConfigService.save(config);
        sysConfigService.removeSysConfig(config.getParamKey());
        return ServerResponseEntity.success();
    }

    @SysLog("修改配置")
    @PutMapping
    @ApiOperation(value = "修改配置", notes = "修改配置")
    @PreAuthorize("@pms.hasPermission('score:explainConfig:update')")
    public ServerResponseEntity<Void> update(@RequestBody @Valid SysConfig config){
        SysConfig dbSysConfig = sysConfigService.getOne(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getParamKey, config.getParamKey()));
        dbSysConfig.setParamValue(config.getParamValue());
        sysConfigService.updateById(dbSysConfig);
        sysConfigService.removeSysConfig(config.getParamKey());
        return ServerResponseEntity.success();
    }


}
