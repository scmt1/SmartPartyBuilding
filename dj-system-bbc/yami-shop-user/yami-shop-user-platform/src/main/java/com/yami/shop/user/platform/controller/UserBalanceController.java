/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.platform.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.param.UserUpdateParam;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.model.UserBalance;
import com.yami.shop.user.common.model.UserBalanceLog;
import com.yami.shop.user.common.service.UserBalanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 余额充值级别表
 *
 * @author YXF
 * @date 2020-09-08 10:42:39
 */
@Api(tags = "余额充值等级")
@RestController
@AllArgsConstructor
@RequestMapping("/user/userBalance" )
public class UserBalanceController {

    private final UserBalanceService userBalanceService;

    @SysLog("查看余额充值级别表" )
    @GetMapping("/getList" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PreAuthorize("@pms.hasPermission('user:userBalance:getList')" )
    public ServerResponseEntity<List<UserBalance>> getList(UserBalance userBalance) {
        return ServerResponseEntity.success(userBalanceService.getBalanceList());
    }

    @GetMapping("/info/{balanceId}" )
    @ApiOperation(value = "查询余额充值信息", notes = "通过id查询余额充值级别表")
    @ApiImplicitParam(name = "balanceId", value = "余额充值id")
    public ServerResponseEntity<UserBalance> getById(@PathVariable("balanceId") Long balanceId) {
        return ServerResponseEntity.success(userBalanceService.getBalanceInfo(balanceId));
    }

    @SysLog("新增余额充值级别表" )
    @PostMapping
    @ApiOperation(value = "新增余额充值", notes = "新增余额充值")
    @PreAuthorize("@pms.hasPermission('user:userBalance:save')" )
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid UserBalance userBalance) {
        userBalanceService.saveBalance(userBalance);
        return ServerResponseEntity.success();
    }

    @SysLog("修改余额充值级别表" )
    @PutMapping
    @ApiOperation(value = "修改余额充值", notes = "修改余额充值")
    @PreAuthorize("@pms.hasPermission('user:userBalance:update')" )
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid UserBalance userBalance) {
        userBalanceService.updateBalanceById(userBalance);
        userBalanceService.removeCacheByBalanceId();
        return ServerResponseEntity.success();
    }

    @SysLog("删除余额充值级别表" )
    @DeleteMapping("/{balanceId}" )
    @ApiOperation(value = "删除余额充值", notes = "删除余额充值")
    @PreAuthorize("@pms.hasPermission('user:userBalance:delete')" )
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long balanceId) {
        userBalanceService.removeById(balanceId);
        userBalanceService.removeCacheByBalanceId();
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateUserBalance")
    @ApiOperation(value = "平台批量修改会员余额", notes = "平台批量修改会员余额")
    @PreAuthorize("@pms.hasPermission('platform:user:updateBalance')" )
    public ServerResponseEntity<Boolean> batchUpdateUserBalance(@RequestBody UserUpdateParam param) {
        return ServerResponseEntity.success(userBalanceService.batchUpdateUserBalance(param));
    }

    @GetMapping("/getPageByUserId" )
    @ApiOperation(value = "分页查询指定用户的余额明细", notes = "分页查询指定用户的余额明细")
    @ApiImplicitParam(name = "userId", value = "用户id")
    public ServerResponseEntity<IPage<UserBalanceLog>> getPageByUserId(PageParam<UserBalanceLog> page, String userId) {
        IPage<UserBalanceLog> res = userBalanceService.getPageByUserId(page,userId);
        return ServerResponseEntity.success(res);
    }


}
