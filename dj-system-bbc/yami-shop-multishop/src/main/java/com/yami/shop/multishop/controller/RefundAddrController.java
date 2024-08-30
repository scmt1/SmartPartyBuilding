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


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.RefundAddr;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.RefundAddrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author person
 */
@RestController
@AllArgsConstructor
@RequestMapping("/shop/refundAddr")
@Api(tags = "退货地址接口")
public class RefundAddrController {

    private final RefundAddrService refundAddrService;

    /**
     * 分页查询
     *
     * @param page       分页对象
     * @param refundAddr
     * @return 分页数据
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('shop:refundAddr:page')")
    @ApiOperation(value = "分页查询退货地址", notes = "分页查询退货地址")
    public ServerResponseEntity<IPage<RefundAddr>> getRefundAddrPage(PageParam<RefundAddr> page, RefundAddr refundAddr) {
        return ServerResponseEntity.success(refundAddrService.page(page, new LambdaQueryWrapper<RefundAddr>()
                .eq(RefundAddr::getShopId, SecurityUtils.getShopUser().getShopId())
                .ne(RefundAddr::getStatus, -1).orderByDesc(RefundAddr::getRefundAddrId)));
    }


    /**
     * 通过id查询
     *
     * @param refundAddrId id
     * @return 单个数据
     */
    @GetMapping("/info/{refundAddrId}")
    @ApiOperation(value = "通过id查询退货地址", notes = "通过id查询退货地址")
    public ServerResponseEntity<RefundAddr> getById(@PathVariable("refundAddrId") Long refundAddrId) {
        RefundAddr refundAddr = refundAddrService.getById(refundAddrId);
        if (!Objects.equals(SecurityUtils.getShopUser().getShopId(), refundAddr.getShopId())) {
            throw new YamiShopBindException("yami.no.auth");
        }
        return ServerResponseEntity.success(refundAddr);
    }

    /**
     * 新增
     *
     * @param refundAddr
     * @return 是否新增成功
     */
    @SysLog("新增")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('shop:refundAddr:save')")
    @ApiOperation(value = "新增退货地址", notes = "新增退货地址")
    public ServerResponseEntity<String> save(@RequestBody @Valid RefundAddr refundAddr) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        Date now = new Date();
        refundAddr.setShopId(shopId);
        refundAddr.setCreateTime(now);
        refundAddr.setUpdateTime(now);
        refundAddrService.addRefundAddr(refundAddr);
        // 添加成功
        return ServerResponseEntity.success(ResponseEnum.SHOW_SUCCESS.value(), I18nMessage.getMessage("yami.activity.add.success"));
    }

    /**
     * 修改
     *
     * @param refundAddr
     * @return 是否修改成功
     */
    @SysLog("修改")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('shop:refundAddr:update')")
    @ApiOperation(value = "修改退货地址", notes = "修改退货地址")
    public ServerResponseEntity<String> updateById(@RequestBody @Valid RefundAddr refundAddr) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        refundAddr.setUpdateTime(new Date());
        refundAddrService.updateRefundAddr(shopId, refundAddr);
        // 修改成功
        return ServerResponseEntity.success(ResponseEnum.SHOW_SUCCESS.value(), I18nMessage.getMessage("yami.activity.update.success"));
    }

    /**
     * 通过id删除
     *
     * @param refundAddrId id
     * @return 是否删除成功
     */
    @SysLog("删除")
    @DeleteMapping("/{refundAddrId}")
    @PreAuthorize("@pms.hasPermission('shop:refundAddr:delete')")
    @ApiOperation(value = "删除退货地址", notes = "删除退货地址")
    public ServerResponseEntity<Boolean> removeById(@PathVariable Long refundAddrId) {
        return ServerResponseEntity.success(refundAddrService.removeById(refundAddrId));
    }

    /**
     * 获取店铺的所有收获地址
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取店铺的所有退货地址", notes = "获取店铺的所有退货地址")
    public ServerResponseEntity<List<RefundAddr>> list() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        List<RefundAddr> list = refundAddrService.list(new LambdaQueryWrapper<RefundAddr>()
                .eq(RefundAddr::getShopId, shopId)
                .eq(RefundAddr::getStatus, 1)
                .orderByDesc(RefundAddr::getUpdateTime));
        return ServerResponseEntity.success(list);
    }

}
