/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 签约分类失效事件
 *
 * @Author lth
 * @Date 2021/9/6 13:44
 */
@Data
@AllArgsConstructor
public class SigningCategoryInvalidEvent {
    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 店铺id
     */
    private Long shopId;
}
