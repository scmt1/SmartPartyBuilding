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
import com.yami.shop.bean.model.NotifyLog;
import com.yami.shop.bean.param.OrderParam;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.NotifyLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 *
 *
 * @author lhd
 * @date 2020-08-10 15:20:53
 */
@RestController
@AllArgsConstructor
@RequestMapping("/platform/notifyLog" )
@Api(tags = "消息记录")
public class NotifyLogController {

    private final NotifyLogService notifyLogService;

    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('platform:notifyLog:list')" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<NotifyLog>> getNotifyLogPage(PageParam<NotifyLog> page, OrderParam orderParam) {
        return ServerResponseEntity.success(notifyLogService.pageByParam(page,orderParam));
    }


}
