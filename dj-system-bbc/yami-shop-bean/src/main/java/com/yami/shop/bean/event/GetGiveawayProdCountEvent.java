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
 * @Author lth
 * @Date 2021/11/12 10:56
 */
@Data
@AllArgsConstructor
public class GetGiveawayProdCountEvent {
    private Long prodId;

    /**
     * 赠品类型 1：主商品 2：赠送商品
     */
    private Integer giveawayProdType;

    private Integer count;

    public GetGiveawayProdCountEvent() {
        this.count = 0;
    }
}
