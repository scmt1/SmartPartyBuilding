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
import com.yami.shop.bean.model.CompanyAuditing;
import com.yami.shop.bean.param.CompanyInfoAuditParam;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.platform.util.SecurityUtils;
import com.yami.shop.service.CompanyAuditingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


/**
 * @author chiley
 * @date 2022/9/21 10:23
 */

@RestController
@RequestMapping("/platform/companyAuditing")
@Api(tags = "审核工商信息接口")
public class CompanyAuditingController {

    @Autowired
    private CompanyAuditingService companyAuditingService;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('shop:companyAuditing:page')")
    @ApiOperation(value = "分页获取待审核的工商信息", notes = "分页获取待审核的工商信息")
    public ServerResponseEntity<IPage<CompanyAuditing>> getCompanyAuditingPage(PageParam<CompanyAuditing> page, CompanyInfoAuditParam auditParam) {
        return ServerResponseEntity.success(companyAuditingService.page(page, auditParam));
    }

    @PutMapping("/audit")
    @ApiOperation(value = "审核签约信息", notes = "审核签约信息")
    @PreAuthorize("@pms.hasPermission('shop:companyAuditing:audit')")
    public ServerResponseEntity<Void> audit(@Valid @RequestBody CompanyAuditing companyAuditing) {
        companyAuditing.setAuditorId(SecurityUtils.getSysUser().getUserId());
        companyAuditingService.audit(companyAuditing);
        return ServerResponseEntity.success();
    }

    @GetMapping("/auditInfo")
    @ApiOperation(value = "查看申请审核情况", notes = "查看申请审核情况")
    public ServerResponseEntity<CompanyAuditing> auditInfo(@RequestParam("shopId") Long shopId) {
        return ServerResponseEntity.success(companyAuditingService.getAuditInfo(shopId));
    }
}
