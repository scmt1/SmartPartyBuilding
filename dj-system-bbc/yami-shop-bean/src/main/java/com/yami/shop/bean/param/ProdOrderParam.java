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

/**
 * @author lhd
 */
@Data
public class ProdOrderParam {
    /**
     * 订单项ID
     */
    private Long orderItemId;
    /**
     * 订单orderNumber
     */
    private String orderNumber;
    /**
     * 产品ID
     */
    private Long prodId;
    /**
     * 在平台当中的分类id
     */
    private Long categoryId;
    /**
     * 商品实际金额 = 商品总金额 - 分摊的优惠金额
     */
    private Double actualTotal;
}
