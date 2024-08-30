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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.yami.shop.bean.app.dto.ShopHeadInfoDto;
import com.yami.shop.bean.enums.EsOperationType;
import com.yami.shop.bean.enums.OfflineHandleEventType;
import com.yami.shop.bean.enums.SendType;
import com.yami.shop.bean.enums.ShopStatus;
import com.yami.shop.bean.event.EsProductUpdateEvent;
import com.yami.shop.bean.model.OfflineHandleEvent;
import com.yami.shop.bean.model.ShopAuditing;
import com.yami.shop.bean.model.ShopDetail;
import com.yami.shop.bean.param.OfflineHandleEventAuditParam;
import com.yami.shop.bean.param.ShopDetailParam;
import com.yami.shop.bean.vo.ShopStatusInfoVO;
import com.yami.shop.common.bean.SysServiceConfig;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.*;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;


/**
 * @author lgh on 2018/08/29.
 */
@RestController
@RequestMapping("/shop/shopDetail")
@Api(tags="店铺基本信息接口")
public class ShopDetailController {

    @Autowired
    private ShopDetailService shopDetailService;
    @Autowired
    private ShopEmployeeService shopEmployeeService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private OfflineHandleEventService offlineHandleEventService;
    @Autowired
    private SmsLogService smsLogService;
    @Autowired
    private ShopAuditingService shopAuditingService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/info")
    @ApiOperation(value = "获取店铺基本信息", notes = "获取店铺基本信息")
    public ServerResponseEntity<ShopDetail> info() {
        ShopDetail shopDetail = shopDetailService.getById(SecurityUtils.getShopUser().getShopId());
        ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
        shopDetail.setUserName(shopEmployee.getUsername());
        shopDetail.setMerchantAccount(shopEmployee.getMobile());
        shopDetail.setAccountStatus(shopEmployee.getStatus());
        shopDetail.setEmployeeId(SecurityUtils.getShopUser().getEmployeeId());
        return ServerResponseEntity.success(shopDetail);
    }

    @GetMapping("/getShopInfo")
    @ApiOperation(value = "店铺信息（装修）", notes = "获取的店铺信息（装修）")
    public ServerResponseEntity<ShopHeadInfoDto> getShopInfo() {
        ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(SecurityUtils.getShopUser().getShopId());
        ShopHeadInfoDto shopHeadInfoDto = mapperFacade.map(shopDetail, ShopHeadInfoDto.class);
        return ServerResponseEntity.success(shopHeadInfoDto);
    }

    @GetMapping("/getStatusInfo")
    @ApiOperation(value = "获取店铺状态信息", notes = "获取店铺状态信息")
    public ServerResponseEntity<ShopStatusInfoVO> getShopStatusInfo() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(shopId);
        if (Objects.isNull(shopDetail)) {
            throw new YamiShopBindException("店铺不存在");
        }
        ShopStatusInfoVO shopStatusInfoVO = new ShopStatusInfoVO();
        shopStatusInfoVO.setShopStatus(shopDetail.getShopStatus());
        shopStatusInfoVO.setContractStartTime(shopDetail.getContractStartTime());
        shopStatusInfoVO.setContractEndTime(shopDetail.getContractEndTime());
        if (Objects.equals(shopDetail.getShopStatus(), ShopStatus.OFFLINE.value())) {
            OfflineHandleEvent offlineHandleEventRes = offlineHandleEventService.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.SHOP.getValue(), shopId);
            shopStatusInfoVO.setOfflineStatus(offlineHandleEventRes.getStatus());
            shopStatusInfoVO.setOfflineReason(offlineHandleEventRes.getOfflineReason());
        }
        return ServerResponseEntity.success(shopStatusInfoVO);
    }

    @PutMapping
    @ApiOperation(value = "更新店铺基本信息", notes = "更新店铺基本信息")
    @PreAuthorize("@pms.hasPermission('shop:shopDetail:save')")
    public ServerResponseEntity<Void> update(@Valid @RequestBody ShopDetailParam shopDetailParam) {
        ShopDetail shopDetail = mapperFacade.map(shopDetailParam, ShopDetail.class);
        shopDetail.setShopId(SecurityUtils.getShopUser().getShopId());
        shopDetail.setUpdateTime(new Date());
        this.dealWithUpdateOrCreateInfo(shopDetail);
//        // 修改店铺接收手机号在专门的接口
//        shopDetail.setReceiveMobile(null);
        shopDetailService.updateShopDetail(shopDetail);
        eventPublisher.publishEvent(new EsProductUpdateEvent(shopDetail.getShopId(), null, EsOperationType.UPDATE_BY_SHOP_ID));
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateMobile")
    @ApiOperation(value = "修改店铺接收手机号", notes = "修改店铺接收手机号")
    public ServerResponseEntity<Void> updateMobile(@RequestBody ShopDetail shopDetail) {
        shopDetail.setShopId(SecurityUtils.getShopUser().getShopId());
        shopDetail.setUpdateTime(new Date());
        this.dealWithUpdateOrCreateInfo(shopDetail);
        shopDetailService.updateShopDetail(shopDetail);
        return ServerResponseEntity.success();
    }

    @GetMapping("/getOfflineHandleEvent")
    @ApiOperation(value = "获取店铺下线信息", notes = "获取店铺下线信息")
    public ServerResponseEntity<OfflineHandleEvent> getOfflineHandleEventByShopId() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        OfflineHandleEvent offlineHandleEvent = offlineHandleEventService.getProcessingEventByHandleTypeAndHandleId(OfflineHandleEventType.SHOP.getValue(), shopId);
        return ServerResponseEntity.success(offlineHandleEvent);
    }

    @PostMapping("/auditApply")
    @ApiOperation(value = "违规店铺重新提交审核", notes = "违规店铺重新提交审核")
    @PreAuthorize("@pms.hasPermission('shop:shopDetail:applyOnline')")
    public ServerResponseEntity<Void> auditApply(@RequestBody OfflineHandleEventAuditParam offlineHandleEventAuditParam) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        shopDetailService.auditApply(offlineHandleEventAuditParam.getEventId(), shopId, offlineHandleEventAuditParam.getReapplyReason());
        return ServerResponseEntity.success();
    }

    @PostMapping("/sendCode")
    @ApiOperation(value = "给店长发送验证码", notes = "给店长发送验证码")
    public ServerResponseEntity<String> sendLoginCode() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        // 查询店长信息
        ShopDetail shopDetail = shopDetailService.getById(shopId);
        if (Objects.isNull(shopDetail.getReceiveMobile())) {
            throw new YamiShopBindException("请先为商家账号设置接受通知手机号");
        }
//        if(Objects.equals(shopDetail.getShopId(), Constant.MAIN_SHOP)){
//            // 自营店无法提现
//            throw  new YamiShopBindException("yami.my.store.no.cash");
//        }
        smsLogService.sendSms(SendType.VALID, shopDetail.getShopId().toString(), shopDetail.getReceiveMobile(), Maps.newHashMap());
        return ServerResponseEntity.success(shopDetail.getReceiveMobile());
    }

    @GetMapping("/getAuditingInfo")
    @ApiOperation(value = "获取店铺审核状态信息", notes = "获取店铺审核状态信息")
    public ServerResponseEntity<ShopAuditing> getAuditingInfo() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        ShopAuditing shopAuditing = shopAuditingService.getOne(Wrappers.lambdaQuery(ShopAuditing.class).eq(ShopAuditing::getShopId, shopId));
        ShopDetail shopDetail = shopDetailService.getShopDetailByShopId(shopId);
        // 开店协议开关
        SysServiceConfig sysConfigObject = sysConfigService.getSysConfigObject(Constant.SERVICE_SWITCH_CONFIG, SysServiceConfig.class);
        if (Objects.isNull(shopAuditing)) {
            // 审核信息不存在，店铺未提交审核
            shopAuditing = new ShopAuditing();
            shopAuditing.setStatus(-2);
            shopAuditing.setShopProtocolSwitch(sysConfigObject.getShopProtocolSwitch());
            return ServerResponseEntity.success(shopAuditing);
        }
        if (Objects.equals(shopDetail.getShopStatus(), ShopStatus.OFFLINE.value())) {
            shopAuditing.setStatus(2);
        } else if (Objects.equals(shopDetail.getShopStatus(), ShopStatus.OFFLINE_AUDIT.value())) {
            shopAuditing.setStatus(0);
        }
        return ServerResponseEntity.success(shopAuditing);
    }

    /**
     * 处理店铺信息，这些信息商家无法直接修改
     * @param shopDetail
     */
    private void dealWithUpdateOrCreateInfo(ShopDetail shopDetail) {
        if (Objects.isNull(shopDetail)) {
            return;
        }
        shopDetail.setContractEndTime(null);
        shopDetail.setContractStartTime(null);
        shopDetail.setShopStatus(null);
        shopDetail.setType(null);
    }

}
