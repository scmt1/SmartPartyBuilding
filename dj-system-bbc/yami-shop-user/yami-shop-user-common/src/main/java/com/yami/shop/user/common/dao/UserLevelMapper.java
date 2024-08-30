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
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.user.common.dto.LevelDetailDto;
import com.yami.shop.user.common.dto.UserLevelDto;
import com.yami.shop.user.common.model.UserLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员等级
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
public interface UserLevelMapper extends BaseMapper<UserLevel> {

    /**
     * 获取用户所在等级的，等级及等级关联的分类id列表信息
     * @param user 用户信息
     * @return 等级及等级关联的分类id列表
     */
    UserLevel selectOneAndCategory(@Param("user") UserExtension user);

    /**
     * 根据等级类型，获取对应的等级列表
     * @param userLevelType 等级类型
     * @return 等级列表
     */
    List<UserLevelDto> getList(@Param("userLevelType") Integer userLevelType);

    /**
     * 获取等级列表， 等级包含等级关联的分类id列表 （普通会员等级）
     * @param nowGrowth 所需成长值
     * @param level 等级
     * @return  等级列表
     */
    List<UserLevel> selectListAndCoupons(@Param("nowGrowth") Integer nowGrowth, @Param("level") Integer level);

    /**
     * 获取等级列表， 等级包含等级关联的分类id列表 （付费会员等级）
     * @param level 等级
     * @return  等级列表
     */
    UserLevel selectLevelAndCoupons(@Param("level") Integer level);

    /**
     * 获取等级列表，等级包含等级关联的权限列表信息
     * @param levelType 等级类型
     * @return 等级列表
     */
    List<LevelDetailDto> selectLevelAndRights(@Param("levelType") Integer levelType);

    /**
     * 更新普通会员等级的状态为启用
     */
    void setStatusByLevelType();

    /**
     * 获取等级列表， 等级包含等级关联的分类id列表 （普通会员等级）（等级倒序）
     * @param nowGrowth 所需成长值
     * @param level 等级
     * @return  等级列表
     */
    List<UserLevel> selectListAndCouponsLtNowGrowth(@Param("nowGrowth") Integer nowGrowth, @Param("level") Integer level);
}
