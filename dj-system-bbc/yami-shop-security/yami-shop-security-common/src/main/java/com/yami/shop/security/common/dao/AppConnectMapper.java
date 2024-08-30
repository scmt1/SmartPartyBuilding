/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.security.common.model.AppConnect;
import org.apache.ibatis.annotations.Param;

/**
 * @author yami
 */
public interface AppConnectMapper extends BaseMapper<AppConnect> {

    /**
     * 根据第三方userId和第三方系统id获取用户关联第三方信息
     *
     * @param bizUserId 第三方用户id
     * @param appId     第三方系统id
     * @return 用户关联第三方信息
     */
    AppConnect getByBizUserId(@Param("bizUserId") String bizUserId, @Param("appId") Integer appId);

    /**
     * 根据第三方userId和第三方系统id获取用户关联第三方信息
     *
     * @param userId 用户id
     * @param appId  第三方系统id
     * @return 用户关联第三方信息
     */
    AppConnect getByUserId(@Param("userId") String userId, @Param("appId") Integer appId);

    /**
     * 获取用户id
     *
     * @param bizUnionId 第三方userId
     * @return 用户关联第三方信息
     */
    String getUserIdByUnionId(@Param("bizUnionId") String bizUnionId);

    /**
     * 修改用户第三方登录信息
     *
     * @param appConnect 用户第三方登录信息
     */
    void updateByBizUserIdAndAppId(@Param("appConnect") AppConnect appConnect);

    /**
     * 获取根据尝试社交登录时，保存的临时的uid获取社交
     *
     * @param tempUid tempUid
     * @return 用户社交账号信息
     */
    AppConnect getByTempUid(@Param("tempUid") String tempUid);

    /**
     * 绑定社交账号，通过tempuid
     *
     * @param userId  userId
     * @param tempUid tempUid
     */
    void bindUserIdByTempUid(@Param("userId") String userId, @Param("tempUid") String tempUid);

    /**
     * 解除用户绑定
     *
     * @param bizUserId  openid
     * @param socialType 社交账号类型
     */
    void unBindUser(@Param("bizUserId") String bizUserId, @Param("socialType") Integer socialType);

    /**
     * 解除用户绑定
     *
     * @param userId 用户id
     */
    void unBindUserByUserId(@Param("userId") String userId);
}
