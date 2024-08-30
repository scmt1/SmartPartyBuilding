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

import java.util.Date;
import java.util.Objects;

/**
 * @author Yami
 */
@Data
public class FormExcelDto {


    /**
     * 时间（报表中显示的时间字符串）
     */
    private String formatTime;

    /**
     * 时间（日）
     */
    private Date infoTime;

    /**
     * 时间（周/月）
     */
    private String weedOrMonth;

    /**
     * 总金额
     */
    private Double totalTransactionAmount = 0.0;

    /**
     * 下单金额
     */
    private Double orderAmount = 0.0;
    /**
     * 下单笔数
     */
    private Integer orderNums = 0;

    /**
     * 下单商品数
     */
    private Integer productNums = 0;

    /**
     * 下单人数
     */
    private Integer userNums = 0;

    /**
     * 自营金额
     */
    private Double selfOperatedAmount = 0.0;

    /**
     * 自营订单数
     */
    private Integer selfOperatedOrderNums = 0;

    /**
     * 自营商品数
     */
    private Integer selfOperatedProductNums = 0;

    /**
     * 支付金额
     */
    private Double payAmount = 0.0;

    /**
     * 支付订单数
     */
    private Integer payOrderNums = 0;

    /**
     * 支付商品数
     */
    private Integer payProductNums = 0;

    /**
     * 支付人数
     */
    private Integer payUserNums = 0;

    /**
     * 退款金额
     */
    private Double refundAmount = 0.0;
    /**
     * 退款订单数
     */
    private Integer refundOrderNums = 0;

    public Double getRefundAmount(){
        if (Objects.isNull(refundAmount)){
            return 0.0;
        }
        return refundAmount;
    }

    public Integer getRefundOrderNums(){
        if (Objects.isNull(refundOrderNums)){
            return 0;
        }
        return refundOrderNums;
    }
}
