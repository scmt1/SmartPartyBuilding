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

import com.yami.shop.bean.vo.ShopWalletVO;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ShopWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 商家钱包信息
 *
 * @author Dwl
 * @date 2019-09-19 14:02:57
 */
@RestController
@AllArgsConstructor
@RequestMapping("/shop/shopWallet" )
@Api(tags = "商家钱包接口")
public class ShopWalletController {

    private final ShopWalletService shopWalletService;

    /**
     * 通过id查询商家钱包信息
     * @return 单个数据
     */
    @GetMapping("/info" )
    @ApiOperation(value = "通过id查询商家钱包信息", notes = "通过id查询商家钱包信息")
    public ServerResponseEntity<ShopWalletVO> info() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        ShopWalletVO shopWallet = shopWalletService.getShopWalletAndTransaction(shopId);
        return ServerResponseEntity.success(shopWallet);
    }

}
