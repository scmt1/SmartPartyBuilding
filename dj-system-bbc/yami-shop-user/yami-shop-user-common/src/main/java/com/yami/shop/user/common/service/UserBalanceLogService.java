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


import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.dto.UserBalanceLogDto;
import com.yami.shop.user.common.model.UserBalanceLog;

/**
 * 余额充值记录
 *
 * @author YXF
 * @date 2020-09-09 17:38:30
 */
public interface UserBalanceLogService extends IService<UserBalanceLog> {

    /**
     * 分页获取余额充值记录
     * @param page 分页信息
     * @param userId 用户id
     * @return
     */
    PageParam<UserBalanceLogDto> getLogPage(PageParam<UserBalanceLogDto> page, String userId);

    /**
     * 获取用户最新充值余额的记录
     * @param userId
     * @return
     */
    UserBalanceLog getMaxCrtTimeByUserId(String userId);
}
