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

/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2020-07-14 11:26:35
 */
@Data
public class PageAnalysisDto {
    /**
     * 页面编号
     */
    private Integer pageId;
    /**
     * 页面名称
     */
    private String pageName;
    /**
     * 业务数据
     */
    private String bizDate;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 浏览量
     */
    private Integer visis;

    /**
     * 访客数
     */
    private Integer visitUser;

    /**
     * 点击次数
     */
    private Integer click;

    /**
     * 点击人数
     */
    private Integer clickUser;
    /**
     * 停留时长
     */
    private Integer stopTime;
    /**
     * 点击率
     */
    private Double clickRate;

    /**
     * 跳失率
     */
    private Double loseRate;

    /**
     * 平均停留时长（秒）
     */
    private Double averageStopTime;
    /**
     * 分享访问次数
     */
    private Integer shareVisit;
    /**
     * 分享访问人数
     */
    private Integer shareVisitUser;
    /**
     * 下单金额
     */
    private Double placeOrderAmount;
    /**
     * 下单人数
     */
    private Integer placeOrderUser;
    /**
     * 访问-下单转化率
     */
    private Double visitToPlaceOrderRate;
    /**
     * 支付金额
     */
    private Double payAmount;
    /**
     * 支付人数
     */
    private Integer payUser;
    /**
     * 访问-支付转化率
     */
    private Double visitToPayRate;
    /**
     * 加购
     */
    private Integer plusShopCart;
    /**
     * 加购人数
     */
    private Integer plusShopCartUser;


    public PageAnalysisDto(Integer pageId,String bizDate) {
        this.pageId = pageId;
        this.bizDate = bizDate;
        visis = 0;
        visitUser = 0;
        click = 0;
        clickUser = 0;
        clickRate = 0.0;
        loseRate = 0.0;
        averageStopTime = 0.0;
        shareVisit = 0;
        shareVisitUser = 0;
        placeOrderAmount = 0.0;
        placeOrderUser = 0;
        visitToPlaceOrderRate = 0.0;
        payAmount = 0.0;
        payUser = 0;
        visitToPayRate = 0.0;
        stopTime = 0;
        plusShopCart = 0;
        plusShopCartUser = 0;
    }
}
