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

import com.yami.shop.bean.dto.FlowAnalysisDto;
import com.yami.shop.bean.dto.SystemDto;
import com.yami.shop.bean.param.FlowAnalysisParam;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.FlowUserAnalysisExcelService;
import com.yami.shop.service.FlowUserAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author lgh on 2018/11/26.
 */
@RestController
@RequestMapping("/platform/flowAnalysis")
@Api(tags = "流量概况")
public class FlowAnalysisController {

    @Autowired
    private FlowUserAnalysisService flowUserAnalysisService;

    @Autowired
    private FlowUserAnalysisExcelService flowUserAnalysisExcelService;

    @GetMapping("/getAnalysisData")
    @ApiOperation(value = "流量总览", notes = "流量总览")
    public ServerResponseEntity<FlowAnalysisDto> getAnalysisData(FlowAnalysisParam flowAnalysisParam) {
        //获取开始和结束时间
        flowAnalysisParam.setTime(1);
        FlowAnalysisDto flowAnalysisDto = flowUserAnalysisService.getFlowAnalysisData(flowAnalysisParam);
        return ServerResponseEntity.success(flowAnalysisDto);
    }

    @GetMapping("/analysisDataExport")
    @PreAuthorize("@pms.hasPermission('flow:data:export')")
    @ApiOperation(value = "导出流量总览", notes = "导出流量总览")
    public void analysisDataExport(FlowAnalysisParam flowAnalysisParam, HttpServletResponse response) {
        //获取开始和结束时间
        flowAnalysisParam.setTime(1);
        flowUserAnalysisExcelService.analysisDataExport(flowAnalysisParam, response);
    }

    @GetMapping("/flowTrend")
    @ApiOperation(value = "流量趋势", notes = "流量趋势")
    public ServerResponseEntity<List<FlowAnalysisDto>> flowTrend(FlowAnalysisParam flowAnalysisParam) {
        // 获取开始和结束时间
        flowAnalysisParam.setTime(2);
        List<FlowAnalysisDto> flowAnalysisDtoList = flowUserAnalysisService.flowTrend(flowAnalysisParam);
        return ServerResponseEntity.success(flowAnalysisDtoList);
    }


    @GetMapping("/flowTrendExport")
    @PreAuthorize("@pms.hasPermission('flow:trend:export')")
    @ApiOperation(value = "导出流量趋势", notes = "导出流量趋势")
    public void flowTrendExport(FlowAnalysisParam flowAnalysisParam, HttpServletResponse response) {
        //获取开始和结束时间
        flowAnalysisParam.setTime(2);
        flowUserAnalysisExcelService.flowTrendExport(flowAnalysisParam, response);
    }

    @GetMapping("/flowSour")
    @ApiOperation(value = "成交转换", notes = "成交转换")
    public ServerResponseEntity<List<FlowAnalysisDto>> flowSour(FlowAnalysisParam flowAnalysisParam) {
        flowAnalysisParam.setTime(1);
        List<FlowAnalysisDto> flowAnalysisDtoList = flowUserAnalysisService.flowSour(flowAnalysisParam);
        return ServerResponseEntity.success(flowAnalysisDtoList);
    }


    @GetMapping("/flowSourExport")
    @PreAuthorize("@pms.hasPermission('flow:sour:export')")
    @ApiOperation(value = "导出成交转换", notes = "导出成交转换")
    public void flowSourExport(FlowAnalysisParam flowAnalysisParam, HttpServletResponse response) {
        //获取开始和结束时间
        flowAnalysisParam.setTime(1);
        flowUserAnalysisExcelService.flowSour(flowAnalysisParam, response);
    }

    @GetMapping("/systemTypeNums")
    @ApiOperation(value = "系统访客数量", notes = "系统访客数量")
    public ServerResponseEntity<SystemDto> systemTypeNums(FlowAnalysisParam flowAnalysisParam) {
        flowAnalysisParam.setTime(1);
        SystemDto systemDto = flowUserAnalysisService.systemTypeNums(flowAnalysisParam);
        return ServerResponseEntity.success(systemDto);
    }
}
