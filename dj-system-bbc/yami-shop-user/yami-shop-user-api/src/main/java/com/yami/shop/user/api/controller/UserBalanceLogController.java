/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.api.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.user.common.dto.UserBalanceLogDto;
import com.yami.shop.user.common.service.UserBalanceLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 余额充值记录
 *
 * @author YXF
 * @date 2020-09-09 17:38:30
 */
@RestController
@AllArgsConstructor
@Api(tags = "用户余额记录接口")
@RequestMapping("/p/userBalanceLog" )
public class UserBalanceLogController {

    private final UserBalanceLogService userBalanceLogService;

    /**
     * 分页查询用户余额记录
     * @param page 分页对象
     * @return 分页数据
     */
    @GetMapping("/log" )
    @ApiOperation(value = "查询用户余额记录", notes = "分页查询用户余额记录")
    public ServerResponseEntity<IPage<UserBalanceLogDto>> getUserBalanceLogPage(PageParam<UserBalanceLogDto> page) {
        String userId = SecurityUtils.getUser().getUserId();
        IPage<UserBalanceLogDto> userBalanceLogPage = userBalanceLogService.getLogPage(page, userId);
        return ServerResponseEntity.success(userBalanceLogPage);
    }


}
