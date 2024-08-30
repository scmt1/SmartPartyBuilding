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

import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
public class FormExcelParam{


    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 报表名称
     */
    private String formName;


    /**
     * 时间周期 1:天 2:周 3:月
     */
    private Integer timeType;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 时间范围 1:近1天 2:近7天 3: 近30天
     */
    private Integer timeRamge;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 总金额
     */
    private Boolean totalTransactionAmount;

    /**
     * 下单金额
     */
    private Boolean orderAmount;
    /**
     * 下单笔数
     */
    private Boolean orderNums;

    /**
     * 下单商品数
     */
    private Boolean productNums;

    /**
     * 下单人数
     */
    private Boolean userNums;

    /**
     * 自营金额
     */
    private Boolean selfOperatedAmount;

    /**
     * 自营订单数
     */
    private Boolean selfOperatedOrderNums;

    /**
     * 自营商品数
     */
    private Boolean selfOperatedProductNums;

    /**
     * 支付金额
     */
    private Boolean payAmount;

    /**
     * 支付订单数
     */
    private Boolean payOrderNums;

    /**
     * 支付商品数
     */
    private Boolean payProductNums;

    /**
     * 支付人数
     */
    private Boolean payUserNums;

    /**
     * 退款金额
     */
    private Boolean refundAmount;
    /**
     * 退款订单数
     */
    private Boolean refundOrderNums;


    /**
     * 报表项id集合
     */
    private List<Integer> formItemIdList;

    /**
     * 报表项名称数组
     */
    private String[] formItemNameList;
}
