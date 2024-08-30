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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.dto.PageAnalysisDto;
import com.yami.shop.bean.param.FlowAnalysisParam;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.FlowPageAnalysisService;
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
@RequestMapping("/platform/flowPageAnalysis")
@Api(tags = "流量页面分析")
public class FlowPageAnalysisController {

    @Autowired
    private FlowPageAnalysisService flowPageAnalysisService;

    @GetMapping("/page")
    @ApiOperation(value = "分页获取页面统计数据", notes = "分页获取页面统计数据")
    public ServerResponseEntity<IPage<PageAnalysisDto>> page(PageParam<PageAnalysisDto> page, FlowAnalysisParam flowAnalysisParam) {
        return ServerResponseEntity.success(flowPageAnalysisService.getPageOrProdAnalysis(page, flowAnalysisParam));
    }
}
