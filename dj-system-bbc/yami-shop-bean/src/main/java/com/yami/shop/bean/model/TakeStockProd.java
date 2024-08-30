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

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 盘点商品列表
 *
 * @author Citrus
 * @date 2021-09-15 11:18:33
 */
@Data
@TableName("tz_take_stock_prod")
public class TakeStockProd implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 盘点商品id
     */
    @TableId
    private Long takeStockProdId;
    /**
     * 盘点id
     */
    @NotNull(message = "盘点id不能为空")
    private Long takeStockId;
    /**
     * 商品id
     */
    @NotNull(message = "商品id不能为空")
    private Long prodId;
    /**
     * skuId
     */
    @NotNull(message = "skuId不能为空")
    private Long skuId;
    /**
     * 账面库存
     */
    @NotNull(message = "账面库存不能为空")
    private Integer stocks;
    /**
     * 实盘库存
     */
    @NotNull(message = "实盘库存不能为空")
    private Integer totalStock;
    /**
     * 盈亏类型 0盘平 1盘盈 2盘亏 -1异常
     */
    private Integer ioType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 异常原因 1.删除商品 2.盘点期间有库存变动 3.其他
     */
    private Integer exceptionReason;
}
