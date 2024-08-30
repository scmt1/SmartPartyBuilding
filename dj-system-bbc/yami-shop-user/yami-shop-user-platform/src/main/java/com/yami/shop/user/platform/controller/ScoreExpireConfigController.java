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
import com.yami.shop.bean.param.ScoreExpireParam;
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
 * 积分过期配置
 * @author lgh
 */
@Api(tags = "积分过期配置")
@RestController
@RequestMapping("/user/scoreExpire")
public class ScoreExpireConfigController {

    @Autowired
    private SysConfigService sysConfigService;


    /**
     * 配置信息
     */
    @GetMapping("/info/{key}")
    @PreAuthorize("@pms.hasPermission('user:scoreSonfig:info')")
    @ApiOperation(value = "配置信息", notes = "配置信息")
    @ApiImplicitParam(name = "key", value = "参数名")
    public ServerResponseEntity<ScoreExpireParam> info(@PathVariable("key") String key){
        SysConfig config = sysConfigService.getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getParamKey,key));
        if(Objects.isNull(config)){
            return ServerResponseEntity.success();
        }
        ScoreExpireParam expireParam = Json.parseObject(config.getParamValue(), ScoreExpireParam.class);
        return ServerResponseEntity.success(expireParam);
    }

    @SysLog("保存配置")
    @PostMapping
    @ApiOperation(value = "保存配置", notes = "保存配置")
    @PreAuthorize("@pms.hasPermission('user:GrowthConfig:save')")
    public ServerResponseEntity<Void> save(@RequestBody @Valid ScoreExpireParam expireParam){
        SysConfig config = new SysConfig();

        String paramValue = Json.toJsonString(expireParam);
        config.setParamKey(Constant.SCORE_EXPIRE);
        config.setParamValue(paramValue);
        config.setRemark("积分过期规则");
        if (sysConfigService.count(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getParamKey, config.getParamKey())) > 0) {
            sysConfigService.update(new LambdaUpdateWrapper<SysConfig>()
                    .set(SysConfig::getParamValue,config.getParamValue())
                    .set(SysConfig::getRemark,config.getRemark())
                    .eq(SysConfig::getParamKey,config.getParamKey()));
        }else {
            sysConfigService.save(config);
        }
        sysConfigService.removeSysConfig(Constant.SCORE_EXPIRE);
        return ServerResponseEntity.success();
    }

}
