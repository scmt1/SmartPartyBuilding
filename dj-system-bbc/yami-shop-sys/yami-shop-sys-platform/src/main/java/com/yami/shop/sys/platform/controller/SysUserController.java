/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.platform.controller;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.model.UpdatePasswordDto;
import com.yami.shop.security.platform.util.SecurityUtils;
import com.yami.shop.sys.common.model.SysUser;
import com.yami.shop.sys.common.service.SysRoleService;
import com.yami.shop.sys.common.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 系统用户
 * @author lgh
 */
@Api(tags = "系统用户")
@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserController {

    @Value("${yami.expose.operation.auth:}")
    private Boolean permission;

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private PasswordManager passwordManager;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", value = "用户名"),
            @ApiImplicitParam(name = "nickName", value = "用户昵称")
    })
    @PreAuthorize("@pms.hasPermission('sys:user:page')")
    public ServerResponseEntity<IPage<SysUser>> page(String username, String nickName, PageParam<SysUser> page){

        IPage<SysUser> sysUserPage = sysUserService.page(page, new LambdaQueryWrapper<SysUser>()
                .like(StrUtil.isNotBlank(username), SysUser::getUsername, username)
                .like(StrUtil.isNotBlank(nickName), SysUser::getNickName, nickName));
        if (BooleanUtil.isFalse(permission)) {
            for (SysUser record : sysUserPage.getRecords()) {
                record.setMobile(PhoneUtil.hideBetween(record.getMobile()).toString());
            }
        }

        return ServerResponseEntity.success(sysUserPage);
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取登录的用户信息", notes = "获取登录的用户信息")
    public ServerResponseEntity<SysUser> info(){
        return ServerResponseEntity.success(sysUserService.getSysUserById(SecurityUtils.getSysUser().getUserId()));
    }

    @SysLog("修改密码")
    @PostMapping("/password")
    @ApiOperation(value="修改密码", notes="修改当前登陆用户的密码")
    public ServerResponseEntity<String> password(@RequestBody @Valid UpdatePasswordDto param) {
        Long userId = SecurityUtils.getSysUser().getUserId();
        if (Objects.equals(userId.intValue(), Constant.SUPER_ADMIN_ID) && BooleanUtil.isFalse(permission)) {
            throw new YamiShopBindException("yami.no.auth");
        }
        SysUser dbUser = sysUserService.getSysUserById(userId);
        String password = passwordManager.decryptPassword(param.getPassword());
        if (!passwordEncoder.matches(password, dbUser.getPassword())) {
            // 原密码不正确
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.password.error"));
        }
        //新密码
        String decryptPassword = passwordManager.decryptPassword(param.getNewPassword());
        String newPassword = passwordEncoder.encode(decryptPassword);
//        更新密码
        sysUserService.updatePasswordByUserId(userId, newPassword);
        tokenStore.deleteAllToken(SysTypeEnum.PLATFORM.value().toString(),userId.toString());
        return ServerResponseEntity.success();
    }

    @GetMapping("/info/{userId}")
    @ApiOperation(value = "用户信息", notes = "用户信息")
    @ApiImplicitParam(name = "userId", value = "用户id")
    @PreAuthorize("@pms.hasPermission('sys:user:info')")
    public ServerResponseEntity<SysUser> info(@PathVariable("userId") Long userId){
        SysUser user = sysUserService.getSysUserById(userId);
        if (BooleanUtil.isFalse(permission)){
            user.setMobile(PhoneUtil.hideBetween(user.getMobile()).toString());
        }
        if (Objects.equals(userId, SecurityUtils.getSysUser().getUserId())) {
            user.setIsSelf(1);
        }
        user.setUserId(null);
        //获取用户所属的角色列表
        List<Long> roleIdList = sysRoleService.listRoleIdByUserId(userId);
        user.setRoleIdList(roleIdList);
        return ServerResponseEntity.success(user);
    }

    @GetMapping("/sysUserInfo")
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @PreAuthorize("@pms.hasPermission('sys:user:info')")
    public ServerResponseEntity<SysUser> sysUserInfo(){
        SysUser user = sysUserService.getSysUserById(SecurityUtils.getSysUser().getUserId());
        user.setUserId(null);
        user.setPassword(null);
        return ServerResponseEntity.success(user);
    }

    @SysLog("保存用户")
    @PostMapping
    @ApiOperation(value = "保存用户", notes = "保存用户")
    @PreAuthorize("@pms.hasPermission('sys:user:save')")
    public ServerResponseEntity<String> save(@Valid @RequestBody SysUser user){
        String username = user.getUsername();
        SysUser dbUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username));
        if (dbUser!=null) {
            // 该用户已存在
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.user.already"));
        }
        String decryptPassword = passwordManager.decryptPassword(user.getPassword());
        user.setPassword(passwordEncoder.encode(decryptPassword));
        sysUserService.saveUserAndUserRole(user);

        return ServerResponseEntity.success();
    }

    @SysLog("保存用户")
    @PostMapping("/saveSysUserInfo")
    @ApiOperation(value = "保存用户", notes = "保存用户")
    public ServerResponseEntity<String> saveSysUserInfo(@Valid @RequestBody SysUser user){
        String username = user.getUsername();
        SysUser dbUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username));
        if (dbUser!=null) {
            // 该用户已存在
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.user.already"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        sysUserService.saveUserAndUserRole(user);

        return ServerResponseEntity.success();
    }

    @SysLog("修改用户")
    @PutMapping
    @ApiOperation(value = "修改用户", notes = "修改用户")
    @PreAuthorize("@pms.hasPermission('sys:user:update')")
    public ServerResponseEntity<String> update(@Valid @RequestBody SysUser user){
        if (Objects.equals(user.getUserId().intValue(), Constant.SUPER_ADMIN_ID) && BooleanUtil.isFalse(permission)) {
            throw new YamiShopBindException("yami.no.auth");
        }
        String password = passwordManager.decryptPassword(user.getPassword());

        SysUser dbUser = sysUserService.getSysUserById(user.getUserId());
        SysUser dbUserNameInfo = sysUserService.getByUserName(user.getUsername());

        //修改管理员账号但修改人不是管理员，则抛出异常（只有管理员可以改管理员信息）
        if (user.getUserId() == Constant.SUPER_ADMIN_ID &&
                SecurityUtils.getSysUser().getUserId() != Constant.SUPER_ADMIN_ID) {
            // 您没有权限修改管理员信息
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.not.permission.modify.administrator.info"));
        }
        if (dbUserNameInfo != null && !Objects.equals(dbUserNameInfo.getUserId(),user.getUserId())) {
            // 该用户已存在
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.user.already"));
        }
        if (StrUtil.isBlank(password)) {
            user.setPassword(null);
        }else {
            user.setPassword(passwordEncoder.encode(password));
        }
        sysUserService.updateUserAndUserRole(user);
        return ServerResponseEntity.success();
    }

    @SysLog("删除用户")
    @DeleteMapping
    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParam(name = "userIds", value = "用户id列表")
    @PreAuthorize("@pms.hasPermission('sys:user:delete')")
    public ServerResponseEntity<String> delete(@RequestBody Long[] userIds){
        if (userIds.length == 0) {
            // 请选择需要删除的用户
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.select.user"));
        }
        if(ArrayUtil.contains(userIds, Constant.SUPER_ADMIN_ID)){
            // 系统管理员不能删除
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.admin.error"));
        }
        if(ArrayUtil.contains(userIds, SecurityUtils.getSysUser().getUserId())){
            // 当前用户不能删除
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.delete.error"));
        }
        sysUserService.removeByIds(Arrays.asList(userIds));
        return ServerResponseEntity.success();
    }



}
