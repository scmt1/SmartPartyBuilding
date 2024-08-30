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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.ShopWalletLog;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ShopWalletLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 商家钱包记录
 *
 * @author Dwl
 * @date 2019-09-19 14:02:57
 */
@RestController
@AllArgsConstructor
@RequestMapping("/shop/shopWalletLog")
@Api(tags = "商家钱包记录接口")
public class ShopWalletLogController {

    private final ShopWalletLogService shopWalletLogService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询商家收入明细", notes = "分页查询商家收入明细")
    public ServerResponseEntity<IPage<ShopWalletLog>> getShopWalletLogPage(PageParam<ShopWalletLog> page, ShopWalletLog shopWalletLog){
        shopWalletLog.setShopId(SecurityUtils.getShopUser().getShopId());
        IPage<ShopWalletLog> shopWalletLogPage = shopWalletLogService.pageByParam(page, shopWalletLog);
        return ServerResponseEntity.success(shopWalletLogPage);
    }
}
