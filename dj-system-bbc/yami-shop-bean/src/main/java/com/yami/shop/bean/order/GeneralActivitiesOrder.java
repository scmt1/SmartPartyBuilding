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
 * 所以活动通用顺序
 * @author cl
 */
public interface GeneralActivitiesOrder {

    /**
     * 没有任何活动时的顺序
     */
    int DEFAULT = 0;

    /**
     * 优惠券，排在DEFAULT后面
     */
    int COUPON = 200;

    /**
     * 分销，排在DEFAULT后面就行，不论顺序
     */
    int DISTRIBUTION = 300;

    /**
     * 秒杀，排在DEFAULT后面就行，不论顺序
     */
    int SECKILL = 400;

    /**
     * 团购
     */
    int GROUPON = 500;

    /**
     * 积分
     */
    int SCORE = 600;
}
