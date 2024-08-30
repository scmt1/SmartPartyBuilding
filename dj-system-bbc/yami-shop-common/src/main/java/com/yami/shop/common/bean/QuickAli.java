/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 阿里物流详情查询
 * @Author lth
 * @Date 2021/11/11 14:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuickAli extends SwitchBaseModel {

    /**
     * 阿里物流中的顺丰编号
     */
    public final static String SF_CODE = "SFEXPRESS";

    private String aliCode;
    private String reqUrl;
}
