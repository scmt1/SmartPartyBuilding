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

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.OrderInvoiceState;
import com.yami.shop.bean.model.OrderInvoice;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.OrderInvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;


/**
 *
 *
 * @author Citrus
 * @date 2021-08-16 14:22:47
 */
@RestController("multishopOrderInvoiceController")
@AllArgsConstructor
@RequestMapping("/m/orderInvoice")
@Api(tags = "商家端订单发票接口")
public class OrderInvoiceController {

    private final OrderInvoiceService orderInvoiceService;

    @ApiOperation(value = "分页查询订单发票", notes = "分页查询订单发票")
    @GetMapping("/page")
    public ServerResponseEntity<IPage<OrderInvoice>> getOrderInvoicePage(PageParam<OrderInvoice> page, OrderInvoice orderInvoice) {
        orderInvoice.setShopId(SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success(orderInvoiceService.page(page,orderInvoice));
    }

    @ApiOperation(value = "通过id查询发票信息", notes = "通过id查询发票信息")
    @GetMapping("/info/{orderInvoiceId}")
    @PreAuthorize("@pms.hasPermission('order:orderInvoice:update')")
    public ServerResponseEntity<OrderInvoice> getById(@PathVariable("orderInvoiceId") Long orderInvoiceId) {
        OrderInvoice orderInvoice = orderInvoiceService.getById(orderInvoiceId);
        if (!Objects.equals(SecurityUtils.getShopUser().getShopId(), orderInvoice.getShopId())) {
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(orderInvoice);
    }

    @ApiOperation(value = "开票，上传文件", notes = "开票，上传文件")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('order:orderInvoice:commit')")
    public ServerResponseEntity<Boolean> updateById(@RequestBody @Valid OrderInvoice orderInvoice) {
        if (Objects.isNull(orderInvoice.getFileId())){
            //商家提交，不上传文件不给保存
            throw new YamiShopBindException("yami.file.isNot.exist");
        }
        OrderInvoice orderInvoiceById = orderInvoiceService.getById(orderInvoice.getOrderInvoiceId());
        if (!Objects.equals(orderInvoiceById.getInvoiceState(), OrderInvoiceState.FAIL.value())) {
            orderInvoice.setInvoiceState(OrderInvoiceState.ISSUED.value());
        }
        orderInvoice.setUploadTime(new Date());
        return ServerResponseEntity.success(orderInvoiceService.updateById(orderInvoice));
    }

    @GetMapping("/is_change")
    @ApiOperation(value = "申请时间是否发生改变", notes = "如果改变了，用户可能已经更改过申请内容，提醒商家刷新页面")
    public ServerResponseEntity<Boolean> applicationTimeChange(@RequestParam("orderInvoiceId") Long orderInvoiceId,
                                                         @RequestParam("applicationTime") String applicationTime) {
        OrderInvoice dbInvoice = orderInvoiceService.getById(orderInvoiceId);
        return ServerResponseEntity.success(!DateUtil.isSameTime(Convert.toDate(applicationTime),dbInvoice.getApplicationTime()));
    }

    @ApiOperation(value = "检查该订单是否已经上传发票", notes = "检查该订单是否已经上传发票")
    @GetMapping("/is_upload")
    public ServerResponseEntity<Boolean> isUpload(@RequestParam String orderNumber) {
        return ServerResponseEntity.success(orderInvoiceService.isUpload(orderNumber));
    }

}
