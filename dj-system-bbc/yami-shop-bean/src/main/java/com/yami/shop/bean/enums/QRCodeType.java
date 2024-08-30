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
 * 二维码类型
 * @author yami
 */
public enum QRCodeType {

    /** 小程序团购商品*/
    GROUP(1),

    /** 小程序分销商品二维码*/
    DISTRIBUTION(2)
    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    QRCodeType(Integer num){
        this.num = num;
    }

    public static QRCodeType instance(Integer value) {
        QRCodeType[] enums = values();
        for (QRCodeType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
