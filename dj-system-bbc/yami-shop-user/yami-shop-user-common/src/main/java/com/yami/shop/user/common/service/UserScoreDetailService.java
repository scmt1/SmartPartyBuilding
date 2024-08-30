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

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.user.common.model.UserScoreDetail;

import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author lhd
 * @date 2020-05-25 15:31:02
 */
public interface UserScoreDetailService extends IService<UserScoreDetail> {


    /**
     * 修改用户过期积分
     * @param dateTime 过期时间
     */
    void updateExpireScoreDetail(DateTime dateTime);
//
//    /**
//     * 进行回退用户积分,并添加日志
//     * @param orderNumber 订单编号
//     * @param userId 用户id
//     */
//    void updateLogAndDetail(String orderNumber, String userId);

    /**
     * 保存用户积分明细（该方法需要在userExtension更新之前调用）
     * @param addDetail
     */
    void saveUserScoreDetail(UserScoreDetail addDetail);

    /**
     * 批量保存用户积分明细（该方法需要在userExtension更新之前调用）
     * @param userScoreDetails
     * @param userId
     */
    void saveBatchUserScoreDetail(List<UserScoreDetail> userScoreDetails, String userId);

    /**
     * 查询已经过期但还没标记的积分
     * @param userId 用户id
     * @param expireTime 过期时间
     * @param status 状态
     * @return 用户积分记录列表
     */
    List<UserScoreDetail> listByUserIdAndExpireTimeAndStatus(String userId, Date expireTime, Integer status);

    /**
     * 查询用户积分详细表数据
     * @param userId 用户id
     * @param status 状态
     * @return 用户积分详细表数据
     */
    List<UserScoreDetail> listByCreateTime(String userId, Integer status);

    /**
     * 查询用户积分记录列表
     * @param userId 用户id
     * @param status 状态
     * @param current 开始搜索的索引
     * @param size 分页的大小
     * @return
     */
    List<UserScoreDetail> listByCreateTimeAndPage(String userId, Integer status, Integer current, Integer size);
}
