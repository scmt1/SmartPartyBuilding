/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.api.controller;

import com.yami.shop.common.bean.SysPayConfig;
import com.yami.shop.common.bean.SysServiceConfig;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.i18n.LanguageEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 系统配置信息
 * @author yami
 */
@RestController
@Api(tags = "系统配置信息")
@RequestMapping("/sys/config")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    @ApiOperation(value = "获取服务条款信息", notes = "获取服务条款信息")
    @GetMapping("/info/serviceTerms")
    public ServerResponseEntity<String> serviceTerms(){
        Integer dbLang = I18nMessage.getDbLang();
        if(Objects.equals(LanguageEnum.LANGUAGE_EN.getLang(), dbLang)) {
            return ServerResponseEntity.success(sysConfigService.getValue("SERVICE_TERMS_CONFIG_EN"));
        }
        return ServerResponseEntity.success(sysConfigService.getValue("SERVICE_TERMS_CONFIG_CN"));
    }

    @ApiOperation(value = "获取隐私策略信息", notes = "获取隐私策略信息")
    @GetMapping("/info/servicePolicy")
    public ServerResponseEntity<String> servicePolicy(){
        Integer dbLang = I18nMessage.getDbLang();
        if(Objects.equals(LanguageEnum.LANGUAGE_ZH_CN.getLang(), dbLang)) {
            return ServerResponseEntity.success(sysConfigService.getValue("SERVICE_POLICY_CONFIG_CN"));
        }
        return ServerResponseEntity.success(sysConfigService.getValue("SERVICE_POLICY_CONFIG_EN"));
    }

    @ApiOperation(value = "获取系统支付开关", notes = "获取系统支付开关")
    @GetMapping("/info/getSysPaySwitch")
    public ServerResponseEntity<SysPayConfig> getSysPaySwitch(){
        SysPayConfig sysPayConfig = sysConfigService.getSysConfigObject(Constant.PAY_SWITCH_CONFIG, SysPayConfig.class);
        return ServerResponseEntity.success(sysPayConfig);
    }

    @ApiOperation(value = "获取系统服务条例开关", notes = "获取系统服务条例开关")
    @GetMapping("/info/getSysServiceSwitch")
    public ServerResponseEntity<SysServiceConfig> getSysServiceSwitch(){
        SysServiceConfig sysServiceConfig = sysConfigService.getSysConfigObject(Constant.SERVICE_SWITCH_CONFIG, SysServiceConfig.class);
        return ServerResponseEntity.success(sysServiceConfig);
    }
}
