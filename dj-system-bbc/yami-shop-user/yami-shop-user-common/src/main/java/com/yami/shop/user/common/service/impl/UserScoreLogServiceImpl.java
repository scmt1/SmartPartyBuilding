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
import com.yami.shop.user.common.dao.UserScoreLogMapper;
import com.yami.shop.user.common.model.UserScoreLog;
import com.yami.shop.user.common.service.UserScoreLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户积分记录
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Service
@AllArgsConstructor
public class UserScoreLogServiceImpl extends ServiceImpl<UserScoreLogMapper, UserScoreLog> implements UserScoreLogService {

    private final UserScoreLogMapper userScoreLogMapper;

    @Override
    public Integer getConsecutiveDays(String userId) {
        return userScoreLogMapper.getConsecutiveDays(userId);
    }

    @Override
    public Page<UserScoreLog> getPage(PageParam<UserScoreLog> page, UserScoreLog userScoreLog) {
        return userScoreLogMapper.getPage(page,userScoreLog);
    }

    @Override
    public IPage<UserScoreLog> getPageByUserId(PageParam<UserScoreLog> page, String userId) {
        return userScoreLogMapper.getPageByUserId(page,userId);
    }


}
