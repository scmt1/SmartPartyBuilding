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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.model.UserAddr;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.UserAddrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author lgh on 2018/10/16.
 */
@RestController
@RequestMapping("/platform/userAddr")
@Api(tags = "用户地址")
public class UserAddrController {

    @Autowired
    private UserAddrService userAddrService;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('plateform:user:page')")
    @ApiOperation(value = "分页获取", notes = "分页获取")
    public ServerResponseEntity<List<UserAddr>> page(UserAddr userAddr,PageParam<UserAddr> page) {
        List<UserAddr> userAddrPage = userAddrService.list(new LambdaQueryWrapper<UserAddr>()
                .eq(UserAddr::getUserId, userAddr.getUserId()));
        return ServerResponseEntity.success(userAddrPage);
    }

}
