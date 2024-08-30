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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.model.Delivery;
import com.yami.shop.common.enums.StatusEnum;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.DeliveryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;


/**
 * 物流公司
 *
 * @author LGH
 * @date 2019-10-29 11:42:24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/platform/delivery")
@Api(tags = "物流公司")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('platform:delivery:page')")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<Delivery>> getDeliveryPage(PageParam<Delivery> page, Delivery delivery) {
        return ServerResponseEntity.success(deliveryService.page(page, new LambdaQueryWrapper<Delivery>()
                .like(StrUtil.isNotBlank(delivery.getDvyName()), Delivery::getDvyName, delivery.getDvyName())
                .like(StrUtil.isNotBlank(delivery.getDvyNo()), Delivery::getDvyNo, delivery.getDvyNo())
                .like(StrUtil.isNotBlank(delivery.getDvyNoHd()), Delivery::getDvyNoHd, delivery.getDvyNoHd())
                .gt(Delivery::getStatus, StatusEnum.DELETE.value())
        ));
    }


    @GetMapping("/info/{dvyId}")
    @ApiOperation(value = "查询物流公司信息", notes = "查询物流公司信息")
    @ApiImplicitParam(name = "dvyId", value = "物流id")
    public ServerResponseEntity<Delivery> getById(@PathVariable("dvyId") Long dvyId) {
        return ServerResponseEntity.success(deliveryService.getById(dvyId));
    }

    @PostMapping
    @PreAuthorize("@pms.hasPermission('platform:delivery:save')")
    @ApiOperation(value = "新增物流公司", notes = "新增物流公司")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid Delivery delivery) {
        delivery.setRecTime(new Date());
        delivery.setModifyTime(new Date());
        List<Delivery> deliveryList = deliveryService.list(new LambdaQueryWrapper<Delivery>().eq(Delivery::getDvyName, delivery.getDvyName()));
        if (CollUtil.isNotEmpty(deliveryList)) {
            // 物流名称不能重复
            throw new YamiShopBindException("yami.platform.delivery.name.repeat");
        }
        return ServerResponseEntity.success(deliveryService.save(delivery));
    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('platform:delivery:update')")
    @ApiOperation(value = "修改物流公司信息", notes = "修改物流公司信息")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid Delivery delivery) {
        delivery.setModifyTime(new Date());
        delivery.setQueryUrl(null);
        List<Delivery> deliveryList = deliveryService.list(new LambdaQueryWrapper<Delivery>().eq(Delivery::getDvyName, delivery.getDvyName())
                .ne(Delivery::getDvyId, delivery.getDvyId()));
        if (CollUtil.isNotEmpty(deliveryList)) {
            // 物流名称不能重复
            throw new YamiShopBindException("yami.platform.delivery.name.repeat");
        }
        boolean updateRes = deliveryService.update(delivery, Wrappers.lambdaUpdate(Delivery.class)
                .eq(Delivery::getDvyId, delivery.getDvyId())
                .gt(Delivery::getStatus, StatusEnum.DELETE.value())
        );
        if (!updateRes) {
            throw new YamiShopBindException("修改失败，当前物流公司信息可能已经被删除");
        }
        return ServerResponseEntity.success(Boolean.TRUE);
    }

    @DeleteMapping("/{dvyId}")
    @PreAuthorize("@pms.hasPermission('platform:delivery:delete')")
    @ApiOperation(value = "删除物流公司", notes = "删除物流公司")
    @ApiImplicitParam(name = "dvyId", value = "物流id")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long dvyId) {
        // 逻辑删除
        deliveryService.update(Wrappers.lambdaUpdate(Delivery.class)
                .set(Delivery::getStatus, StatusEnum.DELETE.value())
                .eq(Delivery::getDvyId, dvyId)
        );
        return ServerResponseEntity.success(Boolean.TRUE);
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取物流公司列表", notes = "获取物流公司列表")
    public ServerResponseEntity<List<Delivery>> page() {
        List<Delivery> list = deliveryService.list(Wrappers.lambdaQuery(Delivery.class)
                .eq(Delivery::getStatus, StatusEnum.ENABLE.value())
        );
        return ServerResponseEntity.success(list);
    }
}
