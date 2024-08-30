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


import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.enums.WebConfigTypeEnum;
import com.yami.shop.bean.model.WebConfig;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.bean.SysConfig;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 由于权限要求不同，所以不能把功能合并到sysController
 *
 * @author SJL
 * @date 2021-02-20 09:44:42
 * @description: 平台网站配置、商家网站配置、PC配置、H5配置、自提点网站配置
 */

@RestController
@RequestMapping("/sys/webConfig")
@Api(tags = "网站配置")
public class WebConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    @Value("${yami.expose.operation.auth:}")
    private Boolean permission;

    @SysLog("获取配置信息")
    @GetMapping("/info/{key}")
    @ApiOperation(value = "获取配置信息", notes = "获取配置信息")
    @ApiImplicitParam(name = "key", value = "参数名")
    public ServerResponseEntity<String> info(@PathVariable("key") String key) {
        return ServerResponseEntity.success(sysConfigService.getValue(key));
    }

    @SysLog("保存配置")
    @PostMapping("/save")
    @ApiOperation(value = "保存配置", notes = "保存配置")
    @PreAuthorize("@pms.hasPermission('sys:webConfig:save')")
    public ServerResponseEntity<Void> save(@RequestBody @Valid SysConfig sysConfig) {
        if (BooleanUtil.isFalse(permission)) {
            return ServerResponseEntity.fail(ResponseEnum.NOT_FOUND);
        }
        String paramValue = sysConfig.getParamValue();
        String paramKey = sysConfig.getParamKey();
        if (Objects.isNull(paramKey) || Objects.isNull(paramValue)) {
            throw new YamiShopBindException("参数不完整，请准确填写后重试");
        }
        int count = sysConfigService.count(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getParamKey, paramKey));
        if (count > 0) {
            sysConfigService.updateValueByKey(paramKey, paramValue);
        } else {
            sysConfigService.save(sysConfig);
            sysConfigService.removeSysConfig(paramKey);
        }
        return ServerResponseEntity.success();
    }

    /**
     * 获取当前激活的后台网站配置
     *
     * @return
     */
    @GetMapping("/getActivity")
    public ServerResponseEntity<WebConfig> getActivityWebConfig() {
        WebConfig webConfig  = sysConfigService.getSysConfigObject(WebConfigTypeEnum.PLATFROM.value(), WebConfig.class);
        webConfig.setParamKey(WebConfigTypeEnum.PLATFROM.value());
        return ServerResponseEntity.success(webConfig);
    }
}
