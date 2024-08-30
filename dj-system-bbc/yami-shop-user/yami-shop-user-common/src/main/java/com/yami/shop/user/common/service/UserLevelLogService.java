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
import com.yami.shop.bean.param.CustomerMemberParam;
import com.yami.shop.bean.param.CustomerReqParam;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.model.UserLevelLog;

/**
 *
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
public interface UserLevelLogService extends IService<UserLevelLog> {

    /**
     * 分页获取用户等级记录
     * @param page 分页信息
     * @param userLevelLog 筛选信息
     * @return 用户等级记录列表
     */
    Page<UserLevelLog> getPage(Page<UserLevelLog> page, UserLevelLog userLevelLog);

    /**
     * 获取用户最高的等级（普通会员）
     * @param userId 用户id
     * @return 用户等级
     */
    Integer getMaxLevelByUserId(String userId);

    /**
     * 统计升级会员数
     * @param param 筛选数据
     * @return 升级会员数
     */
    CustomerMemberParam countMemberNum(CustomerReqParam param);

    /**
     * 获取用户最新的购买等级记录信息
     * @param userId 用户id
     * @return 等级记录信息
     */
    UserLevelLog getMaxCrtTimeByUserId(String userId);

    /**
     * 获取付费会员购买记录
     * @param page
     * @param userLevelLog
     * @return
     */
    IPage<UserLevelLog> pageBuyLevelLog(PageParam<UserLevelLog> page, UserLevelLog userLevelLog);
}
