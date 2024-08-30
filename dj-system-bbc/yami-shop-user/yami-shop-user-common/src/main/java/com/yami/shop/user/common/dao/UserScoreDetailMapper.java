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

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.user.common.model.UserScoreDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author lhd
 * @date 2020-05-25 15:31:02
 */
public interface UserScoreDetailMapper extends BaseMapper<UserScoreDetail> {

    /**
     * 查询有积分过期或抵现记录的用户
     * @param expireTime 过期时间
     * @return 积分过期列表
     */
    List<UserScoreDetail> listExpireScoreDetail(@Param("expireTime") DateTime expireTime);

    /**
     * 修改状态为0的积分明细为过期状态
     * @param dateTime 过期时间
     */
    void updateExpireScoreDetail(@Param("expireTime") DateTime dateTime);

    /**
     * 查询已经过期但还没标记的积分
     * @param userId 用户id
     * @param expireTime 过期时间
     * @param status 状态
     * @return 用户积分记录列表
     */
    List<UserScoreDetail> listByUserIdAndExpireTimeAndStatus(@Param("userId") String userId, @Param("expireTime") Date expireTime, @Param("status") Integer status);

    /**
     * 查询用户积分详细表数据
     * @param userId 用户id
     * @param status 状态
     * @return 用户积分详细表数据
     */
    List<UserScoreDetail> listByCreateTime(@Param("userId") String userId, @Param("status") Integer status);

    /**
     * 查询用户积分记录列表
     * @param userId 用户id
     * @param status 状态
     * @param current 开始搜索的索引
     * @param size 分页的大小
     * @return
     */
    List<UserScoreDetail> listByCreateTimeAndPage(@Param("userId") String userId, @Param("status") Integer status, @Param("current") Integer current, @Param("size") Integer size);

    /**
     * 批量更新用户积分状态
     *
     * @param status        状态
     * @param userScoreGetIds 用户积分ids
     */
    void batchUpdateUserScoreGetStatus(@Param("status") Integer status, @Param("userScoreGetIds") List<Long> userScoreGetIds);

}
