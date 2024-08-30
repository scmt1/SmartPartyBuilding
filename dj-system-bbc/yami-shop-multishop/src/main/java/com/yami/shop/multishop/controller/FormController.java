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
import com.yami.shop.bean.model.Form;
import com.yami.shop.bean.model.FormItem;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.FormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author lgh on 2018/11/26.
 */
@RestController
@RequestMapping("/admin/form")
@Api(tags="数据报表接口")
public class FormController {

    @Autowired
    private FormService formService;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('admin:form:page')")
    @ApiOperation(value = "根据参数分页获取报表信息", notes = "根据参数分页获取报表信息")
    public ServerResponseEntity<IPage<Form>> page(Form form, PageParam<Form> page) {
        IPage<Form> iPage = formService.page(page, new LambdaQueryWrapper<Form>()
                .eq(Form::getShopId,SecurityUtils.getShopUser().getShopId())
                .like(StrUtil.isNotBlank(form.getFormName()),Form::getFormName,form.getFormName())
                .eq(Objects.nonNull(form.getTimeType()),Form::getTimeType,form.getTimeType())
                .orderByAsc(Form::getSeq));
        return ServerResponseEntity.success(iPage);
    }

    @GetMapping("/info/{formId}")
    @ApiOperation(value = "根据报表id获取报表信息", notes = "根据报表id获取报表信息")
    public ServerResponseEntity<Form> info(@PathVariable("formId") Long formId) {
        Form form = formService.getById(formId);
        return ServerResponseEntity.success(form);
    }

    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin:form:save')")
    @ApiOperation(value = "新增报表", notes = "新增报表")
    public ServerResponseEntity<Void> save(@RequestBody @Valid Form form) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        form.setShopId(shopId);
        Date date = new Date();
        form.setCreateTime(date);
        form.setUpdateTime(date);
        formService.save(form);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin:form:update')")
    @ApiOperation(value = "修改报表", notes = "修改报表")
    public ServerResponseEntity<Void> update(@RequestBody @Valid Form form) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        Form formServiceById = formService.getById(form.getFormId());
        if (!Objects.equals(formServiceById.getShopId(),shopId)){
            // 您没有权限更改该报表的数据
            throw new YamiShopBindException("yami.no.auth.update.form");
        }
        form.setUpdateTime(new Date());
        formService.saveOrUpdate(form);
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/{formId}")
    @PreAuthorize("@pms.hasPermission('admin:form:delete')")
    @ApiOperation(value = "根据报表id删除报表", notes = "根据报表id删除报表")
    public ServerResponseEntity<Void> delete(@PathVariable("formId") Long formId) {
        formService.removeById(formId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/getFormItem")
    @ApiOperation(value = "获取报表项列表", notes = "获取报表项列表")
    public ServerResponseEntity<List<FormItem>> getFormItem() {
        List<FormItem> formItemEnumList = formService.getFormItem(2, I18nMessage.getDbLang());
        return ServerResponseEntity.success(formItemEnumList);
    }

    @GetMapping("/formExcel")
    @PreAuthorize("@pms.hasPermission('admin:form:formExcel')")
    @ApiOperation(value = "根据报表id生成对应的报表统计数据", notes = "根据报表id生成对应的报表统计数据")
    public void formExcel(@RequestParam("formId") Long formId, HttpServletResponse response) {
        formService.formExcel(formId,response);
    }

    @GetMapping("/getRecommendFormList")
    @ApiOperation(value = "获取全部推荐报表", notes = "获取全部推荐报表")
    public ServerResponseEntity<List<Form>> getRecommendFormList() {
        List<Form> formList = formService.getRecommendFormList();
        return ServerResponseEntity.success(formList);
    }

}
