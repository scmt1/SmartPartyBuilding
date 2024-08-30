/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.constants;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
public interface OrderCacheNames {
    /**
     *
     * 参考CacheKeyPrefix
     * cacheNames 与 key 之间的默认连接字符
     */
    String UNION = "::";


    /**
     * 普通确认订单信息缓存
     */
    String ORDER_CONFIRM_KEY = "order:confirm:";

    /**
     * 普通订单uuid
     */
    String ORDER_CONFIRM_UUID_KEY = "order:uuid_confirm:";

    /**
     * 拼团确认订单信息缓存
     */
    String GROUP_ORDER_CONFIRM_KEY = "group_order:confirm:";

    /**
     * 拼团订单uuid
     */
    String GROUP_ORDER_CONFIRM_UUID_KEY = "group_order:uuid_confirm:";
    /**
     * 秒杀确认订单信息缓存
     */
    String SECKILL_ORDER_CONFIRM_KEY = "seckill_order:confirm:";

    /**
     * 秒杀订单uuid
     */
    String SECKILL_ORDER_CONFIRM_UUID_KEY = "seckill_order:uuid_confirm:";

    /**
     * 积分确认订单信息缓存
     */
    String SCORE_ORDER_CONFIRM_KEY = "score_order:confirm:";

    /**
     * 积分订单uuid
     */
    String SCORE_ORDER_CONFIRM_UUID_KEY = "score_order:uuid_confirm:";
}
