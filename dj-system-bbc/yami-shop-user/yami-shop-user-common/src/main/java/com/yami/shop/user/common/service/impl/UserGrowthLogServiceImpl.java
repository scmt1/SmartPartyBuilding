/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.dao.UserGrowthLogMapper;
import com.yami.shop.user.common.model.UserGrowthLog;
import com.yami.shop.user.common.service.UserGrowthLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户成长值记录
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Service
@AllArgsConstructor
public class UserGrowthLogServiceImpl extends ServiceImpl<UserGrowthLogMapper, UserGrowthLog> implements UserGrowthLogService {

    private final UserGrowthLogMapper userGrowthLogMapper;

    @Override
    public Page<UserGrowthLog> getPage(PageParam<UserGrowthLog> page, UserGrowthLog userGrowthLog) {
        return userGrowthLogMapper.getPage(page,userGrowthLog);
    }

    @Override
    public IPage<UserGrowthLog> getPageByUserId(PageParam<UserGrowthLog> page, String userId) {
        return userGrowthLogMapper.getPageByUserId(page,userId);
    }
}
