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
 * 商品状态枚举类型
 * <p>
 * 状态（-1表示删除 0:商家下架 1:上架 2:平台下架 3:违规下架待平台审核 4:审核失败 5：审核成功 6:待审核）
 */
/**
 * @author Yami
 */
public enum ProdStatusEnums {
    /**
     * 删除
     */
    DELETE(-1, "删除"),

    /**
     * 商家下架
     */
    SHOP_OFFLINE(0, "商家下架"),

    /**
     * 上架
     */
    NORMAL(1, "上架"),

    /**
     * 平台下架
     */
    PLATFORM_OFFLINE(2, "平台下架"),

    /**
     * 平台审核
     */
    PLATFORM_AUDIT(3, "违规下架待审核"),

    /**
     * 待审核[审核开关打开时，新发布的商品待审核]
     */
    AUDIT(6,"待审核"),
    ;

    private Integer value;
    private String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    ProdStatusEnums(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    public static ProdStatusEnums instance(Integer value) {
        ProdStatusEnums[] enums = values();
        for (ProdStatusEnums statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}

