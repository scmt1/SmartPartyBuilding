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


import com.yami.shop.bean.enums.WebConfigTypeEnum;
import com.yami.shop.bean.model.WebConfig;
import com.yami.shop.common.bean.SysServiceConfig;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.SysConfigService;
import com.yami.shop.service.WebConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SJL
 * @date 2021-02-20 09:44:42
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys/webConfig")
@Api(tags = "系统配置接口")
public class WebConfigController {

    private final SysConfigService sysConfigService;


    /**
     * 获取当前激活的商家端网站配置
     *
     * @return
     */
    @GetMapping("/getActivity")
    @ApiOperation(value = "获取当前激活的商家端网站配置", notes = "获取当前激活的商家端网站配置")
    public ServerResponseEntity<WebConfig> getActivityWebConfig() {
        WebConfig webConfig = sysConfigService.getSysConfigObject(WebConfigTypeEnum.MULTISHOP.value(), WebConfig.class);
        SysServiceConfig sysConfigObject = sysConfigService.getSysConfigObject(Constant.SERVICE_SWITCH_CONFIG, SysServiceConfig.class);
        webConfig.setMerchantRegisterProtocolSwitch(sysConfigObject.getMerchantRegisterProtocolSwitch());
        return ServerResponseEntity.success(webConfig);
    }
}
