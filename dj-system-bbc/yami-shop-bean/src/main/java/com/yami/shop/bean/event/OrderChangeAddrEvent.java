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

import com.yami.shop.bean.param.OrderChangeAddrParam;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 修改地址重算运费事件
 * @author Citrus
 * @date 2021/11/25 10:58
 */
@Data
@AllArgsConstructor
public class OrderChangeAddrEvent {

    private OrderChangeAddrParam order;

    /**
     * 更改的价格
     */
    private double changeAmount;
}
