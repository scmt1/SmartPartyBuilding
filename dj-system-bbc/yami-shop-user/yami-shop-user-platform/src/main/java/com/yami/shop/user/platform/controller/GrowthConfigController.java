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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yami.shop.bean.param.GrowthParamConfig;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.bean.SysConfig;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.Json;
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
 * 成长值基本配置信息
 * @author lhd
 */
@Api(tags = "成长值基本配置信息")
@RestController
@RequestMapping("/user/GrowthConfig")
public class GrowthConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    @GetMapping("/info/{key}")
    @PreAuthorize("@pms.hasPermission('user:GrowthConfig:info')")
    @ApiOperation(value = "配置信息", notes = "配置信息")
    @ApiImplicitParam(name = "key", value = "参数名")
    public ServerResponseEntity<GrowthParamConfig> info(@PathVariable("key") String key){
        SysConfig config = sysConfigService.getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getParamKey,key));
        if(Objects.isNull(config)){
            return ServerResponseEntity.success();
        }
        GrowthParamConfig growthConfigParam = Json.parseObject(config.getParamValue(), GrowthParamConfig.class);
        return ServerResponseEntity.success(growthConfigParam);
    }

    @SysLog("保存配置")
    @PostMapping
    @ApiOperation(value = "保存配置", notes = "保存配置")
    @PreAuthorize("@pms.hasPermission('user:GrowthConfig:save')")
    public ServerResponseEntity<Void> save(@RequestBody @Valid GrowthParamConfig growthConfigParam){
        SysConfig config = new SysConfig();

        String paramValue = Json.toJsonString(growthConfigParam);
        config.setParamKey(Constant.GROWTH_CONFIG);
        config.setParamValue(paramValue);
        config.setRemark("成长值获取比例规则");
        if (sysConfigService.count(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getParamKey, config.getParamKey())) > 0) {
            sysConfigService.update(new LambdaUpdateWrapper<SysConfig>()
                    .set(SysConfig::getParamValue,config.getParamValue())
                    .set(SysConfig::getRemark,config.getRemark())
                    .eq(SysConfig::getParamKey,config.getParamKey()));
        }else {
            sysConfigService.save(config);
        }
        sysConfigService.removeSysConfig(Constant.GROWTH_CONFIG);
        return ServerResponseEntity.success();
    }

}
