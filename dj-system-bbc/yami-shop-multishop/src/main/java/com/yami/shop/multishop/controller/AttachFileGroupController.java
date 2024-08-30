/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.multishop.controller;

import com.yami.shop.bean.model.AttachFileGroup;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.multishop.util.SecurityUtils;
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
@Api(tags = "文件分组接口")
public class AttachFileGroupController {

    @Autowired
    private AttachFileGroupService attachFileGroupService;

    @GetMapping("/list")
    @ApiOperation(value = "根据分组类型获取分组列表", notes = "根据分组类型获取分组列表")
    @ApiImplicitParam(name = "type", value = "分组类型 1:图片 2:视频 3:文件", required = true, dataType = "Integer", defaultValue = "1")
    public ServerResponseEntity<List<AttachFileGroup>> list(@RequestParam(value = "type", defaultValue = "1") Integer type) {
        List<AttachFileGroup> attachFileGroupPage = attachFileGroupService.list(SecurityUtils.getShopUser().getShopId(),type);
        return ServerResponseEntity.success(attachFileGroupPage);
    }

    @GetMapping
    @ApiOperation(value = "根据分组id获取分组", notes = "根据分组id获取分组")
    @ApiImplicitParam(name = "attachFileGroupId", value = "分组Id", required = true, dataType = "Long")
    public ServerResponseEntity<AttachFileGroup> getByAttachFileGroupId(@RequestParam Long attachFileGroupId) {
        AttachFileGroup attachFileGroup = attachFileGroupService.getByAttachFileGroupId(attachFileGroupId);
        if (!Objects.equals(SecurityUtils.getShopUser().getShopId(), attachFileGroup.getShopId())) {
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(attachFileGroup);
    }

    @PostMapping
    @ApiOperation(value = "保存文件分组", notes = "保存文件分组")
    public ServerResponseEntity<Void> save(@Valid @RequestBody AttachFileGroup attachFileGroup) {
        attachFileGroup.setAttachFileGroupId(null);
        attachFileGroup.setShopId(SecurityUtils.getShopUser().getShopId());
        attachFileGroupService.saveGroup(attachFileGroup);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新文件分组", notes = "更新文件分组")
    public ServerResponseEntity<Void> update(@Valid @RequestBody AttachFileGroup attachFileGroup) {
        attachFileGroup.setShopId(SecurityUtils.getShopUser().getShopId());
        attachFileGroupService.update(attachFileGroup);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "根据分组id删除文件分组", notes = "根据分组id删除文件分组")
    public ServerResponseEntity<Void> delete(@RequestParam Long attachFileGroupId) {
        AttachFileGroup dbAttachFileGroup = attachFileGroupService.getByAttachFileGroupId(attachFileGroupId);
        if (!Objects.equals(dbAttachFileGroup.getShopId(), SecurityUtils.getShopUser().getShopId())) {
            throw new YamiShopBindException("yami.no.auth");
        }
        attachFileGroupService.deleteById(attachFileGroupId);
        return ServerResponseEntity.success();
    }
}
