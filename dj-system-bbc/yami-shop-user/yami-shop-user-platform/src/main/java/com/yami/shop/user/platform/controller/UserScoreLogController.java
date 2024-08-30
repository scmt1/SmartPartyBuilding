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
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.model.UserScoreLog;
import com.yami.shop.user.common.service.UserScoreLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用户积分记录
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Api(tags = "用户积分记录")
@RestController
@AllArgsConstructor
@RequestMapping("/user/userScoreLog" )
public class UserScoreLogController {

    private final UserScoreLogService userScoreLogService;
//
//    @GetMapping("/page" )
//    @ApiOperation(value = "分页查询", notes = "分页查询")
//    public ServerResponseEntity<IPage<UserScoreLog>> getUserScoreLogPage(PageParam<UserScoreLog> page, UserScoreLog userScoreLog) {
//        return ServerResponseEntity.success(userScoreLogService.getPage(page,userScoreLog));
//    }
//
//    @GetMapping("/info/{logId}" )
//    @ApiOperation(value = "查询用户积分记录", notes = "通过id查询用户积分记录")
//    public ServerResponseEntity<UserScoreLog> getById(@PathVariable("logId") Long logId) {
//        return ServerResponseEntity.success(userScoreLogService.getById(logId));
//    }
//
//    @SysLog("新增用户积分记录" )
//    @PostMapping
//    @ApiOperation(value = "新增用户积分记录", notes = "新增用户积分记录")
//    @PreAuthorize("@pms.hasPermission('user:userScoreLog:save')" )
//    public ServerResponseEntity<Boolean> save(@RequestBody @Valid UserScoreLog userScoreLog) {
//        return ServerResponseEntity.success(userScoreLogService.save(userScoreLog));
//    }
//
//    @SysLog("修改用户积分记录" )
//    @PutMapping
//    @ApiOperation(value = " 修改用户积分记录", notes = " 修改用户积分记录")
//    @PreAuthorize("@pms.hasPermission('user:userScoreLog:update')" )
//    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid UserScoreLog userScoreLog) {
//        return ServerResponseEntity.success(userScoreLogService.updateById(userScoreLog));
//    }
//
//    @SysLog("删除用户积分记录" )
//    @DeleteMapping("/{logId}" )
//    @ApiOperation(value = "删除用户积分记录", notes = "通过id删除用户积分记录")
//    @PreAuthorize("@pms.hasPermission('user:userScoreLog:delete')" )
//    public ServerResponseEntity<Boolean> removeById(@PathVariable Long logId) {
//        return ServerResponseEntity.success(userScoreLogService.removeById(logId));
//    }

    @GetMapping("/pageByUserId" )
    @ApiOperation(value = "获取指定用户的积分明细", notes = "获取指定用户的积分明细")
    public ServerResponseEntity<IPage<UserScoreLog>> getPageByUserId(PageParam<UserScoreLog> page, String userId) {
        IPage<UserScoreLog> resPage = userScoreLogService.getPageByUserId(page,userId);
        return ServerResponseEntity.success(resPage);
    }


}
