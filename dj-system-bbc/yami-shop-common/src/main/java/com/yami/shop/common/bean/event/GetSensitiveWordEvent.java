/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.bean.event;

import com.yami.shop.common.bean.SensitiveWord;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 获取敏感词配置的事件
 * @author Citrus
 * @date 2021/8/17 11:24
 */
@Data
@AllArgsConstructor
public class GetSensitiveWordEvent {

    /**
     * 配置key
     */
    private String key;

    /**
     * 敏感词
     */
    private SensitiveWord sensitiveWord;
}
