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
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Citrus
 * @date 2021/9/15 14:29
 */
@Data
public class TakeStockParam {
    /**
     * 盘点单号
     */
    private String takeStockNo;
    /**
     * 盘点状态 0已作废 1盘点中 2已完成
     */
    private Integer billStatus;
    /**
     * 制单人
     */
    private Long maker;
    /**
     * 盘点开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 盘点结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 商品名称
     */
    private String prodName;
    /**
     * 规格编码
     */
    private String partyCode;
}
