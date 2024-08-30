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

import com.yami.shop.bean.enums.ShopStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 店铺下线事件
 *
 * @Author lth
 * @Date 2021/11/22 14:59
 */
@Data
@AllArgsConstructor
public class ShopChangeStatusEvent {

    private Long shopId;

    private ShopStatus shopStatus;
}
