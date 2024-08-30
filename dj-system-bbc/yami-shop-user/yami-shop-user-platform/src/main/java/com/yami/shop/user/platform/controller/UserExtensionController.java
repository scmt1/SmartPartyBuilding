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

import com.yami.shop.service.UserExtensionService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用户扩展信息
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Api(tags = "用户扩展信息")
@RestController
@AllArgsConstructor
@RequestMapping("/user/userExtension" )
public class UserExtensionController {

    private final UserExtensionService userExtensionService;

//    @GetMapping("/page" )
//    @ApiOperation(value = "分页查询", notes = "分页查询")
//    public ServerResponseEntity<IPage<UserExtension>> getUserExtensionPage(PageParam<UserExtension> page, UserExtension userExtension) {
//        return ServerResponseEntity.success(userExtensionService.getPage(page,userExtension));
//    }
//
//    @GetMapping("/info/{userExtensionId}" )
//    @ApiOperation(value = "查询用户扩展", notes = "查询用户扩展")
//    @ApiImplicitParam(name = "userExtensionId", value = "用户扩展信息id")
//    public ServerResponseEntity<UserExtension> getById(@PathVariable("userExtensionId") Long userExtensionId) {
//        return ServerResponseEntity.success(userExtensionService.getById(userExtensionId));
//    }
//
//    @SysLog("新增用户扩展信息" )
//    @PostMapping
//    @ApiOperation(value = "新增用户扩展信息", notes = "新增用户扩展信息")
//    @PreAuthorize("@pms.hasPermission('user:userExtension:save')" )
//    public ServerResponseEntity<Boolean> save(@RequestBody @Valid UserExtension userExtension) {
//        return ServerResponseEntity.success(userExtensionService.save(userExtension));
//    }
//
//    /**
//     * 修改用户扩展信息
//     * @param userExtension 用户扩展信息
//     * @return 是否修改成功
//     */
//    @SysLog("修改用户扩展信息" )
//    @PutMapping
//    @ApiOperation(value = "修改用户扩展信息", notes = "修改用户扩展信息")
//    @PreAuthorize("@pms.hasPermission('user:userExtension:update')" )
//    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid UserExtension userExtension) {
//        return ServerResponseEntity.success(userExtensionService.updateById(userExtension));
//    }
//
//    /**
//     * 通过id删除用户扩展信息
//     * @param userExtensionId id
//     * @return 是否删除成功
//     */
//    @SysLog("删除用户扩展信息" )
//    @DeleteMapping("/{userExtensionId}" )
//    @ApiOperation(value = "删除用户扩展信息", notes = "删除用户扩展信息")
//    @PreAuthorize("@pms.hasPermission('user:userExtension:delete')" )
//    public ServerResponseEntity<Boolean> removeById(@PathVariable Long userExtensionId) {
//        return ServerResponseEntity.success(userExtensionService.removeById(userExtensionId));
//    }

}
