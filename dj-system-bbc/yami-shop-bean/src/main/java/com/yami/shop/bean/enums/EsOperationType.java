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
 * 配送类型
 */
/**
 * @author Yami
 */
public enum EsOperationType {

    /**
     * 保存
     */
    SAVE(1),
    /**
     * 更新
     */
    UPDATE(2),

    /**
     * 删除
     */
    DELETE(3),
    /**
     * 批量保存
     */
    SAVE_BATCH(4),
    /**
     * 批量更新
     */
    UPDATE_BATCH(5),
    /**
     * 批量删除
     */
    DELETE_BATCH(6),
    /**
     * 更新商品评论
     */
    UPDATE_PROD_COMM(7),
    /**
     * 更新商品销量
     */
    UPDATE_SOLD_NUM(8),
    /**
     * 根据分类id，更新商品
     */
    UPDATE_BY_CATEGORY_ID(9),
    /**
     * 根据店铺分类id，更新商品
     */
    UPDATE_BY_SHOP_CATEGORY_ID(10),
    /**
     * 根据店铺id，更新商品
     */
    UPDATE_BY_SHOP_ID(11),
    /**
     * 根据拼团活动id，更新商品
     */
    UPDATE_BY_GROUP_ID(12)
    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    EsOperationType(Integer num) {
        this.num = num;
    }

    public static EsOperationType instance(Integer value) {
        EsOperationType[] enums = values();
        for (EsOperationType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
