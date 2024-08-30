/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.Order;
import com.yami.shop.user.common.dto.ScoreOrderMergerDto;

/**
 * @author LGH
 * @date 2019-08-27 17:55:57
 */
public interface ScoreOrderService extends IService<Order> {


    /**
     * 将确认订单的信息放入缓存
     *
     * @param userId 用户id
     * @param scoreOrderMergerDto 积分订单数据
     * @return 积分订单数据
     */
    ScoreOrderMergerDto putConfirmScoreOrderCache(String userId, ScoreOrderMergerDto scoreOrderMergerDto);


    /**
     * 获取缓存中确认订单的信息
     * @param userId 用户id
     * @return 确认订单的信息
     */
    ScoreOrderMergerDto getConfirmScoreOrderCache(String userId);

    /**
     * 移除缓存中确认订单的信息
     *
     * @param userId 用户id
     */
    void removeConfirmScoreOrderCache(String userId);

    /**
     * 提交订单
     *
     * @param userId 用户id
     * @param hadConfirmOrder 提交积分订单信息
     * @return 处理后的订单信息
     */
    Order submit(String userId, ScoreOrderMergerDto hadConfirmOrder);

}
