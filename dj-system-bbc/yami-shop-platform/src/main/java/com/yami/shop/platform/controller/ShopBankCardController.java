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
import com.yami.shop.bean.model.ShopBankCard;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.ShopBankCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/8/11 15:04
 */
@RestController
@AllArgsConstructor
@RequestMapping("/platform/shopBankCard" )
@Api(tags="银行卡相关接口")
public class ShopBankCardController {

    private final ShopBankCardService shopBankCardService;

    @GetMapping("/listByShopId")
    @ApiOperation(value = "根据店铺id批量获取银行卡信息")
    @ApiImplicitParam(name = "shopId", value = "店铺id")
    public ServerResponseEntity<List<ShopBankCard>> getShopBankCardList(@RequestParam(value = "shopId") Long shopId) {
        List<ShopBankCard> list = shopBankCardService.list(new LambdaQueryWrapper<ShopBankCard>().eq(ShopBankCard::getShopId, shopId).eq(ShopBankCard::getStatus,1));
        return ServerResponseEntity.success(list);
    }
}
