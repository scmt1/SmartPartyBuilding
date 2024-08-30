/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo.statistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 平台基本信息统计
 * @author Yami
 */
@Data
public class PlatformStatisticsVO {

    @ApiModelProperty("基本信息-商品数量")
    private Long prodNum;

    @ApiModelProperty("基本信息-商品数量提升比例")
    private Double prodNumRatio;

    @ApiModelProperty("基本信息-会员数量")
    private Long userNum;

    @ApiModelProperty("基本信息-会员数量提升比例")
    private Double userNumRatio;

    @ApiModelProperty("基本信息-订单数量")
    private Long orderNum;

    @ApiModelProperty("基本信息-订单数量提升比例")
    private Double orderNumRatio;

    @ApiModelProperty("基本信息-店铺数量")
    private Long shopNum;

    @ApiModelProperty("基本信息-店铺数量提升比例")
    private Double shopNumRatio;

    @ApiModelProperty("基本信息-退单数量")
    private Long refundNum;

    @ApiModelProperty("基本信息-退单数量提升比例")
    private Double refundNumRatio;




    @ApiModelProperty("今日待办-商品违规审核数量")
    private Long prodAuditNum;

    @ApiModelProperty("今日待办-待审核店铺")
    private Long shopAuditNum;

    @ApiModelProperty("今日待办-待审核分销体现")
    private Long distributionAuditNum;

    @ApiModelProperty("今日待办-待审核提现")
    private Long withdrawalAuditNum;





    @ApiModelProperty("实时概况-今日订单数")
    private Long currentOrderNum;

    @ApiModelProperty("实时概况-今日新增会员数")
    private Long currentUserNum;

    @ApiModelProperty("实时概况-今日新增店铺数")
    private Long currentShopNum;

    @ApiModelProperty("实时概况-今日交易额")
    private Double currentPayAmount;

    @ApiModelProperty("实时概况-今日新增商品数")
    private Long currentProdNum;

    @ApiModelProperty("实时概况-今日新增评论数")
    private Long currentProdCommNum;

    @ApiModelProperty("实时概况-今日访客数")
    private Long todayVisitors;

    @ApiModelProperty("实时概况-昨日访客数")
    private Long yesterdayVisitors;

    @ApiModelProperty("实时概况-前七天访客数")
    private Long firstSevenDayVisitors;

    @ApiModelProperty("实时概况-前30天访客数")
    private Long firstThirtyDayVisitors;

    @ApiModelProperty("热卖店铺列表")
    private List<HotStatisticsVO> hotShopList;

    @ApiModelProperty("热卖商品列表")
    private List<HotStatisticsVO> hotProdList;

    @ApiModelProperty("趋势列表")
    private List<TrendStatisticsVO> trendStatisticsList;

}
