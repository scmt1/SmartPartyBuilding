/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单自提信息
 *
 * @author lhd
 * @date 2021-09-15 15:45:35
 */
@Data
@TableName("tz_order_virtual_info")
public class OrderVirtualInfo implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 店铺id
     */
    private Long stationId;
    /**
     * 核销码
     */
    private String writeOffCode;

    /**
     * 剩余的多次核销次数
     */
    private Integer writeOffMultipleCount;

    /**
     * 是否核销 1.已核销 0.未核销
     */
    private Integer isWriteOff;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 核销时间
     */
    private Date writeOffTime;

}
