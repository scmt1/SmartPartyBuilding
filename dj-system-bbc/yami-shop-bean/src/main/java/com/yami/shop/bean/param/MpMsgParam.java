/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author lhd
 * @date 2020-07-01 15:44:27
 */
@Data
@AllArgsConstructor
public class MpMsgParam implements Serializable{

    /**
     *
     */
    private String[] keys;
    /**
     *
     */
    private String[] values;




}
