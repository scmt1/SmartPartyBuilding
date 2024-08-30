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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.Form;
import com.yami.shop.bean.model.FormItem;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.FormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
@RequestMapping("/platform/form")
@Api(tags = "数据报表")
public class FormController {

    @Autowired
    private FormService formService;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('platform:form:page')")
    @ApiOperation(value = "分页获取报表", notes = "分页获取报表")
    public ServerResponseEntity<IPage<Form>> page(Form form, PageParam<Form> page) {
        IPage<Form> formPage = formService.page(page, new LambdaQueryWrapper<Form>()
                .eq(Form::getShopId,Constant.PLATFORM_SHOP_ID)
                .like(StrUtil.isNotBlank(form.getFormName()),Form::getFormName,form.getFormName())
                .eq(Objects.nonNull(form.getTimeType()),Form::getTimeType,form.getTimeType())
                .orderByAsc(Form::getSeq)
        );
        return ServerResponseEntity.success(formPage);
    }

    @GetMapping("/info/{formId}")
    @PreAuthorize("@pms.hasPermission('platform:form:info')")
    @ApiOperation(value = "获取信息", notes = "获取信息")
    @ApiImplicitParam(name = "formId", value = "报表id")
    public ServerResponseEntity<Form> info(@PathVariable("formId") Long formId) {
        Form form = formService.getById(formId);
        return ServerResponseEntity.success(form);
    }

    @PostMapping
    @PreAuthorize("@pms.hasPermission('platform:form:save')")
    @ApiOperation(value = "保存", notes = "保存")
    public ServerResponseEntity<Void> save(@RequestBody @Valid Form form) {
        Date date = new Date();
        form.setCreateTime(date);
        form.setUpdateTime(date);
        if (!form.getRecommendForm()){
            Long shopId = Constant.PLATFORM_SHOP_ID;
            form.setShopId(shopId);
        }
        formService.save(form);
        //如果是推荐报表，则清除缓存
        if (form.getRecommendForm()){
            formService.removeCache();
        }
        return ServerResponseEntity.success();
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('platform:form:update')")
    @ApiOperation(value = "修改", notes = "修改")
    public ServerResponseEntity<Void> update(@RequestBody @Valid Form form) {
        if (!form.getRecommendForm()){
            Form formDb = formService.getById(form.getFormId());
            if (!Objects.equals(formDb.getShopId(),Constant.PLATFORM_SHOP_ID)){
                // 该报表数据有误，请刷新后重新输入
                throw new YamiShopBindException("yami.form.data.error");
            }
        }
        form.setUpdateTime(new Date());
        formService.saveOrUpdate(form);
        //如果是推荐报表，则清除缓存
        if (form.getRecommendForm()){
            formService.removeCache();
        }
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/{formId}")
    @PreAuthorize("@pms.hasPermission('platform:form:delete')")
    @ApiOperation(value = "删除报表", notes = "删除报表")
    @ApiImplicitParam(name = "formId", value = "报表id")
    public ServerResponseEntity<Void> delete(@PathVariable("formId") Long formId) {
        Form formDb = formService.getById(formId);
        formService.removeById(formId);
        //如果是推荐报表，则清除缓存
        if (Objects.isNull(formDb.getShopId())){
            formService.removeCache();
        }
        return ServerResponseEntity.success();
    }

    @GetMapping("/getFormItem")
    @ApiOperation(value = "获取报表项列表", notes = "获取报表项列表")
    @ApiImplicitParam(name = "type", value = "1:平台端  2：商家端")
    public ServerResponseEntity<List<FormItem>> getFormItem(@RequestParam("type")Integer type) {
        List<FormItem> formItemEnumList = formService.getFormItem(type, I18nMessage.getDbLang());
        return ServerResponseEntity.success(formItemEnumList);
    }

    @GetMapping("/formExcel")
    @PreAuthorize("@pms.hasPermission('platform:form:excel')")
    @ApiOperation(value = "生成报表", notes = "生成报表")
    public void formExcel(@RequestParam("formId") Long formId, HttpServletResponse response) {
        formService.formExcel(formId,response);
    }

    @GetMapping("/getRecommendFormPage")
    @ApiOperation(value = "分页获取推荐报表", notes = "分页获取推荐报表")
    public ServerResponseEntity<IPage<Form>> getRecommendFormPage(Form form, PageParam<Form> page) {
        IPage<Form> formPage = formService.getRecommendFormPage(page,form);
        return ServerResponseEntity.success(formPage);
    }

    @GetMapping("/getRecommendFormList")
    @ApiOperation(value = "获取推荐报表", notes = "获取推荐报表")
    public ServerResponseEntity<List<Form>> getRecommendFormList() {
        List<Form> formList = formService.getRecommendFormList();
        return ServerResponseEntity.success(formList);
    }
}
