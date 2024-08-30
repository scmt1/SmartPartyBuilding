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

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.yami.shop.bean.app.dto.ShopHeadInfoDto;
import com.yami.shop.bean.app.param.SendSmsParam;
import com.yami.shop.bean.dto.ShopCreateInfoDTO;
import com.yami.shop.bean.dto.ShopSigningInfoDTO;
import com.yami.shop.bean.enums.*;
import com.yami.shop.bean.event.EsProductUpdateEvent;
import com.yami.shop.bean.model.OfflineHandleEvent;
import com.yami.shop.bean.model.ShopDetail;
import com.yami.shop.bean.param.AuditingInfoParam;
import com.yami.shop.bean.param.OfflineHandleEventAuditParam;
import com.yami.shop.bean.param.ShopSearchParam;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.common.util.PrincipalUtil;
import com.yami.shop.security.platform.util.SecurityUtils;
import com.yami.shop.service.OfflineHandleEventService;
import com.yami.shop.service.ShopDetailService;
import com.yami.shop.service.SmsLogService;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;


/**
 * 商家详细信息
 * @author yami
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/platform/shopDetail")
@Api(tags="店铺基本信息相关接口")
public class ShopDetailController {

    @Value("${yami.expose.operation.auth:}")
    private Boolean permission;

    private final ShopDetailService shopDetailService;
    private final OfflineHandleEventService offlineHandleEventService;
    private final ApplicationEventPublisher eventPublisher;
    private final SmsLogService smsLogService;
    private final ShopEmployeeService shopEmployeeService;


    @GetMapping("/info")
    @ApiOperation(value = "根据店铺id获取店铺基本信息", notes = "根据店铺id获取店铺基本信息")
    @ApiImplicitParam(name = "shopId", value = "店铺id")
    public ServerResponseEntity<ShopDetail> getInfo(@RequestParam Long shopId) {
        ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(shopId);
        ShopEmployee shopEmployee = shopEmployeeService.getOne(Wrappers.lambdaQuery(ShopEmployee.class).eq(ShopEmployee::getShopId, shopId).eq(ShopEmployee::getType, PositionType.ADMIN.value()));
        if (Objects.nonNull(shopEmployee)) {
            shopDetail.setMerchantAccount(shopEmployee.getUsername());
            shopDetail.setAccountStatus(shopEmployee.getStatus());
            if (BooleanUtil.isFalse(permission)) {
                if (StrUtil.isNotBlank(shopDetail.getTel())){
                    shopDetail.setTel(PhoneUtil.hideBetween(shopDetail.getTel()).toString());
                }
                if (PrincipalUtil.isMobile(shopDetail.getMerchantAccount())) {
                    shopDetail.setMerchantAccount(PhoneUtil.hideBetween(shopDetail.getMerchantAccount()).toString());
                }
            }
        }
        return ServerResponseEntity.success(shopDetail);
    }

    @GetMapping("/getMerchantInfo")
    @ApiOperation(value = "根据店铺id获取店铺商家账号信息", notes = "根据店铺id获取店铺商家账号信息")
    @ApiImplicitParam(name = "shopId", value = "店铺id")
    public ServerResponseEntity<ShopEmployee> getMerchantInfo(@RequestParam Long shopId) {
        ShopEmployee shopEmployee = shopEmployeeService.getMerchantInfoByShopId(shopId);
        return ServerResponseEntity.success(shopEmployee);
    }

    @PutMapping("/updateMerchantInfo")
    @ApiOperation(value = "更新店铺商家账号信息", notes = "更新店铺商家账号信息")
    public ServerResponseEntity<Void> updateMerchantInfo(@RequestBody ShopEmployee shopEmployee) {
        if (Objects.equals(shopEmployee.getShopId(), Constant.MAIN_SHOP) && BooleanUtil.isFalse(permission)) {
            throw new YamiShopBindException("yami.no.auth");
        }
        shopEmployeeService.updateMerchantInfo(shopEmployee);
        return ServerResponseEntity.success();
    }

    @PostMapping("/createShop")
    @ApiOperation(value = "新建店铺", notes = "新建店铺")
    public ServerResponseEntity<Void> createShop(@RequestBody @Valid ShopCreateInfoDTO shopCreateInfoDTO) {
        Long userId = SecurityUtils.getSysUser().getUserId();
        shopDetailService.platformCreateShop(shopCreateInfoDTO, userId);
        return ServerResponseEntity.success();
    }

    @PostMapping("/sendCode")
    @ApiOperation(value = "发送申请开店验证码", notes = "发送申请开店验证码")
    public ServerResponseEntity<Void> sendCode(@Valid @RequestBody SendSmsParam sendSmsParam) {
        int count = shopEmployeeService.count(Wrappers.lambdaQuery(ShopEmployee.class).eq(ShopEmployee::getMobile, sendSmsParam.getMobile()));
        if (count > 0) {
            // 手机号已存在
            throw new YamiShopBindException("yami.phone.number.already.exists");
        }
        smsLogService.sendSms(SendType.VALID, null, sendSmsParam.getMobile(), Maps.newHashMap());
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "编辑店铺基本信息", notes = "编辑店铺基本信息")
    public ServerResponseEntity<Void> editShop(@RequestBody ShopDetail shopDetail) {
        if (Objects.isNull(shopDetail.getShopId())) {
            throw new YamiShopBindException("店铺id不能为空");
        }
        shopDetailService.updateShopDetail(shopDetail);
        eventPublisher.publishEvent(new EsProductUpdateEvent(shopDetail.getShopId(), null, EsOperationType.UPDATE_BY_SHOP_ID));
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateSigningInfo")
    @ApiOperation(value = "更新店铺签约信息", notes = "更新店铺签约信息")
    public ServerResponseEntity<Void> updateSigningInfo(@RequestBody @Valid ShopSigningInfoDTO shopSigningInfoDTO) {
        if (Objects.isNull(shopSigningInfoDTO.getShopId())) {
            throw new YamiShopBindException("店铺id不能为空");
        }
        shopDetailService.updateSigningInfo(shopSigningInfoDTO);
        return ServerResponseEntity.success();
    }

    @GetMapping("/getOfflineHandleEventByShopId/{shopId}")
    @PreAuthorize("@pms.hasPermission('shop:shopAuditing:info')")
    @ApiImplicitParam(name = "shopId", value = "店铺id")
    public ServerResponseEntity<OfflineHandleEvent> getOfflineHandleEventByShopId(@PathVariable("shopId") Long shopId) {
        OfflineHandleEvent offlineHandleEvent = offlineHandleEventService.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.SHOP.getValue(), shopId);
        return ServerResponseEntity.success(offlineHandleEvent);
    }

    /**
     * 下线店铺
     */
    @PostMapping("/offline")
    @PreAuthorize("@pms.hasPermission('shop:shopAuditing:audit')")
    public ServerResponseEntity<Void> offline(@RequestBody OfflineHandleEvent offlineHandleEvent) {
        Long sysUserId = SecurityUtils.getSysUser().getUserId();
        ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(offlineHandleEvent.getHandleId());
        if (shopDetail == null) {
            // 未找到该店铺信息
            throw new YamiShopBindException("yami.store.not.exist");
        }
        if (!Objects.equals(shopDetail.getShopStatus(), ShopStatus.OPEN.value()) && !Objects.equals(shopDetail.getShopStatus(), ShopStatus.STOP.value())) {
            // 店铺不处于营业或停业状态,不能进行下线
            throw new YamiShopBindException("yami.store.offline.check");
        }
        shopDetailService.offline(shopDetail, offlineHandleEvent.getOfflineReason(), sysUserId);

        eventPublisher.publishEvent(new EsProductUpdateEvent(shopDetail.getShopId(), null, EsOperationType.UPDATE_BY_SHOP_ID));
        return ServerResponseEntity.success();
    }

    /**
     * 店铺审核重新开店
     */
    @PostMapping("/auditShop")
    @PreAuthorize("@pms.hasPermission('shop:shopAuditing:audit')")
    @ApiOperation(value = "店铺违规审核", notes = "店铺违规审核")
    public ServerResponseEntity<Void> auditOfflineShop(@RequestBody OfflineHandleEventAuditParam offlineHandleEventAuditParam) {
        Long sysUserId = SecurityUtils.getSysUser().getUserId();
        shopDetailService.auditOfflineShop(offlineHandleEventAuditParam, sysUserId);
        return ServerResponseEntity.success();
    }

    @GetMapping("/checkShopName")
    @ApiOperation(value = "检查店铺名称是否已存在", notes = "检查店铺名称是否已存在")
    public ServerResponseEntity<Boolean> checkShopName(@RequestParam(value = "shopName") String shopName) {
        int count = shopDetailService.count(Wrappers.lambdaQuery(ShopDetail.class)
                .eq(ShopDetail::getShopName, shopName)
                .ne(ShopDetail::getShopStatus, ShopStatus.NOTOPEN.value())
        );
        return ServerResponseEntity.success(count > 0);
    }

    @GetMapping("/exportShop")
    @ApiOperation(value = "导出店铺列表", notes = "导出店铺列表")
    public void exportShop(AuditingInfoParam auditingInfoParam, HttpServletResponse response) {
        shopDetailService.exportShop(auditingInfoParam, response);
    }

    private String generateKey(Map<String, String> values) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(values.toString().getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException nsae) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).", nsae);
        }
    }

    @GetMapping("/searchShops")
    @ApiOperation(value = "搜索店铺", notes = "根据店铺名称搜索店铺")
    public ServerResponseEntity<IPage<ShopHeadInfoDto>> searchShops(PageParam<ShopDetail> page, ShopSearchParam shopSearchParam) {
        IPage<ShopHeadInfoDto> shopPage = shopDetailService.renovationShopPage(page, shopSearchParam);
        return ServerResponseEntity.success(shopPage);
    }

}
