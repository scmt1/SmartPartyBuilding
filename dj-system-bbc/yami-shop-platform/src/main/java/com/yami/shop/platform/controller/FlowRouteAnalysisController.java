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

import com.yami.shop.bean.dto.SankeyDto;
import com.yami.shop.bean.param.FlowRouteAnalysisParam;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.FlowRouteAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lgh on 2018/11/26.
 */
@RestController
@RequestMapping("/platform/flowRouteAnalysis")
@Api(tags = "用户访问路径")
public class FlowRouteAnalysisController {

    @Autowired
    private FlowRouteAnalysisService flowRouteAnalysisService;

    @GetMapping("/getRoutData")
    @ApiOperation(value = "分页获取用户访问路径数据", notes = "分页获取用户访问路径数据")
    public ServerResponseEntity<FlowRouteAnalysisParam> getRoutData(FlowRouteAnalysisParam flowRouteAnalysisParam) {
        flowRouteAnalysisParam.setTime();
        SankeyDto sankeyDto = flowRouteAnalysisService.getRoutData(flowRouteAnalysisParam);
        flowRouteAnalysisParam.setSankeyDto(sankeyDto);
        return ServerResponseEntity.success(flowRouteAnalysisParam);
    }
}
