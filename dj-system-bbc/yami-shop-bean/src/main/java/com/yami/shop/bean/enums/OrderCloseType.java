/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.enums;

/**
 * @author 小懒虫
 * @date 2019/9/9 10:18
 */
public enum  OrderCloseType {

    /**
     * 超时未支付
     */
    OVERTIME(1),
    /**
     * 退款关闭
     */
    REFUND(2),

    /**
     * 买家取消
     */
    BUYER(4),

    /**
     * 已通过货到付款交易
     */
    DELIVERY(15),
    ;

    private Integer code;

    public Integer value() {
        return code;
    }

    OrderCloseType(Integer code) {
        this.code = code;
    }
}
