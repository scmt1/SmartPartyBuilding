/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.platform.controller;

import com.yami.shop.bean.model.Delivery;
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
@Api(tags = "积分物流信息")
@RestController
@RequestMapping("/score/delivery")
public class ScoreDeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/list")
    @ApiOperation(value = "获取物流公司列表", notes = "获取物流公司列表")
    public ServerResponseEntity<List<Delivery>> list(){
        List<Delivery> list = deliveryService.list();
        return ServerResponseEntity.success(list);
    }

}
