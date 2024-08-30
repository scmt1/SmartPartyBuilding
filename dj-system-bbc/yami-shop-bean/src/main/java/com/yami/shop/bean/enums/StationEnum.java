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

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 秒杀状态枚举类型
 */
/**
 * @author Yami
 */
@Getter
@AllArgsConstructor
public enum StationEnum {
    /**    失效        */
    INVALID(0,"失效"),

    /**    正常        */
    NORMAL(1,"正常"),

    /**    违规下架    */
    OFFLINE(2,"违规下架"),

    /**    等待审核    */
    WAIT_AUDIT(3,"等待审核"),

    /**    审核失败    */
    AUDIT_FAIL(4,"审核失败"),
    ;
    private int value;
    private String desc;

}
