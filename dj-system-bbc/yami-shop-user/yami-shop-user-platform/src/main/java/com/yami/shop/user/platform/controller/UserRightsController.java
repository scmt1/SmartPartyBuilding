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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.model.UserRights;
import com.yami.shop.user.common.service.UserRightsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


/**
 * 权益
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Api(tags = "会员权益")
@RestController
@AllArgsConstructor
@RequestMapping("/user/userRights" )
public class UserRightsController {

    /**
     * 系统核销中最大的权益id
     */
    private final Integer MAX_RIGHTSID_OF_SERVICETYPE = 5;
    private final UserRightsService userRightsService;

    @GetMapping
    @ApiOperation(value = "权益列表", notes = "权益列表")
    @PreAuthorize("@pms.hasPermission('user:userRights:list')" )
    public ServerResponseEntity<IPage<UserRights>> listUserLevel(PageParam<UserRights> page, UserRights userRights) {
        return ServerResponseEntity.success( userRightsService.page(page, new LambdaQueryWrapper<UserRights>()
                .like(!StrUtil.isBlank(userRights.getRightsName()),UserRights::getRightsName, userRights.getRightsName())
                .eq(UserRights::getStatus, 1)
                .eq(!Objects.isNull(userRights.getServiceType()),UserRights::getServiceType, userRights.getServiceType())
                .orderByDesc(UserRights::getServiceType)
                .orderByAsc(UserRights::getSeq)
        ));
    }

    /**
     * 通过id查询
     * @param rightsId id
     * @return 单个数据
     */
    @GetMapping("/info/{rightsId}" )
    @ApiImplicitParam(name = "rightsId", value = "权益id")
    @ApiOperation(value = "查询信息", notes = "通过id查询信息")
    public ServerResponseEntity<UserRights> getById(@PathVariable("rightsId") Long rightsId) {
        return ServerResponseEntity.success(userRightsService.getById(rightsId));
    }

    @SysLog("新增" )
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    @PreAuthorize("@pms.hasPermission('user:userRights:save')" )
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid UserRights userRights) {
        // 1:商家线下核销
        userRights.setServiceType(1);
        userRights.setStatus(1);
        return ServerResponseEntity.success(userRightsService.save(userRights));
    }

    @SysLog("修改" )
    @PutMapping
    @ApiOperation(value = "修改", notes = "修改")
    @PreAuthorize("@pms.hasPermission('user:userRights:update')" )
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid UserRights userRights) {
        return ServerResponseEntity.success(userRightsService.updateById(userRights));
    }

    @SysLog("删除" )
    @DeleteMapping("/{rightsId}" )
    @ApiOperation(value = "删除", notes = "通过id删除")
    @ApiImplicitParam(name = "rightsId", value = "权益id")
    @PreAuthorize("@pms.hasPermission('user:userRights:delete')" )
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long rightsId) {
        return ServerResponseEntity.success(userRightsService.removeRights(rightsId));
    }

//    /**
//     * 启用系统核销权益
//     * @param rightsIds
//     */
//    @SysLog("启用系统核销权益" )
//    @PutMapping("/setStatus")
//    @PreAuthorize("@pms.hasPermission('user:userRights:update')" )
//    public ServerResponseEntity<Void> updateById(@RequestBody @Valid Long[] rightsIds) {
//        for (Long rightsId:rightsIds){
//            userRightsService.setStatua(rightsId,1);
//        }
//        return ServerResponseEntity.success();
//    }
//    /**
//     * 获取系统核销列表
//     */
//    @GetMapping("/getList")
//    public ServerResponseEntity<List<UserRights>> getList() {
//        return ServerResponseEntity.success( userRightsService.list(new LambdaQueryWrapper<UserRights>()
//                .eq(UserRights::getServiceType, 0)
//                .orderByAsc(UserRights::getSeq)
//        ));
//    }

    @GetMapping("/list")
    @ApiOperation(value = "获取权益列表", notes = "获取权益列表")
    public ServerResponseEntity<List<UserRights>> list() {
        return ServerResponseEntity.success( userRightsService.list(new LambdaQueryWrapper<UserRights>()
                .eq(UserRights::getServiceType, 1)
                .orderByAsc(UserRights::getSeq)
        ));
    }

    @GetMapping("/getMaxSeq" )
    @ApiOperation(value = "获取最大的序号", notes = "获取最大的序号")
    public ServerResponseEntity<Integer> getMaxSeq() {
        List<UserRights> list = userRightsService.list(new LambdaQueryWrapper<UserRights>().eq(UserRights::getServiceType, Constant.SERVICE_TYPE));
        Integer seq = 1;
        for (UserRights userRights:list){
            if (userRights.getSeq()> seq){
                seq = userRights.getSeq();
            }
        }
        seq++;
        return ServerResponseEntity.success(seq);
    }
}
