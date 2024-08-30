/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo;

import com.yami.shop.bean.model.OrderItem;
import lombok.Data;

import java.util.List;

/**
 * @author Pineapple
 * @date 2021/7/21 17:33
 */
@Data
public class UnDeliveryOrderExcelVO {

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 配送方式[名称]
     */
    private String deliveryType;

    /**
     * 配送方式[数值,存入数据库]
     */
    private Integer dvyType;

    /**
     * 快递公司名称
     */
    private String dvyName;

    /**
     * 快递公司编号
     */
    private Long dvyId;

    /**
     * 快递单号
     */
    private String dvyFlowId;

    /**
     * 收货人姓名
     */
    private String receiver;

    /**
     * 收货人手机号
     */
    private String mobile;

    /**
     * 收货地址
     */
    private String receivingAddr;

    /**
     * 订单项列表
     */
    private List<OrderItem> orderItemList;
}
