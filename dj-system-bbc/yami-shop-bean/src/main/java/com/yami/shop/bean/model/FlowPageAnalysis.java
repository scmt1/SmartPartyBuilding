/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2020-07-14 11:26:35
 */
@Data
@TableName("tz_flow_page_analyse")
public class FlowPageAnalysis implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long flowPageAnalyseId;
    /**
     * 页面编号
     */
    private Integer pageId;
    /**
     * 系统类型 1:pc  2:h5  3:小程序 4:安卓  5:ios
     */
    private Integer systemType;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 业务数据
     */
    private String bizDate;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 访问次数
     */
    private Integer visis;
    /**
     * 点击次数
     */
    private Integer click;
    /**
     * 停留时长（秒）
     */
    private Long stopTime;
    /**
     * 分享访问次数
     */
    private Integer shareVisit;
    /**
     * 下单金额
     */
    private Double placeOrderAmount;
    /**
     * 支付金额
     */
    private Double payAmount;
    /**
     * 加购数
     */
    private Integer plusShopCart;
    /**
     * 1:页面数据  2:商品详情页
     */
    private Integer type;
}
