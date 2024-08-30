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
import com.yami.shop.bean.param.UserTagParam;
import com.yami.shop.user.common.model.UserTagUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户和标签关联表
 *
 * @author LGH
 * @date 2020-09-10 08:44:22
 */
public interface UserTagUserMapper extends BaseMapper<UserTagUser> {

    /**
     * 获取标签关联信息根据用户id和标签id
     * @param userId 用户id
     * @param userTagId 标签id
     * @return 标签关联信息
     */
    UserTagUser getByUserIdAndTagId(@Param("userId") String userId, @Param("userTagId") Long userTagId);

    /**
     * 获取用户的标签数量
     * @param userId 用户id
     * @param userTagId 标签id
     * @return 结果
     */
    Integer countByUserIdAndTagId(@Param("userId") String userId, @Param("userTagId") Long userTagId);

    /**
     * 获取用户的标签列表
     * @param userId 用户id
     * @return 标签列表
     */
    List<UserTagParam> getUserTagsUserByUserId(@Param("userId") String userId);

    /**
     * 移除标签通过用户id和标签关联id
     * @param userId 用户id
     * @param userTagId 用户标签关联id
     * @return 结果
     */
    int removeByUserIdAndTagId(@Param("userId") String userId, @Param("userTagId") Long userTagId);
}
