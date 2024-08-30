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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.RemindType;
import com.yami.shop.bean.model.NotifyLog;
import com.yami.shop.bean.param.OrderParam;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.NotifyLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 *
 *
 * @author lhd
 * @date 2020-07-05 16:19:10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/multishop/notifyLog" )
@Api(tags = "消息日志接口")
public class NotifyLogController {

    private final NotifyLogService notifyLogService;
    private final MapperFacade mapperFacade;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询消息日志", notes = "分页查询消息日志")
    public ServerResponseEntity<IPage<NotifyLog>> getSmsRechargeLogPage(PageParam<NotifyLog> page, OrderParam orderParam) {
        return ServerResponseEntity.success(notifyLogService.page(page, new LambdaQueryWrapper<NotifyLog>()
                .eq(NotifyLog::getShopId, SecurityUtils.getShopUser().getShopId())
                .eq(NotifyLog::getRemindType, RemindType.SMS.value())
                .gt(Objects.nonNull(orderParam.getStartTime()),NotifyLog::getCreateTime,orderParam.getStartTime())
                .lt(Objects.nonNull(orderParam.getEndTime()),NotifyLog::getCreateTime,orderParam.getEndTime())
                .orderByDesc(NotifyLog::getCreateTime)));
    }

    @GetMapping("/unReadCount" )
    @ApiOperation(value = "查询未读消息数量", notes = "查询未读消息数量")
    public ServerResponseEntity<Integer> getNotifyCount() {
        return ServerResponseEntity.success(notifyLogService.count(new LambdaQueryWrapper<NotifyLog>()
                .eq(NotifyLog::getRemindId, SecurityUtils.getShopUser().getShopId())
                .eq(NotifyLog::getRemindType, 1)
                .ge(NotifyLog::getSendType, 100)
                .eq(NotifyLog::getStatus,0)));
    }

    @GetMapping("/unReadCountList" )
    @ApiOperation(value = "查询未读消息列表", notes = "查询未读消息列表")
    public ServerResponseEntity<IPage<NotifyLog>> getUnReadCountList(PageParam<NotifyLog> page,NotifyLog notifyLog) {
        notifyLogService.pageByNotifyLogParam(page, notifyLog);
        IPage<NotifyLog> notifyLogs = notifyLogService.page(page,new LambdaQueryWrapper<NotifyLog>()
                .eq(NotifyLog::getRemindId, SecurityUtils.getShopUser().getShopId())
                .eq(NotifyLog::getRemindType, 1)
                .ge(NotifyLog::getSendType, 100)
                .orderByAsc(NotifyLog::getStatus)
                .orderByDesc(NotifyLog::getCreateTime));

        List<NotifyLog> recordsDb = notifyLogs.getRecords();
        List<NotifyLog> records = new ArrayList<>();
        for (NotifyLog record : recordsDb) {
            NotifyLog remind = mapperFacade.map(record,NotifyLog.class);
            record.setStatus(1);
            records.add(remind);
        }
        notifyLogs.setRecords(records);
        if(CollectionUtils.isNotEmpty(recordsDb)) {
            notifyLogService.updateBatchById(recordsDb);
        }
        return ServerResponseEntity.success(notifyLogs);
    }

    @GetMapping("pageByNotifyLogParam")
    @ApiOperation(value = "根据条件分页查询消息日志", notes = "根据条件分页查询消息日志")
    public ServerResponseEntity<IPage<NotifyLog>> pageByNotifyLogParam(PageParam<NotifyLog> page, NotifyLog notifyLog) {
        notifyLog.setShopId(SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success(notifyLogService.pageByNotifyLogParam(page, notifyLog));
    }

    @PutMapping("/is_read")
    @ApiOperation(value = "批量设置消息为已读状态 ", notes = "批量设置消息为已读状态")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "logIds", value = "消息id", required = true),
            @ApiImplicitParam(name = "type", value = "数据类型 1全部 0已选", defaultValue = "0")
    })
    public ServerResponseEntity<Void> isRead(@RequestParam(value="logIds", required = false) List<Long> logIds, Integer type) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        if(Objects.equals(type, 1)){
            notifyLogService.isReadByShopId(shopId);
        }
        else if(Objects.equals(type, 0) && CollUtil.isNotEmpty(logIds)){
            notifyLogService.isReadByIds(logIds);
        }
        return ServerResponseEntity.success();
    }
}
