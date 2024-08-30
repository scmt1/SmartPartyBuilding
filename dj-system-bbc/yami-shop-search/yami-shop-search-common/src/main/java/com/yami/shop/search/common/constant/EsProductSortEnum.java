/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.search.common.constant;

import org.elasticsearch.search.sort.SortOrder;


/**
 * 商品排序
 * @author FrozenWatermelon
 * @date 2020/11/17
 */
public enum EsProductSortEnum {

    /**
     * 创建时间正序
     */
    CREATE_TIME_ASC(0, EsConstant.CREATE_TIME, SortOrder.ASC),

    /**
     * 创建时间倒序
     */
    CREATE_TIME_DESC(1, EsConstant.CREATE_TIME, SortOrder.DESC),

    /**
     * 销量倒序
     */
    SALE_NUM_DESC(2, EsConstant.SOLD_NUM, SortOrder.DESC),

    /**
     * 销量正序
     */
    SALE_NUM_ASC(3, EsConstant.SOLD_NUM, SortOrder.ASC),

    /**
     * 商品价格倒序
     */
    PRICE_DESC(4, EsConstant.PRICE, SortOrder.DESC),

    /**
     * 商品价格正序
     */
    PRICE_ASC(5, EsConstant.PRICE, SortOrder.ASC),

    /**
     * 市场价倒序
     */
    MARKET_PRICE_DESC(7, EsConstant.ORI_PRICE, SortOrder.DESC),

    /**
     * 市场价正序
     */
    MARKET_PRICE_ASC(8, EsConstant.ORI_PRICE, SortOrder.ASC),

    /**
     * 商品库存倒序
     */
    STOCK_DESC(10, EsConstant.TOTAL_STOCKS, SortOrder.DESC),

    /**
     * 商品库存正序
     */
    STOCK_ASC(11, EsConstant.TOTAL_STOCKS, SortOrder.ASC),

    /**
     * 商品序号倒序
     */
    SEQ_DESC(12, EsConstant.SEQ, SortOrder.DESC),

    /**
     * 商品序号正序
     */
    SEQ_ASC(13, EsConstant.SEQ, SortOrder.ASC),

    /**
     * 评论数量倒序
     */
    COMMENT_NUM_DESC(14, EsConstant.COMMENT_NUM, SortOrder.DESC),

    /**
     * 评论数量正序
     */
    COMMENT_NUM_ASC(15, EsConstant.COMMENT_NUM, SortOrder.ASC),

    /**
     * 根据置顶状态排序
     */
    IS_TOP_DESC(16, EsConstant.IS_TOP, SortOrder.DESC),

    /**
     * 实际销量倒序
     */
    ACTUAL_SOLD_NUM_DESC(17, EsConstant.ACTUAL_SOLD_NUM, SortOrder.DESC),

    /**
     * 实际销量正序
     */
    ACTUAL_SOLD_NUM_ASC(18, EsConstant.ACTUAL_SOLD_NUM, SortOrder.ASC),

    /**
     * 注水销量倒序
     */
    WATER_SOLD_NUM_DESC(19, EsConstant.WATER_SOLD_NUM, SortOrder.DESC),

    /**
     * 注水销量正序
     */
    WATER_SOLD_NUM_ASC(20, EsConstant.WATER_SOLD_NUM, SortOrder.ASC),

    /**
     * 发布时间倒序
     */
    PUTAWAY_TIME_DESC(21, EsConstant.PUTAWAY_TIME, SortOrder.DESC),

    /**
     * 发布时间正序
     */
    PUTAWAY_TIME_ASC(22, EsConstant.PUTAWAY_TIME, SortOrder.ASC),

    ;

    private final Integer value;

    private final String param;

    private final SortOrder order;

    public Integer value() {
        return value;
    }

    EsProductSortEnum(Integer value, String param, SortOrder order) {
        this.value = value;
        this.param = param;
        this.order = order;
    }

    public String param() {
        return param;
    }

    public SortOrder order() {
        return order;
    }


//    public static Boolean isAsc(EsProductSortEnum esProductSortEnum) {
//        if (Objects.equals(esProductSortEnum.order(), Boolean.TRUE)) {
//            return Boolean.TRUE;
//        }
//        return Boolean.FALSE;
//    }
//    public static Boolean isDesc(EsProductSortEnum esProductSortEnum) {
//        if (Objects.equals(esProductSortEnum.order(), Boolean.FALSE)) {
//            return Boolean.TRUE;
//        }
//        return Boolean.FALSE;
//    }
}
