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
import com.yami.shop.bean.dto.UserScoreLockDTO;
import com.yami.shop.bean.model.Order;
import com.yami.shop.user.common.model.UserScoreLock;

import java.util.List;

/**
 * 积分锁定信息
 *
 * @author lhd
 * @date 2022-05-06 17:27:53
 */
public interface UserScoreLockService extends IService<UserScoreLock> {
    /**
     * 按照订单号锁定积分
     * @param userScoreLocks 订单号和积分的集合
     */
    void lock(List<UserScoreLockDTO> userScoreLocks, String userId);

    /**
     * 按照订单号解锁积分
     * @param order 订单信息
     */
    void unlock(Order order);
}
