/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.order;
/**
 * 获取es商品信息的先后顺序
 * @author LGH
 */
public interface EsProductOrder {

    /**
     * 没有任何的顺序
     */
    int DEFAULT = 0;

    /**
     * 商品团购信息
     */
    int GROUP_BUY = 100;

    /**
     * 商品秒杀信息
     */
    int SECKILL = 200;
}
