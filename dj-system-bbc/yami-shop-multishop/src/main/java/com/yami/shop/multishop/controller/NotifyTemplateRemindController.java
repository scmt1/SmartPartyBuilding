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


import javax.validation.Valid;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yami.shop.bean.enums.SendType;
import com.yami.shop.bean.model.NotifyTemplateRemind;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.NotifyTemplateRemindService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.common.util.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Objects;


/**
 * 消息提醒设置表
 *
 * @author LGH
 * @date 2022-10-26 15:51:49
 */
@RestController
@RequestMapping("/multishop/notifyTemplateRemind")
@Api(tags = "消息提醒设置表")
public class NotifyTemplateRemindController {

    @Autowired
    private NotifyTemplateRemindService notifyTemplateRemindService;

    @GetMapping("/page")
    @ApiOperation(value = "获取消息提醒设置表列表", notes = "分页获取消息提醒设置表列表")
    public ServerResponseEntity<IPage<NotifyTemplateRemind>> getNotifyTemplateRemindPage(PageParam<NotifyTemplateRemind> page, NotifyTemplateRemind notifyTemplateRemind) {
        notifyTemplateRemind.setShopId(SecurityUtils.getShopUser().getShopId());
        Page<NotifyTemplateRemind> notifyTemplateRemindPage = notifyTemplateRemindService.pageNotifyTemplateRemind(page, notifyTemplateRemind);
        //没有消息提醒设置记录，默认开启提醒
        notifyTemplateRemindPage.getRecords().forEach(entity -> {
            if(Objects.isNull(entity.getIsRemind())){
                entity.setIsRemind(1);
            }
            entity.setMenu(SendType.instance(entity.getSendType()).getMenu());
            entity.setNodeName(SendType.instance(entity.getSendType()).getNodeName());
        });
        return ServerResponseEntity.success(notifyTemplateRemindPage);
    }

    @GetMapping("/info/{notifyTemplateRemindId}")
    @ApiOperation(value = "获取消息提醒设置表", notes = "根据notifyTemplateRemindId获取消息提醒设置表")
    @ApiImplicitParam(name = "notifyTemplateRemindId", value = "消息提醒设置表", required = true, dataType = "Long")
    public ServerResponseEntity<NotifyTemplateRemind> getById(@PathVariable("notifyTemplateRemindId") Long notifyTemplateRemindId) {
        return ServerResponseEntity.success(notifyTemplateRemindService.getById(notifyTemplateRemindId));
    }

    @PostMapping
    @ApiOperation(value = "保存消息提醒设置表", notes = "保存消息提醒设置表")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid NotifyTemplateRemind notifyTemplateRemind) {
        return ServerResponseEntity.success(notifyTemplateRemindService.save(notifyTemplateRemind));
    }

    @PutMapping
    @ApiOperation(value = "更新消息提醒设置表", notes = "更新消息提醒设置表")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid NotifyTemplateRemind notifyTemplateRemind) {
        notifyTemplateRemind.setShopId(SecurityUtils.getShopUser().getShopId());
        //提醒设置记录为空，则保存记录
        if(Objects.isNull(notifyTemplateRemind.getNotifyTemplateRemindId())){
            notifyTemplateRemindService.save(notifyTemplateRemind);
        }else{
            notifyTemplateRemindService.updateById(notifyTemplateRemind);
        }
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/{notifyTemplateRemindId}")
    @ApiOperation(value = "删除消息提醒设置表", notes = "根据消息提醒设置表id删除消息提醒设置表")
    @ApiImplicitParam(name = "notifyTemplateRemindId", value = "消息提醒设置表", required = true, dataType = "Long")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long notifyTemplateRemindId) {
        return ServerResponseEntity.success(notifyTemplateRemindService.removeById(notifyTemplateRemindId));
    }
}
