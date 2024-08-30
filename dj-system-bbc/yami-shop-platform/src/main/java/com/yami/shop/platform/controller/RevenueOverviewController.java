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
import com.yami.shop.bean.dto.FinanceDetailsDto;
import com.yami.shop.bean.dto.RevenueOverviewDto;
import com.yami.shop.bean.param.FinanceDetailsParam;
import com.yami.shop.bean.param.RevenueOverviewParam;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.RevenueOverviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * 财务管理—营收概况
 *
 * @author SJL
 * @date 2020-08-17
 */

@RestController
@RequestMapping("/platform/financialManagement")
@Api(tags = "营收概况")
public class RevenueOverviewController {

    @Autowired
    private RevenueOverviewService revenueOverviewService;

    @GetMapping("/getIncomeProfile")
    @ApiOperation(value = "获取商家和日期的收入金额和退款金额", notes = "获取商家和日期的收入金额和退款金额")
    public ServerResponseEntity<RevenueOverviewDto> getIncomeProfile(RevenueOverviewParam param) throws ParseException {
        RevenueOverviewDto result = revenueOverviewService.getData(param);
        return ServerResponseEntity.success(result);
    }

    @GetMapping("/getFinanceDetail")
    @ApiOperation(value = "分页获取财务明细", notes = "分页获取财务明细")
    public ServerResponseEntity<IPage<FinanceDetailsDto>> getFinanceDetails(PageParam<FinanceDetailsDto> page, FinanceDetailsParam param) {
        IPage<FinanceDetailsDto> result = revenueOverviewService.getPageDetail(page, param);
        return ServerResponseEntity.success(result);
    }

    @GetMapping("/getFinanceDetailForm")
    @ApiOperation(value = "导出报表", notes = "导出报表")
    @PreAuthorize("@pms.hasPermission('finance:detail:excel')")
    public void getFinanceDetailForm(FinanceDetailsParam param, HttpServletResponse response) {
        revenueOverviewService.excelFianceDetail(param, response);
    }
}
