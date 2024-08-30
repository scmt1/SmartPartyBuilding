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

import java.util.List;

/**
 * @author Yami
 */
@Data
public class CustomerRespParam {

    /**
     * 访客数
     */
    private Integer visitor = 0;
    /**
     * 之前的访客数
     */
    private Integer preVisitor = 0;
    private Double visitorRate = 0.0;
    /**
     * 累积粉丝数
     */
    private Integer fashNum = 0;
    /**
     * 之前的累积粉丝数
     */
    private Integer preFashNum = 0;
    private Double fashNumRate = 0.0;
    /**
     * 累积会员数
     */
    private Integer member = 0;
    /**
     * 之前的累积会员数
     */
    private Integer preMember = 0;
    private Double memberRate = 0.0;
    /**
     * 成交客户数
     */
    private Integer payNum = 0;
    /**
     * 之前的成交客户数
     */
    private Integer prePayNum = 0;
    private Double payNumRate = 0.0;

    List<CustomerPayParam> customerTrend;

}
