/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 快递鸟物流详情查询
 * @author SJL
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Quick100 extends SwitchBaseModel {

    /**
     * 快递100中顺丰编码
     */
    @JsonIgnore
    public final static String SF_CODE = "shunfeng";
    /**
     * 快递100中丰网速运编码
     */
    @JsonIgnore
    public final static String FENGWANG_CODE = "fengwang";

    private String customer;
    private String key;
    private String reqUrl;
}
