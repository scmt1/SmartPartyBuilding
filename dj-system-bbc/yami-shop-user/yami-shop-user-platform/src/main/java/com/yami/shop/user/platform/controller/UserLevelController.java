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

import com.yami.shop.bean.param.UserUpdateParam;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.user.common.dto.UserLevelDto;
import com.yami.shop.user.common.enums.GrowthLogSourceEnum;
import com.yami.shop.user.common.model.UserLevel;
import com.yami.shop.user.common.service.UserLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 会员等级
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Api(tags = "会员等级")
@RestController
@AllArgsConstructor
@RequestMapping("/user/userLevel" )
public class UserLevelController {

    private final UserLevelService userLevelService;

    @SysLog("查看会员等级" )
    @GetMapping("/list" )
    @ApiOperation(value = "获取用户等级列表", notes = "获取用户等级列表")
    @PreAuthorize("@pms.hasPermission('user:userLevel:list')" )
    public ServerResponseEntity<List<UserLevelDto>> listUserLevel(Integer userLevelType) {
        return ServerResponseEntity.success(userLevelService.listUserLevelsByUserLevelType(userLevelType));
    }

    @GetMapping("/info/{id}" )
    @ApiOperation(value = "查询会员等级", notes = "通过id查询会员等级")
    @ApiImplicitParam(name = "id", value = "会员等级id")
    public ServerResponseEntity<UserLevel> getById(@PathVariable("id") Long id) {
        UserLevel userLevel = userLevelService.getUserLevelById(id);
        return ServerResponseEntity.success(userLevel);
    }

    @SysLog("新增会员等级" )
    @PostMapping
    @ApiOperation(value = "新增会员等级", notes = "新增会员等级")
    @PreAuthorize("@pms.hasPermission('user:userLevel:save')" )
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid UserLevel userLevel) {
        return ServerResponseEntity.success(userLevelService.save(userLevel));
    }

    @SysLog("修改会员等级" )
    @PutMapping
    @ApiOperation(value = "修改会员等级", notes = "修改会员等级")
    @PreAuthorize("@pms.hasPermission('user:userLevel:update')" )
    public ServerResponseEntity<Void> updateById(@RequestBody @Valid UserLevel userLevel) {
        userLevelService.updateUserLevelList(userLevel);
        return ServerResponseEntity.success();
    }

    @SysLog("删除会员等级" )
    @DeleteMapping
    @ApiOperation(value = "删除会员等级", notes = "删除会员等级")
    @PreAuthorize("@pms.hasPermission('user:userLevel:delete')" )
    public ServerResponseEntity<Boolean> removeById(@RequestBody @Valid UserLevel userLevel) {
        return ServerResponseEntity.success(userLevelService.deleteUserLevel(userLevel));
    }

    @PutMapping("/updateUserLevel")
    @ApiOperation(value = "更新用户会员等级", notes = "更新用户会员等级")
    public ServerResponseEntity<Boolean> updateUserLevel(@RequestBody @Valid UserLevel userLevel) {
        return ServerResponseEntity.success(userLevelService.updateUserLevel(userLevel));
    }

    @PutMapping("/updateGrowth")
    @ApiOperation(value = "平台批量修改会员成长值", notes = "平台批量修改会员成长值")
    @PreAuthorize("@pms.hasPermission('user:userLevel:updateGrowth')" )
    public ServerResponseEntity<Boolean> batchUserGrowth(@RequestBody UserUpdateParam param) {
        param.setGrowthSource(GrowthLogSourceEnum.SYSTEM.value());
        return ServerResponseEntity.success(userLevelService.batchUpdateGrowth(param));
    }

    @PutMapping("/updateScore")
    @ApiOperation(value = "平台批量修改会员积分", notes = "平台批量修改会员积分")
    @PreAuthorize("@pms.hasPermission('user:userLevel:updateScore')" )
    public ServerResponseEntity<Boolean> batchUserScore(@RequestBody UserUpdateParam param) {
        return ServerResponseEntity.success(userLevelService.batchUpdateScore(param));
    }


}
