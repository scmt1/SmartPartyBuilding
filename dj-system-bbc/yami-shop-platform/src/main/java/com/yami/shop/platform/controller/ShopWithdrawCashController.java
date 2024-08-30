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
import com.yami.shop.bean.model.ShopDetail;
import com.yami.shop.bean.model.ShopWithdrawCash;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.platform.util.SecurityUtils;
import com.yami.shop.service.ShopDetailService;
import com.yami.shop.service.ShopWithdrawCashService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * 商家提现申请信息
 *
 * @author LGH
 * @date 2019-09-29 11:10:12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/shop/shopWithdrawCash")
@Api(tags = "商家提现申请信息")
public class ShopWithdrawCashController {

    private final ShopWithdrawCashService shopWithdrawCashService;

    private final ShopDetailService shopDetailService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<ShopWithdrawCash>> getShopWithdrawCashPage(PageParam<ShopWithdrawCash> page, ShopWithdrawCash shopWithdrawCash) {
        return ServerResponseEntity.success(shopWithdrawCashService.pageShopWithdrawCash(page, shopWithdrawCash));
    }

    @GetMapping("/exportShopWithdrawCash")
    @ApiOperation(value = "导出商家提现信息", notes = "导出商家提现信息")
    public void exportShopWithdrawCash(ShopWithdrawCash shopWithdrawCash, HttpServletResponse response) {
        shopWithdrawCashService.exportShopWithdrawCash(shopWithdrawCash, response);
    }

    @GetMapping("/info/{cashId}")
    @ApiOperation(value = "查询商家提现申请信息", notes = "查询商家提现申请信息")
    @ApiImplicitParam(name = "cashId", value = "商家提现申请id")
    public ServerResponseEntity<ShopWithdrawCash> getById(@PathVariable("cashId") Long cashId) {
        return ServerResponseEntity.success(shopWithdrawCashService.getById(cashId));
    }

    @SysLog("审核商家提现信息")
    @PutMapping("/audit")
    @PreAuthorize("@pms.hasPermission('shop:shopWithdrawCash:audit')")
    @ApiOperation(value = "审核信息", notes = "审核信息")
    public ServerResponseEntity<Void> audit(@RequestBody ShopWithdrawCash shopWithdrawCash) {
        ShopWithdrawCash dbShopWithdrawCash = shopWithdrawCashService.getById(shopWithdrawCash.getCashId());
        if (dbShopWithdrawCash == null) {
            // 未找到申请信息
            throw new YamiShopBindException("yami.store.apply.no.exist");
        }
        ShopDetail shopdetail = shopDetailService.getShopDetailByShopId(dbShopWithdrawCash.getShopId());
        if (shopdetail == null) {
            // 未找到该店铺信息
            throw new YamiShopBindException("yami.store.not.exist");
        }
        shopWithdrawCash.setCreateTime(null);

        shopWithdrawCashService.auditWithdrawCash(shopWithdrawCash.getCashId(), shopWithdrawCash, SecurityUtils.getSysUser().getUserId(), null);
        // 减少冻结金额
        return ServerResponseEntity.success();
    }
}
