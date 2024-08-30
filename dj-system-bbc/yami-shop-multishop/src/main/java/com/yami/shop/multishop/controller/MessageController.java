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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.MessageStatus;
import com.yami.shop.bean.model.Message;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * @author lgh on 2018/10/15.
 */
@RestController
@RequestMapping("/admin/message")
@Api(tags = "留言消息接口")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页获取留言消息", notes = "分页获取留言消息")
    public ServerResponseEntity<IPage<Message>> page(Message message,PageParam<Message> page) {
        IPage<Message> messages = messageService.page(page, new LambdaQueryWrapper<Message>()
                .like(StrUtil.isNotBlank(message.getUserName()), Message::getUserName, message.getUserName())
                .eq(message.getStatus() != null, Message::getStatus, message.getStatus()));
        return ServerResponseEntity.success(messages);
    }

    /**
     * 获取信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据id获取留言消息", notes = "根据id获取留言消息")
    public ServerResponseEntity<Message> info(@PathVariable("id") Long id) {
        Message message = messageService.getById(id);
        return ServerResponseEntity.success(message);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation(value = "保存留言消息", notes = "保存留言消息")
    public ServerResponseEntity<Void> save(@RequestBody Message message) {
        messageService.save(message);
        return ServerResponseEntity.success();
    }

    /**
     * 修改
     */
    @PutMapping
    @ApiOperation(value = "修改留言消息", notes = "修改留言消息")
    public ServerResponseEntity<Void> update(@RequestBody Message message) {
        messageService.updateById(message);
        return ServerResponseEntity.success();
    }

    /**
     * 公开留言
     */
    @PutMapping("/release/{id}")
    @ApiOperation(value = "公开留言", notes = "公开留言")
    public ServerResponseEntity<Void> release(@PathVariable("id") Long id) {
        Message message = new Message();
        message.setId(id);
        message.setStatus(MessageStatus.RELEASE.value());
        messageService.updateById(message);
        return ServerResponseEntity.success();
    }

    /**
     * 取消公开留言
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation(value = "取消公开留言", notes = "取消公开留言")
    public ServerResponseEntity<Void> cancel(@PathVariable("id") Long id) {
        Message message = new Message();
        message.setId(id);
        message.setStatus(MessageStatus.CANCEL.value());
        messageService.updateById(message);
        return ServerResponseEntity.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除", notes = "删除")
    public ServerResponseEntity<Void> delete(@PathVariable Long[] ids) {
        messageService.removeByIds(Arrays.asList(ids));
        return ServerResponseEntity.success();
    }
}
