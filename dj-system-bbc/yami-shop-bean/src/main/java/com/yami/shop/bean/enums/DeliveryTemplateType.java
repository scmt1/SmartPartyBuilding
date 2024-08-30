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
 * 商品物流选择
 */

import java.util.Objects;

/**
 * @author Yami
 */
public enum DeliveryTemplateType {

    /**
     * 统一包邮
     */
    FREE_SHIPPING(0L, "包邮"),
    /**
     * 统一运费
     */
    FREIGHT(-1L, "固定运费"),
    ;

    private Long value;

    private String name;

    public Long getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    DeliveryTemplateType(Long value, String name) {
        this.value = value;
        this.name = name;
    }

    public static boolean isUnifiedTemplate(Long value){
        DeliveryTemplateType[] enums = values();
        for (DeliveryTemplateType deliveryTemplateType : enums) {
            if (Objects.equals(deliveryTemplateType.getValue(), value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSysTemplate(String name) {
        return Objects.equals(name, FREE_SHIPPING.getName()) || Objects.equals(name, FREIGHT.getName());
    }
}



