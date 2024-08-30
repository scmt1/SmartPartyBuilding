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
 * 页面编号（编号的顺序对数据没有影响）
 *
 */
/**
 * @author Yami
 */
public enum FlowVisitEnum {
    /** 访问页面*/
    VISIT(1),
    /** 分享页面 */
    SHARE(2),
    /** 点击页面 */
    CLICK(3),
    /** 加购 */
    SHOP_CAT(4);
    private Integer id;

    public Integer value() {
        return id;
    }

    FlowVisitEnum(Integer id){
        this.id = id;
    }

    public static FlowVisitEnum instance(String value) {
        FlowVisitEnum[] enums = values();
        for (FlowVisitEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static FlowVisitEnum[] allEnum() {
        FlowVisitEnum[] enums = values();
        return enums;
    }


    public static Boolean isVisitOrShare(Integer id){
        if (id.equals(VISIT.value()) || id.equals(SHARE.value())){
            return true;
        }
        return false;
    }
}
