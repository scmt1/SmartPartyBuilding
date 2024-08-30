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

import com.yami.shop.common.util.Arith;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2020-07-14 11:26:35
 */
@Data
public class FlowPageAnalysisDto {
    /**
     *
     */
    private Long flowAnalysisPageId;
    /**
     * 系统类型 1:pc  2:h5  3:小程序 4:安卓  5:ios
     */
    private Integer systemType;
    /**
     * 页面编号
     */
    private Integer pageId;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 业务数据
     */
    private String bizDate;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 商品访问次数
     */
    private Integer visis;
    /**
     * 访问用户数
     */
    private Set<String> visitUser;
    /**
     * 商品页点击数
     */
    private Integer click;
    /**
     * 商品页点击人数
     */
    private Set<String> clickUser;
    /**
     * 停留时长（秒）
     */
    private Long stopTime;
    /**
     * 分享访问次数
     */
    private Integer shareVisit;
    /**
     * 分享访问人数
     */
    private Set<String> shareVisitUser;
    /**
     * 下单金额
     */
    private Double placeOrderAmount;
    /**
     * 下单人数
     */
    private Set<String> placeOrderUser;
    /**
     * 支付金额
     */
    private Double payAmount;
    /**
     * 支付人数
     */
    private Set<String> payUser;

    /**
     * 加购数
     */
    private Integer plusShopCart;
    /**
     * 加购人数
     */
    private Set<String> plusShopCartUser;
    /**
     * 1:页面数据  2:商品详情页
     */
    private Integer type;

    public FlowPageAnalysisDto() {
        visis = 0;
        visitUser = new HashSet<>();
        click = 0;
        clickUser = new HashSet<>();
        stopTime = 0L;
        shareVisit = 0;
        shareVisitUser = new HashSet<>();
        placeOrderAmount = 0.0;
        placeOrderUser =  new HashSet<>();
        payAmount = 0.0;
        payUser =  new HashSet<>();
        plusShopCart = 0;
        plusShopCartUser =  new HashSet<>();
    }

    public void statisticalData(FlowPageAnalysisDto flowPageAnalysisDto) {
        this.visis = this.visis + flowPageAnalysisDto.getVisis();
        this.visitUser.addAll(flowPageAnalysisDto.getVisitUser());
        this.click = this.click + flowPageAnalysisDto.getClick();
        this.clickUser.addAll(flowPageAnalysisDto.getClickUser());
        this.stopTime = this.stopTime + flowPageAnalysisDto.getStopTime();
        this.shareVisit = this.shareVisit + flowPageAnalysisDto.getShareVisit();
        this.shareVisitUser.addAll(flowPageAnalysisDto.getShareVisitUser());
        this.placeOrderAmount = Arith.add(this.placeOrderAmount,flowPageAnalysisDto.getPlaceOrderAmount());
        this.placeOrderUser.addAll(flowPageAnalysisDto.getPlaceOrderUser());
        this.payAmount = Arith.add(this.payAmount, flowPageAnalysisDto.getPayAmount());
        this.payUser.addAll(flowPageAnalysisDto.getPayUser());
        this.plusShopCart = this.plusShopCart + flowPageAnalysisDto.getPlusShopCart();
        this.plusShopCartUser.addAll(flowPageAnalysisDto.getPlusShopCartUser());
    }
}
