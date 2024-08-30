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
 * @Author lth
 * @Date 2021/9/9 15:49
 */
public enum StockBillType {
    /**
     * 采购入库
     */
    PURCHASE_STORAGE(1, "采购入库","Purchasing Inbound"),
    /**
     * 退货入库
     */
    RETURN_STORAGE(2, "退款入库","Refunds to the Treasury"),
    /**
     * 其他入库
     */
    OTHER_STORAGE(3, "其他入库","Other inbound"),
    /**
     * 销售出库
     */
    PURCHASE_OUTBOUND(4, "销售出库","Sales out"),
    /**
     * 编辑出库
     */
    EDIT_OUTBOUND(5, "编辑出库","Edit Out"),
    /**
     * 其他出库
     */
    OTHER_OUTBOUND(6, "其他出库","Other Outbound"),
    /**
     * 库存初始化
     */
    INITIALIZE(7, "库存初始化","Inventory initialization"),

    /**
     * 订单取消
     */
    ORDER_CANCEL(8, "订单取消","Order Cancel"),

    /**
     * 编辑入库
     */
    EDIT_STORAGE(9, "编辑入库","Edit In"),

    /**
     * 盘盈入库
     */
    PROFIT_STORAGE(10, "盘盈入库","Inventory"),

    /**
     * 盘亏出库
     */
    LOSS_OUTBOUND(11, "盘亏出库","Inventory out")
    ;

    private final Integer id;

    private final String remark;

    private final String remarkEn;

    public Integer value() {
        return id;
    }

    public String text() {
        return remark;
    }

    public String textEn() {
        return remarkEn;
    }

    StockBillType(Integer id, String remark, String remarkEn){
        this.id = id;
        this.remark = remark;
        this.remarkEn = remarkEn;
    }

    public static StockBillType instance(Integer value) {
        StockBillType[] enums = values();
        for (StockBillType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
