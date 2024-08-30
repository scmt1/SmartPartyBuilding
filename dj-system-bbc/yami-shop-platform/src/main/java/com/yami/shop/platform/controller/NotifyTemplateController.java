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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.RemindType;
import com.yami.shop.bean.enums.SendType;
import com.yami.shop.bean.model.NotifyTemplate;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.NotifyTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


/**
 *
 *
 * @author lhd
 * @date 2020-07-01 16:13:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/platform/notifyTemplate")
@Api(tags = "消息模板")
public class NotifyTemplateController {

    private final NotifyTemplateService notifyTemplateService;

    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('platform:notifyTemplate:page')" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<NotifyTemplate>> getNotifyTemplatePage(PageParam<NotifyTemplate> page, NotifyTemplate notifyTemplate) {

        IPage<NotifyTemplate> templatePage = notifyTemplateService.page(page, new LambdaQueryWrapper<NotifyTemplate>()
                .eq(Objects.nonNull(notifyTemplate.getSendType()),NotifyTemplate::getSendType,notifyTemplate.getSendType())
                .ne(NotifyTemplate::getSendType, SendType.CUSTOMIZE.getValue())
                .orderByDesc(NotifyTemplate::getStatus).orderByDesc(NotifyTemplate::getCreateTime));
        if(CollectionUtils.isEmpty(templatePage.getRecords())){
            return ServerResponseEntity.success();
        }
        for (NotifyTemplate  template: templatePage.getRecords()) {
            List<Integer> templateList = getTemplateList(template.getTemplateTypes());
            for (Integer type : templateList) {
                template.setSms(Objects.equals(type,RemindType.SMS.value()) || template.getSms());
                template.setSub(Objects.equals(type,RemindType.MP.value()) || template.getSub());
                template.setApp(Objects.equals(type,RemindType.MINI.value()) || template.getApp());
            }
            template.setTemplateTypeList(templateList);
        }
        return ServerResponseEntity.success(templatePage);
    }

    private List<Integer> getTemplateList(String templateTypes) {
        String[] templateTypeList = templateTypes.split(StrUtil.COMMA);
        List<Integer> templates = new ArrayList<>();
        for (String templateStr : templateTypeList) {
            if (StrUtil.isBlank(templateStr)) {
                continue;
            }
            templates.add(Integer.valueOf(templateStr));
        }
        return templates;
    }

    @GetMapping("/info/{templateId}" )
    @ApiOperation(value = "查询模板信息", notes = "查询模板信息")
    @ApiImplicitParam(name = "templateId", value = "模板id")
    public ServerResponseEntity<NotifyTemplate> getById(@PathVariable("templateId") Long templateId) {
        NotifyTemplate template = notifyTemplateService.getById(templateId);
        template.setTemplateTypeList(getTemplateList(template.getTemplateTypes()));
        return ServerResponseEntity.success(template);
    }

    @PostMapping
    @PreAuthorize("@pms.hasPermission('platform:notifyTemplate:save')" )
    @ApiOperation(value = "新增模板", notes = "新增模板")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid NotifyTemplate notifyTemplate) {
        notifyTemplate.setCreateTime(new Date());
        notifyTemplate.setStatus(1);
        String message = notifyTemplate.getMessage();
        //验证消息内容长度是否超出数据库限制
        if (message.length() >255){
            throw new YamiShopBindException("yami.message.leng.exceed.limit");
        }
        List<NotifyTemplate> notifyTemplates = notifyTemplateService.list(new LambdaQueryWrapper<NotifyTemplate>().eq(NotifyTemplate::getSendType, notifyTemplate.getSendType()));
        if(CollectionUtils.isNotEmpty(notifyTemplates)){
            // 已经存在当前消息类型的短信，请去进行修改操作
            throw new YamiShopBindException("yami.select.notify.type.check");
        }
        SendType sendType = SendType.instance(notifyTemplate.getSendType());
        if(Objects.nonNull(sendType)) {
            notifyTemplate.setMsgType(sendType.getType());
        }
        if (CollUtil.isNotEmpty(notifyTemplate.getTemplateTypeList())) {
            notifyTemplate.setTemplateTypes(arrayChangeList(notifyTemplate.getTemplateTypeList()));
        }
        return ServerResponseEntity.success(notifyTemplateService.save(notifyTemplate));
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('platform:notifyTemplate:update')" )
    @ApiOperation(value = "修改模板", notes = "修改模板")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid NotifyTemplate notifyTemplate) {
        List<NotifyTemplate> notifyTemplates = notifyTemplateService.list(new LambdaQueryWrapper<NotifyTemplate>()
                .eq(NotifyTemplate::getSendType, notifyTemplate.getSendType()).ne(NotifyTemplate::getTemplateId,notifyTemplate.getTemplateId()));
        if(CollectionUtils.isNotEmpty(notifyTemplates)){
            // 已经存在当前消息类型的短信，请去进行修改操作
            throw new YamiShopBindException("yami.select.notify.type.check");
        }
        if (CollUtil.isNotEmpty(notifyTemplate.getTemplateTypeList())) {
            notifyTemplate.setTemplateTypes(arrayChangeList(notifyTemplate.getTemplateTypeList()));
        }
        SendType sendType = SendType.instance(notifyTemplate.getSendType());
        if(Objects.nonNull(sendType)) {
            notifyTemplate.setMsgType(sendType.getType());
        }
        return ServerResponseEntity.success(notifyTemplateService.updateById(notifyTemplate));
    }

    private String getRepeatValue(List<Integer> templateTypeList,List<Integer> shopTemplateTypeList){
        ArrayList<Integer> repeatValue = new ArrayList<>();
        for (Integer integer : templateTypeList) {
            if (shopTemplateTypeList.contains(integer)){
                repeatValue.add(integer);
            }
        }
        String s = repeatValue.toString().replace(" ","");
        return s.substring(1,s.length()-1);
    }
    private String arrayChangeList(List<Integer> templateTypeList) {
        StringBuilder templateTypes = new StringBuilder();
        for (Integer templateType : templateTypeList) {
            templateTypes.append(templateType);
            templateTypes.append(StrUtil.COMMA);
        }
        templateTypes.deleteCharAt(templateTypes.length() - 1);
        return templateTypes.toString();
    }

    @DeleteMapping("/{templateId}" )
    @PreAuthorize("@pms.hasPermission('platform:notifyTemplate:updateSts')" )
    @ApiOperation(value = "状态变更", notes = "状态变更")
    @ApiImplicitParam(name = "templateId", value = "模板id")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long templateId) {
        NotifyTemplate template = notifyTemplateService.getById(templateId);
        template.setStatus(template.getStatus() == 1? 0:1);
        return ServerResponseEntity.success(notifyTemplateService.updateById(template));
    }

}
