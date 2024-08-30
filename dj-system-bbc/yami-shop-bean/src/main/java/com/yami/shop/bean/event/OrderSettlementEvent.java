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

import com.yami.shop.bean.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


/**
 * 订单结算的事件，1.结算钱给商家，2.结算给分销员
 * @author lhd
 */
@Data
@AllArgsConstructor
public class OrderSettlementEvent {

    private List<Order> order;
}
