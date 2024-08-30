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
 * @date 2020-07-16 16:23:53
 */
@Data
@TableName("tz_flow_page_analyse_user")
public class FlowPageAnalyseUser implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long flowPageAnalyseUserId;
    /**
     * 页面分析id
     */
    private Long flowPageAnalyseId;
    /**
     * uuid
     */
    private String uuid;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 页面编号
     */
    private Integer pageId;
    /**
     * 业务数据
     */
    private String bizDate;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 用户访问
     */
    private Integer isVisit;
    /**
     * 用户点击
     */
    private Integer isClick;
    /**
     * 用户分享访问
     */
    private Integer isShareVisit;
    /**
     * 用户下单
     */
    private Integer isPlaceOrder;
    /**
     * 用户支付
     */
    private Integer isPay;
    /**
     * 加购数
     */
    private Integer isPlusShopCart;
    /**
     * 1:页面数据  2:商品详情页
     */
    private Integer type;

}
