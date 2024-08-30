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
import com.yami.shop.bean.model.ShopWallet;
import com.yami.shop.bean.model.ShopWalletLog;
import com.yami.shop.bean.param.CustomerReqParam;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.ShopWalletLogService;
import com.yami.shop.service.ShopWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


/**
 * 商家钱包信息
 *
 * @author LGH
 * @date 2019-09-29 11:10:12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/shop/shopWallet" )
@Api(tags = "商家钱包")
public class ShopWalletController {

    private final ShopWalletService shopWalletService;
    private final ShopWalletLogService shopWalletLogService;

    @GetMapping("/page" )
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<ShopWallet>> getShopWalletPage(PageParam<ShopWallet> page, ShopWallet shopWallet) {
        return ServerResponseEntity.success(shopWalletService.pageShopWallet(page, shopWallet));
    }

    @GetMapping("/getShopWallet")
    @ApiOperation(value = "查看店铺钱包信息",notes = "根据店铺id查看店铺钱包信息")
    public ServerResponseEntity<ShopWallet> getShopWalletVoByShopId(){
        ShopWallet shopWallet = shopWalletService.getShopWalletByShopId(Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success(shopWallet);
    }

    @GetMapping("/pagePlatformInfo")
    @ApiOperation(value = "分页查询平台收入明细", notes = "分页查询平台收入明细")
    public ServerResponseEntity<IPage<ShopWalletLog>> getShopWalletLogPage(PageParam<ShopWalletLog> page, ShopWalletLog shopWalletLog){
        shopWalletLog.setShopId(Constant.PLATFORM_SHOP_ID);
        IPage<ShopWalletLog> shopWalletLogPage = shopWalletLogService.pageByParam(page, shopWalletLog);
        return ServerResponseEntity.success(shopWalletLogPage);
    }

    @GetMapping("/getAllShopWallet")
    @ApiOperation(value = "查看店铺钱包总信息")
    public ServerResponseEntity<ShopWallet> getAllShopWalletVoByShopId(CustomerReqParam customerReqParam) {
        ShopWallet shopWallet = shopWalletService.getAllShop(customerReqParam);
        return ServerResponseEntity.success(shopWallet);
    }

    @GetMapping("/pageShopWalletByTime")
    @ApiOperation(value = "分页查看店铺钱包总信息")
    public ServerResponseEntity<IPage<ShopWallet>> pageShopWalletByTime(PageParam<ShopWallet> page, CustomerReqParam customerReqParam) {
        IPage<ShopWallet> shopWalletPage = shopWalletService.pageShopWalletByTime(page, customerReqParam);
        return ServerResponseEntity.success(shopWalletPage);
    }

    @GetMapping("/pageAllShop")
    @ApiOperation(value = "查看所有店铺的日志")
    public ServerResponseEntity<IPage<ShopWalletLog>> getAllShopWalletLogPage(PageParam<ShopWallet> page, ShopWalletLog shopWalletLog) {
        IPage<ShopWalletLog> shopWalletLogPage = shopWalletLogService.pageAllShop(page, shopWalletLog);
        return ServerResponseEntity.success(shopWalletLogPage);
    }

    @GetMapping("/getShopWalletLogForm")
    @ApiOperation(value = "导出报表", notes = "导出店铺结算报表")
    @PreAuthorize("@pms.hasPermission('shop:wallet:excel')")
    public void getShopWalletLogForm(CustomerReqParam customerReqParam, HttpServletResponse response) {
        shopWalletLogService.excelShopWalletLog(customerReqParam, response);
    }
}
