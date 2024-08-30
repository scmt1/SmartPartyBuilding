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

import com.google.common.collect.Lists;
import com.yami.shop.common.util.Arith;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2020-07-14 11:26:35
 */
@Data
public class FlowAnalysisDto {
    /**
     * 日期时间
     */
    private String dateTime;
    /**
     * 时间(周、月)
     */
    private Integer createDate;
    /**
     * 时间(日)
     */
    private Date createTime;
    /**
     *UV价值 (支付金额／访客数)
     */
    private Double uVPrice;
    /**
     *客单价
     */
    private Double customerPrice;
    /**
     * 流失用户
     */
    private Integer lossUser;
    /**
     * 流失率
     */
    private Double lossUserRate;
    /**
     * 访客数
     */
    private Integer userNums;
    /**
     * 访客数占比
     */
    private Double userNumsRatio;
    /**
     * 加购数量
     */
    private Integer plusShopCart;
    /**
     * 加购人数
     */
    private Integer plusShopCartUser;
    /**
     * 下单金额
     */
    private Double placeOrderAmount;
    /**
     * 下单用户
     */
    private Integer placeOrderUser;
    /**
     * 访问-下单率
     */
    private Double placeOrderRate;
    /**
     * 支付金额
     */
    private Double payAmount;
    /**
     * 支付用户
     */
    private Integer payUser;
    /**
     * 支付率
     */
    private Double payRate;
    /**
     * 访问量
     */
    private Integer visitNums;
    /**
     * 人均访问量
     */
    private Double averageVisitNums;
    /**
     * 停留时间
     */
    private Integer stopTime;
    /**
     * 平均停留时间
     */
    private Double averageStopTime;
    /**
     * 新访客数
     */
    private Integer newUser;
    /**
     * 数据类型 1：新数据  2：旧数据
     */
    private Integer dataType;
    /**
     * 系统类型 0:全部 1:pc  2:h5  3:小程序 4:安卓  5:ios
     */
    private Integer systemType;

    private List<FlowAnalysisDto> flowAnalysisDtoList;

    private Ratio ratio;

    @Data
    public static class Ratio{
        /**
         * 流失率升降比例
         */
        private Double lossUserRatio;
        /**
         * 流失率上升
         */
        private Boolean lossUserRatioRise;
        /**
         * 访客数升降比例
         */
        private Double userNumsRatio;
        /**
         * 访客数上升
         */
        private Boolean userNumsRatioRise;
        /**
         * 访问量升降比例
         */
        private Double visitNumsRatio;
        /**
         * 访问量上升
         */
        private Boolean visitNumsRatioRise;
        /**
         * 人均访问量升降比例
         */
        private Double averageVisitNumsRatio;
        /**
         * 人均访问量上升
         */
        private Boolean averageVisitNumsRatioRise;
        /**
         * 平均停留时间升降比例
         */
        private Double averageStopTimeRatio;
        /**
         * 平均停留时间上升
         */
        private Boolean averageStopTimeRatioRise;
        /**
         * 新访客数升降比例
         */
        private Double newUserRatio;
        /**
         * 新访客数上升
         */
        private Boolean newUserRatioRise;
        /**
         * 支付用户升降比例
         */
        private Double payUserRatio;
        /**
         * 支付用户升降上升
         */
        private Boolean payUserRatioRise;
        /**
         * 支付率升降比例
         */
        private Double payRateRatio;
        /**
         * 支付率升降上升
         */
        private Boolean payRateRatioRise;
        public Ratio(){
            newUserRatio = 0.0;
            payUserRatio = 0.0;
            payRateRatio = 0.0;
            userNumsRatio = 0.0;
            lossUserRatio = 0.0;
            visitNumsRatio = 0.0;
            averageStopTimeRatio = 0.0;
            averageVisitNumsRatio = 0.0;
            newUserRatioRise = true;
            payUserRatioRise = true;
            payRateRatioRise = true;
            userNumsRatioRise = true;
            lossUserRatioRise = true;
            visitNumsRatioRise = true;
            averageStopTimeRatioRise = true;
            averageVisitNumsRatioRise = true;
        }

        public Double calculateProportion(Double newNum,Double oldNum){
            if (newNum.equals(0.0) && oldNum.equals(0.0)){
                return 0.0;
            }else if (newNum.equals(0.0) && oldNum > 0.0){
                return -100.0;
            }else if (newNum > 0.0 && oldNum.equals(0.0)){
                return 0.0;
            }
            double sub = Arith.sub(newNum, oldNum);
            return Arith.mul(Arith.div(sub,oldNum,2),100);
        }
    }

    public FlowAnalysisDto(){
        uVPrice = 0.0;
        customerPrice = 0.0;
        lossUser = 0;
        lossUserRate = 0.0;
        userNums = 0;
        plusShopCart = 0;
        plusShopCartUser = 0;
        placeOrderAmount = 0.0;
        placeOrderUser = 0;
        placeOrderRate = 0.0;
        payAmount = 0.0;
        payUser = 0;
        payRate = 0.0;
        visitNums = 0;
        averageVisitNums = 0.0;
        stopTime = 0;
        averageStopTime = 0.0;
        newUser = 0;
        flowAnalysisDtoList = Lists.newArrayList();
    }


}
