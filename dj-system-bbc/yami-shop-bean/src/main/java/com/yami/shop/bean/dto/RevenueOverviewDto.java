/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto;

import lombok.Data;

import java.util.List;

/**
 * 财务管理—营收概况
 *
 * @author SJL
 * @date 2020-08-17
 */
@Data
public class RevenueOverviewDto {

    public RevenueOverviewDto() {
        this.incomeAmount = 0.0;
        this.payoutAmount = 0.0;
        this.actualIncome = 0.0;
        this.incomeAmountRatio = 0.0;
        this.payoutAmount = 0.0;
        this.actualIncomeRatio = 0.0;
    }

    /**
     * 实际收入
     */
    private Double actualIncome;

    /**
     * 实际收入-环比
     */
    private Double actualIncomeRatio;

    /**
     * 收入金额
     */
    private Double incomeAmount;

    /**
     * 收入金额-环比
     */
    private Double incomeAmountRatio;

    /**
     * 支出金额
     */
    private Double payoutAmount;

    /**
     * 支出金额-环比
     */
    private Double payoutAmountRatio;

    private List<TrendData> trendDataList;


    /**
     * 趋势图数据
     */
    @Data
    public static class TrendData {
        private String currentDate;
        private Double incomeAmount;
        private Double payoutAmount;
        private Double actualIncome;

        public TrendData() {
            this.incomeAmount = 0.0;
            this.payoutAmount = 0.0;
            this.actualIncome = 0.0;
        }
    }
}
