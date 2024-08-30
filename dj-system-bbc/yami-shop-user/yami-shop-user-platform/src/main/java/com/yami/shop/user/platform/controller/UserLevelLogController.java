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
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.model.UserLevelLog;
import com.yami.shop.user.common.service.UserLevelLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 用户等级记录
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Api(tags = "用户等级记录")
@RestController
@AllArgsConstructor
@RequestMapping("/user/userLevelLog" )
public class UserLevelLogController {

    private final UserLevelLogService userLevelLogService;

    @GetMapping("/page" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<UserLevelLog>> getUserLevelLogPage(PageParam<UserLevelLog> page, UserLevelLog userLevelLog) {
        return ServerResponseEntity.success(userLevelLogService.getPage(page,userLevelLog));
    }

    @GetMapping("/pageBuyLevelLog")
    @ApiModelProperty(value = "获取付费会员购买记录", notes = "获取付费会员购买记录")
    public ServerResponseEntity<IPage<UserLevelLog>> pageBuyLevelLog(PageParam<UserLevelLog> page, UserLevelLog userLevelLog) {
        return ServerResponseEntity.success(userLevelLogService.pageBuyLevelLog(page, userLevelLog));
    }

    @GetMapping("/info/{levelLogId}" )
    @ApiOperation(value = "查询用户等级记录", notes = "查询用户等级记录")
    @ApiImplicitParam(name = "levelLogId", value = "用户等级记录id")
    public ServerResponseEntity<UserLevelLog> getById(@PathVariable("levelLogId") Long levelLogId) {
        return ServerResponseEntity.success(userLevelLogService.getById(levelLogId));
    }

    @SysLog("新增" )
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    @PreAuthorize("@pms.hasPermission('user:userLevelLog:save')" )
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid UserLevelLog userLevelLog) {
        return ServerResponseEntity.success(userLevelLogService.save(userLevelLog));
    }

    @SysLog("修改" )
    @PutMapping
    @ApiOperation(value = "修改", notes = "修改")
    @PreAuthorize("@pms.hasPermission('user:userLevelLog:update')" )
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid UserLevelLog userLevelLog) {
        userLevelLog.setState(1);
        return ServerResponseEntity.success(userLevelLogService.updateById(userLevelLog));
    }

    @SysLog("删除" )
    @DeleteMapping("/{levelLogId}" )
    @ApiOperation(value = "删除", notes = "通过id删除")
    @ApiImplicitParam(name = "levelLogId", value = "用户等级记录id")
    @PreAuthorize("@pms.hasPermission('user:userLevelLog:delete')" )
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long levelLogId) {
        return ServerResponseEntity.success(userLevelLogService.removeById(levelLogId));
    }

}
