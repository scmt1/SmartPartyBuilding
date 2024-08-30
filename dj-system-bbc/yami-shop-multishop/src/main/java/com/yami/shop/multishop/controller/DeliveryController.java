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
import com.yami.shop.bean.model.Delivery;
import com.yami.shop.common.enums.StatusEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.DeliveryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author lgh on 2018/11/26.
 */
@RestController
@RequestMapping("/admin/delivery")
@Api(tags = "物流公司接口")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/list")
    @ApiOperation(value = "获取物流公司列表", notes = "获取物流公司列表")
    public ServerResponseEntity<List<Delivery>> page(){
        List<Delivery> list = deliveryService.list(Wrappers.lambdaQuery(Delivery.class)
                .eq(Delivery::getStatus, StatusEnum.ENABLE.value())
        );
        return ServerResponseEntity.success(list);
    }

}
