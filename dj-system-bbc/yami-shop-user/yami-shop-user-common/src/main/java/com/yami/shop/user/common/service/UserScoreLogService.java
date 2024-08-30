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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.model.UserScoreLog;

/**
 * 用户积分记录
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
public interface UserScoreLogService extends IService<UserScoreLog> {

    /**
     * 获取签到的天数
     * @param userId 用户id
     * @return 签到的天数
     */
    Integer getConsecutiveDays(String userId);

    /**
     * 分页获取用户积分记录
     * @param page 分页信息
     * @param userScoreLog 筛选信息
     * @return 用户积分记录
     */
    Page<UserScoreLog> getPage(PageParam<UserScoreLog> page, UserScoreLog userScoreLog);

    /**
     * 获取某个用户的积分明细
     * @param page 分页信息
     * @param userId 用户id
     * @return 指定用户id的积分明细
     */
    IPage<UserScoreLog> getPageByUserId(PageParam<UserScoreLog> page, String userId);
}
