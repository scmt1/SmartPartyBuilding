/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.controller;

import com.yami.shop.bean.model.AttachFileGroup;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.AttachFileGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author chendt
 * @date 2021/7/7 17:18
 */
@RestController
@RequestMapping("/admin/fileGroup")
@Api(tags = "文件分组")
public class AttachFileGroupController {

    @Autowired
    private AttachFileGroupService attachFileGroupService;

    @GetMapping("/list")
    @ApiOperation(value = "获取列表", notes = "分页获取列表")
    @ApiImplicitParam(name = "type", value = "文件类型： 1:图片 2:视频 3:文件")
    public ServerResponseEntity<List<AttachFileGroup>> list(@RequestParam(value = "type", defaultValue = "1") Integer type) {
        List<AttachFileGroup> attachFileGroupPage = attachFileGroupService.list(Constant.PLATFORM_SHOP_ID,type);
        return ServerResponseEntity.success(attachFileGroupPage);
    }

    @GetMapping
    @ApiOperation(value = "获取", notes = "根据attachFileGroupId获取")
    @ApiImplicitParam(name = "attachFileGroupId", value = "文件分组id")
    public ServerResponseEntity<AttachFileGroup> getByAttachFileGroupId(@RequestParam Long attachFileGroupId) {
        return ServerResponseEntity.success(attachFileGroupService.getByAttachFileGroupId(attachFileGroupId));
    }

    @PostMapping
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody AttachFileGroup attachFileGroup) {
        attachFileGroup.setAttachFileGroupId(null);
        attachFileGroup.setShopId(Constant.PLATFORM_SHOP_ID);
        attachFileGroupService.saveGroup(attachFileGroup);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新", notes = "更新")
    public ServerResponseEntity<Void> update(@Valid @RequestBody AttachFileGroup attachFileGroup) {
        attachFileGroup.setShopId(Constant.PLATFORM_SHOP_ID);
        attachFileGroupService.update(attachFileGroup);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除", notes = "根据id删除")
    @ApiImplicitParam(name = "attachFileGroupId", value = "文件分组id")
    public ServerResponseEntity<Void> delete(@RequestParam Long attachFileGroupId) {
        AttachFileGroup dbAttachFileGroup = attachFileGroupService.getByAttachFileGroupId(attachFileGroupId);
        if (!Objects.equals(dbAttachFileGroup.getShopId(), Constant.PLATFORM_SHOP_ID)) {
            throw new YamiShopBindException("未授权");
        }
        attachFileGroupService.deleteById(attachFileGroupId);
        return ServerResponseEntity.success();
    }
}
