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

import com.yami.shop.bean.app.dto.ShopCartOrderMergerDto;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  获取用户常用自提点
 */
/**
 * @author Yami
 */
@Data
@AllArgsConstructor
public class GetUserStationEvent {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 整个订单集合信息
     */
    ShopCartOrderMergerDto shopCartOrderMergerDto;
}
