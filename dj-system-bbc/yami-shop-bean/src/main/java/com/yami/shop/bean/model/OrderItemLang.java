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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author lhd
 * @date 2020-08-31 13:54:58
 */
@Data
@TableName("tz_order_item_lang")
public class OrderItemLang implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.INPUT)
    private Long orderItemId;
    /**
     *
     */
    private Integer lang;
    /**
     * 商品名称
     */
    private String prodName;
    /**
     * sku名称
     */
    private String skuName;

}
