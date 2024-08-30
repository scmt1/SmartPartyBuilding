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


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.dto.UserBalanceDto;
import com.yami.shop.user.common.model.UserBalance;
import com.yami.shop.user.common.service.UserBalanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;


/**
 * 余额充值级别表
 *
 * @author YXF
 * @date 2020-09-08 10:42:39
 */
@RestController
@AllArgsConstructor
@Api(tags = "会员充值信息接口")
@RequestMapping("/p/userBalance" )
public class UserBalanceController {

    private final UserBalanceService userBalanceService;
    private final UserExtensionService userExtensionService;
    private final MapperFacade mapperFacade;

    /**
     * 查询充值数据列表
     * @return 列表数据
     */
    @GetMapping("/getList" )
    @ApiOperation(value = "获取充值数据列表", notes = "获取充值数据列表")
    public ServerResponseEntity<List<UserBalanceDto>> getUserBalancePage() {
        List<UserBalance> balanceList = userBalanceService.getBalanceList();
        List<UserBalanceDto> userBalanceDtoList = mapperFacade.mapAsList(balanceList, UserBalanceDto.class);
        return ServerResponseEntity.success(userBalanceDtoList);
    }

    /**
     * 查询用户余额
     * @return 用户余额
     */
    @GetMapping("/getBalanceInfo" )
    @ApiOperation(value = "查询用户余额", notes = "查询用户余额")
    public ServerResponseEntity<Double> getBalanceInfo() {
        UserExtension userExtension = userExtensionService.getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, SecurityUtils.getUser().getUserId()));
        Double balance = userExtension.getTotalBalance();
        if (Objects.isNull(balance)){
            balance = 0.0;
        }
        return ServerResponseEntity.success(balance);
    }
}
