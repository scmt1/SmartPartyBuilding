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
 * @author Yami
 */
@Data
public class CustomerOrderParam {

    private Long orderId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 订购用户ID
     */
    private String userId;

    /**
     * 实际总值
     */
    private Double actualTotal;

    /**
     * 订单商品总数
     */
    private Integer productNums;
    /**
     * 付款时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;


}
