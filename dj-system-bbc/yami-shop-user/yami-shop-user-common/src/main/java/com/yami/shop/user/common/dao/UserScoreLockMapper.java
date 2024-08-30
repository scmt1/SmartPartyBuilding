/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.user.common.model.UserScoreLock;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 积分锁定信息
 *
 * @author lhd
 * @date 2022-05-06 17:27:53
 */
public interface UserScoreLockMapper extends BaseMapper<UserScoreLock> {


    /**
     * 将锁定状态标记为已解锁
     *
     * @param status
     * @param userScoreLockIds 用户锁定的ids
     * @return
     */
    int unLockByIds(@Param("status") Integer status, @Param("userScoreLockIds") List<Long> userScoreLockIds);

    /**
     * 将锁定状态标记为已解锁
     *
     * @param status
     * @param orderNumbers 用户订单号集合
     * @return
     */
    int unLockByOrderNumbers(@Param("status") Integer status, @Param("orderNumbers") List<String> orderNumbers);

    /**
     * 获取需要解锁的用户积分id列表
     *
     * @param orderNumber 订单号
     * @return 用户的积分id列表
     */
    List<UserScoreLock> listUserScoreLocksByOrderNumber(@Param("orderNumber") String orderNumber);

}
