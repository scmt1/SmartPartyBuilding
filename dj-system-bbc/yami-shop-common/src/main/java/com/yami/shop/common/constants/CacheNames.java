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
 * @Author lth
 * @Date 2021/8/17 13:09
 */
public interface CacheNames extends ProductCacheNames, ShopCacheNames {
    /**
     *
     * 参考CacheKeyPrefix
     * cacheNames 与 key 之间的默认连接字符
     */
    String UNION = "::";
    /**
     * key内部的连接字符（自定义）
     */
    String UNION_KEY = ":";
}
