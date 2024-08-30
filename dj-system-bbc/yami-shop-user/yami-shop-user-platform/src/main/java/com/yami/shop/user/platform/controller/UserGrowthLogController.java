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
import com.yami.shop.user.common.model.UserGrowthLog;
import com.yami.shop.user.common.service.UserGrowthLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户成长值记录
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Api(tags = "用户成长值记录")
@RestController
@AllArgsConstructor
@RequestMapping("/user/userGrowthLog" )
public class UserGrowthLogController {

    private final UserGrowthLogService userGrowthLogService;

//    @GetMapping("/page" )
//    @ApiOperation(value = "分页查询", notes = "分页查询")
//    public ServerResponseEntity<IPage<UserGrowthLog>> getUserGrowthLogPage(PageParam<UserGrowthLog> page, UserGrowthLog userGrowthLog) {
//        return ServerResponseEntity.success(userGrowthLogService.getPage(page,userGrowthLog));
//    }
//
//    @GetMapping("/info/{logId}" )
//    @ApiOperation(value = "查询用户成长值记录", notes = "查询用户成长值记录")
//    public ServerResponseEntity<UserGrowthLog> getById(@PathVariable("logId") Long logId) {
//        return ServerResponseEntity.success(userGrowthLogService.getById(logId));
//    }
//
//    /**
//     * 新增用户成长值记录
//     * @param userGrowthLog 用户成长值记录
//     * @return 是否新增成功
//     */
//    @SysLog("新增用户成长值记录" )
//    @PostMapping
//    @ApiOperation(value = "新增用户成长值记录", notes = "新增用户成长值记录")
//    @PreAuthorize("@pms.hasPermission('user:userGrowthLog:save')" )
//    public ServerResponseEntity<Boolean> save(@RequestBody @Valid UserGrowthLog userGrowthLog) {
//        return ServerResponseEntity.success(userGrowthLogService.save(userGrowthLog));
//    }
//
//    /**
//     * 修改用户成长值记录
//     * @param userGrowthLog 用户成长值记录
//     * @return 是否修改成功
//     */
//    @SysLog("修改用户成长值记录" )
//    @PutMapping
//    @PreAuthorize("@pms.hasPermission('user:userGrowthLog:update')" )
//    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid UserGrowthLog userGrowthLog) {
//        return ServerResponseEntity.success(userGrowthLogService.updateById(userGrowthLog));
//    }
//
//    /**
//     * 通过id删除用户成长值记录
//     * @param logId id
//     * @return 是否删除成功
//     */
//    @SysLog("删除用户成长值记录" )
//    @DeleteMapping("/{logId}" )
//    @PreAuthorize("@pms.hasPermission('user:userGrowthLog:delete')" )
//    public ServerResponseEntity<Boolean> removeById(@PathVariable Long logId) {
//        return ServerResponseEntity.success(userGrowthLogService.removeById(logId));
//    }

    /**
     * 获取某个用户的成长值记录
     */
    @GetMapping("/pageByUserId" )
    @ApiOperation(value = "分页", notes = "分页")
    public ServerResponseEntity<IPage<UserGrowthLog>> getPageByUserId(PageParam<UserGrowthLog> page, String userId) {
        IPage<UserGrowthLog> resPage = userGrowthLogService.getPageByUserId(page,userId);
        return ServerResponseEntity.success(resPage);
    }

}
