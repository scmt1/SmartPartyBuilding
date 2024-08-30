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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.enums.AuditStatus;
import com.yami.shop.bean.model.CompanyAuditing;
import com.yami.shop.bean.model.ShopCompany;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.common.util.AuthUserContext;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.CompanyAuditingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.auth.AUTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @author chiley
 * @date 2022/9/21 10:22
 */

@RestController
@RequestMapping("/shop/companyAuditing")
@Api(tags = "变更工商信息接口")
public class CompanyAuditingController {

    @Autowired
    private CompanyAuditingService companyAuditingService;

    @PostMapping("/applyChangeCompanyInfo")
    @ApiOperation(value = "申请变更工商信息", notes = "申请变更工商信息")
    @PreAuthorize("@pms.hasPermission('shop:shopCompany:applyChange')")
    public ServerResponseEntity<Void> applyChangeCompanyInfo(@RequestBody @Valid ShopCompany shopCompany) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        String userId = SecurityUtils.getShopUser().getUserId();
        // 校验是否已经存在申请中记录
        CompanyAuditing latestCompanyAuditing = companyAuditingService.getOne(new LambdaQueryWrapper<CompanyAuditing>().eq(CompanyAuditing::getShopId, shopId).orderByDesc(CompanyAuditing::getCreateTime), false);
        if (Objects.nonNull(latestCompanyAuditing) && Objects.equals(latestCompanyAuditing.getStatus(), AuditStatus.WAITAUDIT.value())) {
            throw new YamiShopBindException("yami.shop.company.repeat.application");
        }
        shopCompany.setShopId(shopId);
        companyAuditingService.applyChangeCompanyInfo(shopCompany, userId);
        return ServerResponseEntity.success();
    }

    @PutMapping("/revoke")
    @ApiOperation(value = "撤销申请", notes = "撤销申请")
    @PreAuthorize("@pms.hasPermission('shop:shopCompany:revoke')")
    public ServerResponseEntity<Void> revoke(@RequestBody Long companyAuditingId) {
        CompanyAuditing companyAuditing = companyAuditingService.getById(companyAuditingId);
        if (Objects.isNull(companyAuditing)) {
            return ServerResponseEntity.success();
        }
        if (!Objects.equals(companyAuditing.getShopId(), SecurityUtils.getShopUser().getShopId())) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        companyAuditingService.revoke(companyAuditing);
        return ServerResponseEntity.success();
    }


    @GetMapping("/auditInfo")
    @ApiOperation(value = "查看申请审核情况", notes = "查看申请审核情况")
    public ServerResponseEntity<CompanyAuditing> auditInfo() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        return ServerResponseEntity.success(companyAuditingService.getAuditInfo(shopId));
    }
}
