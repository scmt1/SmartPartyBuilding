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

/**
 * 出入库原因状态
 *
 * @author LGH
 * @date 2021-09-26 09:46:31
 */
@Data
@TableName("tz_stock_change_reason_status")
public class StockChangeReasonStatus implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long stockChangeReasonStatusId;
    /**
     * 关联的出入库原因id
     */
    private Long stockChangeReasonId;
    /**
     * 状态，1：启用 0：禁用 -1：删除
     */
    private Integer status;
    /**
     * 店铺id
     */
    private Long shopId;

}
