/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import lombok.Data;

/**
 * @author Yami
 */
@Data
public class CustomerDealTrendParam {

    private String currentDay;

    /**
     * 客户数
     * 筛选时间内，付款成功的客户数，一人多次付款成功记为一人
     * 新成交客户数：过去2年没有购买，在筛选时间内首次在店铺付款的客户数量
     * 老成交客户数：过去2年购买过，在筛选时间内在店铺再次付款的客户数量
     */
    private Integer newCustomerNum = 0;
    private Integer oldCustomerNum = 0;

    /**
     * 客户数占比
     * 全部成交客户占比：筛选时间成交客户数 / 累积所有客户数
     * 新成交客户占比：筛选时间新成交客户数 / 全部成交客户数
     * 老成交客户占比：筛选时间老成交客户数 / 全部成交客户数
     */
//    private Double newCustomerRate = 0.0;
//    private Double oldCustomerRate = 0.0;

    /**
     * 客单价
     * 筛选时间内，付款金额 / 付款人数
     */
    private Double newCustomerSinglePrice = 0.0;
    private Double oldCustomerSinglePrice = 0.0;

    /**
     * 付款金额
     */
    private Double newPayAmount = 0.0;
    private Double oldPayAmount = 0.0;

    /**
     * 访问-付款转化率
     * 全部成交客户-访问-付款转化率：筛选时间内，全部成交客户数/店铺访客数
     * 新成交客户-访问-付款转化率：筛选时间内， 新成交客户数/店铺访客数中近2年无购买记录的访客数
     * 老成交客户-访问-付款转化率：筛选时间内，老成交客户数/店铺访客数中近2年购买过的访客数
     */
    private Double newTransRate = 0.0;
    private Double oldTransRate = 0.0;
}
