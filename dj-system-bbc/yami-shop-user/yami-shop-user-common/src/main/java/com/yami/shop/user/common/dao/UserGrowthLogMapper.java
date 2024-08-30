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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.model.UserGrowthLog;
import org.apache.ibatis.annotations.Param;

/**
 * 用户成长值记录
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
public interface UserGrowthLogMapper extends BaseMapper<UserGrowthLog> {

    /**
     * 分页获取用户成长值记录
     * @param page 分页信息
     * @param userGrowthLog 筛选条件
     * @return 用户成长值记录列表
     */
    Page<UserGrowthLog> getPage(PageParam<UserGrowthLog> page, @Param("userGrowthLog")UserGrowthLog userGrowthLog);

    /**
     * 根据用户id，获取用户成长值记录列表
     * @param page 分页信息
     * @param userId 用户id
     * @return 列表
     */
    IPage<UserGrowthLog> getPageByUserId(@Param("page") PageParam<UserGrowthLog> page, @Param("userId") String userId);
}
