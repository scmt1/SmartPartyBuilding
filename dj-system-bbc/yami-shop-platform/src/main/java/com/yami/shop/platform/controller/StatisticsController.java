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

import com.yami.shop.bean.vo.statistics.HotStatisticsVO;
import com.yami.shop.bean.vo.statistics.PlatformStatisticsVO;
import com.yami.shop.bean.vo.statistics.TrendStatisticsVO;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author chiley
 * @create 2022/5/16 15:34
 */
@RestController
@RequestMapping("/platform/statistics")
@AllArgsConstructor
@Api(tags = "平台统计信息")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/getPlatformStatistics")
    @ApiOperation(value = "获取主页基本信息、今日待办")
    public ServerResponseEntity<PlatformStatisticsVO> getPlatformStatistics() {
        return ServerResponseEntity.success(statisticsService.getPlatformStatistics());
    }

    @GetMapping("/platformRealTimeOverview")
    @ApiOperation(value = "获取主页实时概括")
    @ApiImplicitParam(name = "startTime", value = "开始时间")
    public ServerResponseEntity<PlatformStatisticsVO> platformRealTimeOverview(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("startTime") Date startTime) {
        return ServerResponseEntity.success(statisticsService.platformRealTimeOverview(startTime));
    }

    @GetMapping("/prod")
    @ApiOperation(value = "获取某段时间内的商品销量排行")
    public ServerResponseEntity<List<HotStatisticsVO>> getHotProds(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("startTime") Date startTime,
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("endTime") Date endTime) {

        return ServerResponseEntity.success(statisticsService.loadHotProdByDate(startTime, endTime));
    }


    @GetMapping("/shop")
    @ApiOperation(value = "获取某段时间内的店铺销量排行")
    public ServerResponseEntity<List<HotStatisticsVO>> getHotShops(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("startTime") Date startTime,
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("endTime") Date endTime) {


        return ServerResponseEntity.success(statisticsService.loadHotShopByDate(startTime, endTime));
    }

    @GetMapping("/trendData")
    @ApiOperation(value = "获取某段时间内的交易数据")
    public ServerResponseEntity<List<TrendStatisticsVO>> getTrendData(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("startTime") Date startTime,
                                                                @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("endTime") Date endTime) {


        return ServerResponseEntity.success(statisticsService.loadDataTrend(null, startTime, endTime));
    }


}
