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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.dao.UserBalanceLogMapper;
import com.yami.shop.user.common.dto.UserBalanceLogDto;
import com.yami.shop.user.common.model.UserBalanceLog;
import com.yami.shop.user.common.service.UserBalanceLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 余额充值记录
 *
 * @author YXF
 * @date 2020-09-09 17:38:30
 */
@Service
@AllArgsConstructor
public class UserBalanceLogServiceImpl extends ServiceImpl<UserBalanceLogMapper, UserBalanceLog> implements UserBalanceLogService {

    private final UserBalanceLogMapper userBalanceLogMapper;

    @Override
    public PageParam<UserBalanceLogDto> getLogPage(PageParam<UserBalanceLogDto> page, String userId) {
        return userBalanceLogMapper.getLogPage(page, userId);
    }

	@Override
	public UserBalanceLog getMaxCrtTimeByUserId(String userId) {
		return userBalanceLogMapper.getMaxCrtTimeByUserId(userId);
	}
}
