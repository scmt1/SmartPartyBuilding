/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author
 */
@Data
@AllArgsConstructor
public class UpdateSeckillOrderSkuInfoEvent {

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 商品skuId
     */
    private Long skuId;

    /**
     * 退款类型
     */
    private Integer refundType;

    private Integer prodCount;

    private Integer goodsNum;

}
