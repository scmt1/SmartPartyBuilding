/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.platform.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.SysAccessKey;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.SysAccessKeyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;


/**
 * 系统访问密钥
 *
 * @author YXF
 * @date 2021-10-19 14:41:38
 */
@Api(tags = "系统访问密钥")
@RestController
@AllArgsConstructor
@RequestMapping("/platform/sysAccessKey" )
public class SysAccessKeyController {

    private final SysAccessKeyService sysAccessKeyService;

    @GetMapping("/page" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<SysAccessKey>> getSysKeyPage(PageParam<SysAccessKey> page, SysAccessKey sysAccessKey) {
        PageParam<SysAccessKey> keyPage = sysAccessKeyService.page(page, new LambdaQueryWrapper<SysAccessKey>()
                .like(StrUtil.isNotBlank(sysAccessKey.getName()), SysAccessKey::getName, sysAccessKey.getName())
                .like(StrUtil.isNotBlank(sysAccessKey.getAccessId()), SysAccessKey::getAccessId, sysAccessKey.getAccessId())
        );
        if (CollUtil.isNotEmpty(keyPage.getRecords())) {
            for (SysAccessKey accessKey : keyPage.getRecords()) {
                accessKey.setAccessKey(null);
            }
        }
        return ServerResponseEntity.success(keyPage);
    }

    @GetMapping("/info/{sysAccessKeyId}" )
    @ApiOperation(value = "查询系统访问密钥", notes = "通过id查询系统访问密钥")
    @ApiImplicitParam(name = "sysAccessKeyId", value = "系统访问密钥id")
    public ServerResponseEntity<SysAccessKey> getById(@PathVariable("sysAccessKeyId") Long sysAccessKeyId) {
        SysAccessKey sysAccessKey = sysAccessKeyService.getBySysAccessKeyId(sysAccessKeyId);
        return ServerResponseEntity.success(sysAccessKey);
    }

    @SysLog("新增系统访问密钥" )
    @PostMapping
    @ApiOperation(value = "新增系统访问密钥", notes = "新增系统访问密钥")
    @PreAuthorize("@pms.hasPermission('platform:sysAccessKey:save')" )
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid SysAccessKey sysAccessKey) {
        sysAccessKeyService.saveSysAccessKey(sysAccessKey);
        return ServerResponseEntity.success();
    }

    @SysLog("更新系统访问密钥" )
    @PutMapping
    @ApiOperation(value = "更新系统访问密钥", notes = "更新系统访问密钥")
    @PreAuthorize("@pms.hasPermission('platform:sysAccessKey:update')" )
    public ServerResponseEntity<Boolean> update(@RequestBody @Valid SysAccessKey sysAccessKey) {
        SysAccessKey sysAccessKeyDb = sysAccessKeyService.getBySysAccessKeyId(sysAccessKey.getSysAccessKeyId());
        sysAccessKeyService.updateSysAccessKey(sysAccessKey);
        sysAccessKeyService.removeCacheById(sysAccessKeyDb.getAccessId());
        return ServerResponseEntity.success();
    }

    @SysLog("重置系统访问密钥" )
    @PutMapping("/resetAccessKey")
    @ApiOperation(value = "重置系统访问密钥", notes = "重置系统访问密钥")
    @ApiImplicitParam(name = "sysAccessKeyId", value = "系统访问密钥id")
    @PreAuthorize("@pms.hasPermission('platform:sysAccessKey:reset')" )
    public ServerResponseEntity<Boolean> resetAccessKey(@RequestBody Long sysAccessKeyId) {
        if (Objects.isNull(sysAccessKeyId)) {
            // sysAccessKeyId 不能为空
            throw new YamiShopBindException("yami.sys.access.key.id.not.null");
        }
        SysAccessKey sysAccessKeyDb = sysAccessKeyService.getBySysAccessKeyId(sysAccessKeyId);
        if (Objects.isNull(sysAccessKeyDb)) {
            return ServerResponseEntity.success();
        }
        sysAccessKeyService.resetAccessKey(sysAccessKeyId);
        sysAccessKeyService.removeCacheById(sysAccessKeyDb.getAccessId());
        return ServerResponseEntity.success();
    }

    @SysLog("删除系统访问密钥" )
    @DeleteMapping("/{sysAccessKeyId}" )
    @ApiOperation(value = "删除系统访问密钥", notes = "通过id删除系统访问密钥")
    @ApiImplicitParam(name = "sysAccessKeyId", value = "系统访问密钥id")
    @PreAuthorize("@pms.hasPermission('platform:sysAccessKey:delete')" )
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long sysAccessKeyId) {
        SysAccessKey sysAccessKeyDb = sysAccessKeyService.getBySysAccessKeyId(sysAccessKeyId);
        if (Objects.isNull(sysAccessKeyDb)) {
            return ServerResponseEntity.success();
        }
        sysAccessKeyService.removeById(sysAccessKeyId);
        sysAccessKeyService.removeCacheById(sysAccessKeyDb.getAccessId());
        return ServerResponseEntity.success();
    }

}
