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

import cn.hutool.core.date.DateUtil;
import com.yami.shop.bean.dto.FlowAnalysisDto;
import com.yami.shop.bean.param.FlowAnalysisParam;
import com.yami.shop.bean.param.FlowUserAnalysisParam;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.platform.config.FlowUserAnalysisType;
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
import java.util.Date;
import java.util.Objects;

/**
 * @author lgh on 2018/11/26.
 */
@RestController
@RequestMapping("/platform/flowUserAnalysis")
@Api(tags = "用户数据分析")
public class FlowUserAnalysisController {

    @Autowired
    private FlowUserAnalysisService flowUserAnalysisService;

    @Autowired
    private FlowUserAnalysisExcelService flowUserAnalysisExcelService;

    @GetMapping("/getUserAnalysisData")
    @ApiOperation(value = "获取会员分析数据", notes = "获取会员分析数据")
    public ServerResponseEntity<FlowUserAnalysisParam> getUserAnalysisData(FlowUserAnalysisParam flowUserAnalysisParam) {
        handleTime(flowUserAnalysisParam);
        flowUserAnalysisService.getUserAnalysisData(flowUserAnalysisParam);
        return ServerResponseEntity.success(flowUserAnalysisParam);
    }


    @GetMapping("/userAnalysisDataExport")
    @PreAuthorize("@pms.hasPermission('user:analysis:data:export')")
    @ApiOperation(value = "导出会员分析数据-地图", notes = "导出会员分析数据-地图")
    public void userAnalysisDataExport(FlowUserAnalysisParam flowUserAnalysisParam, HttpServletResponse response) {
        handleTime(flowUserAnalysisParam);
        flowUserAnalysisExcelService.userAnalysisDataExport(flowUserAnalysisParam, response);
    }

    private void handleTime(FlowUserAnalysisParam flowUserAnalysisParam) {
        Integer type = flowUserAnalysisParam.getType();
        if (!Objects.equals(type,FlowUserAnalysisType.CUSTOM.value())){
            int day = 0;
            if (type == 1){
                day = -7;
            }else {
                day = -30;
            }
            Date endTime = DateUtil.beginOfDay(new Date());
            flowUserAnalysisParam.setEndTime(endTime);
            flowUserAnalysisParam.setStartTime(DateUtil.offsetDay(endTime,day));
            flowUserAnalysisParam.setStart(flowUserAnalysisParam.getStartTime().getTime());
            flowUserAnalysisParam.setEnd(DateUtil.offsetDay(endTime,-1).getTime());
        }else {
            if (Objects.isNull(flowUserAnalysisParam.getEnd()) || Objects.isNull(flowUserAnalysisParam.getStart())){
                flowUserAnalysisParam.setEndTime(DateUtil.endOfDay(new Date()));
                flowUserAnalysisParam.setStartTime(DateUtil.beginOfDay(new Date()));
            }else {
                flowUserAnalysisParam.setEndTime(DateUtil.endOfDay(new Date(flowUserAnalysisParam.getEnd())));
                flowUserAnalysisParam.setStartTime(new Date(flowUserAnalysisParam.getStart()));
            }
        }
    }
}
