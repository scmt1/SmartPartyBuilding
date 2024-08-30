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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.SendType;
import com.yami.shop.bean.model.NotifyTemplate;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.NotifyTemplateService;
import com.yami.shop.user.common.service.UserTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;


/**
 *
 *
 * @author lhd
 * @date 2020-07-01 16:13:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/platform/sendTagNotify")
@Api(tags = "平台消息模板")
public class PlatformNotifyController {

    private final NotifyTemplateService notifyTemplateService;
    private final UserTagService userTagService;

    @GetMapping("/page" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<NotifyTemplate>> getNotifyTemplatePage(PageParam<NotifyTemplate> page, NotifyTemplate notifyTemplate) {
        IPage<NotifyTemplate> templatePage = notifyTemplateService.pageTagNotify(page);
        return ServerResponseEntity.success(templatePage);
    }

    @GetMapping("/info/{templateId}" )
    @ApiOperation(value = "获取模板信息", notes = "获取模板信息")
    @ApiImplicitParam(name = "templateId", value = "模板id")
    public ServerResponseEntity<NotifyTemplate> getById(@PathVariable("templateId") Long templateId) {
        NotifyTemplate template = notifyTemplateService.getInfoById(templateId);
        return ServerResponseEntity.success(template);
    }

    @SysLog("新增" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('platform:notifyTemplate:save')" )
    @ApiOperation(value = "新增", notes = "新增")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid NotifyTemplate notifyTemplate) {
        if(CollectionUtils.isEmpty(notifyTemplate.getSelTagIds())){
            throw new YamiShopBindException("yami.notify.tag.msg");
        }
        notifyTemplate.setCreateTime(new Date());
        notifyTemplate.setStatus(1);
        notifyTemplate.setSendType(SendType.CUSTOMIZE.getValue());
        // 站内消息1
        notifyTemplate.setTemplateTypes("3");
        notifyTemplateService.saveTagNotify(notifyTemplate);
        return ServerResponseEntity.success();
    }

    @SysLog("修改" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('platform:notifyTemplate:update')" )
    @ApiOperation(value = "修改", notes = "修改")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid NotifyTemplate notifyTemplate) {
        notifyTemplateService.updateInfoById(notifyTemplate);
        return ServerResponseEntity.success(true);
    }

    @DeleteMapping("/{templateId}" )
    @ApiOperation(value = "删除消息模板", notes = "删除消息模板")
    @ApiImplicitParam(name = "templateId", value = "模板id")
    public ServerResponseEntity<Boolean> deleteUserTag(@PathVariable Long templateId) {
        notifyTemplateService.deleteTemplateInfoById(templateId);
        return ServerResponseEntity.success(true);
    }

}
