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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.model.ShopBankCard;
import com.yami.shop.bean.model.ShopWithdrawCash;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ShopBankCardService;
import com.yami.shop.service.ShopWithdrawCashService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


/**
 * 商家提现申请信息
 *
 * @author YXF
 *  * @date 2020-04-07 14:22:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/shop/shopBankCard" )
@Api(tags="银行卡接口")
public class ShopBankCardController {

    private final ShopBankCardService shopBankCardService;
    private final ShopWithdrawCashService shopWithdrawCashService;

    @GetMapping("/getShopBankCardList")
    @ApiOperation(value = "获取店铺下的银行卡列表", notes = "获取店铺下的银行卡列表")
    public ServerResponseEntity<List<ShopBankCard>> getShopBankCardList() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        List<ShopBankCard> list = shopBankCardService.list(new LambdaQueryWrapper<ShopBankCard>().eq(ShopBankCard::getShopId, shopId).eq(ShopBankCard::getStatus,1));
        return ServerResponseEntity.success(list);
    }

    @GetMapping
    @ApiOperation(value = "根据银行卡id获取银行卡信息", notes = "根据银行卡id获取银行卡信息")
    public ServerResponseEntity<ShopBankCard> getById(@RequestParam("shopBankCardId") Long shopBankCardId) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        ShopBankCard shopBankCard = shopBankCardService.getOne(Wrappers.lambdaQuery(ShopBankCard.class)
                .eq(ShopBankCard::getShopBankCardId, shopBankCardId)
                .eq(ShopBankCard::getShopId, shopId)
        );
        return ServerResponseEntity.success(shopBankCard);
    }

    @PutMapping
    @ApiOperation(value = "更新银行卡信息", notes = "更新银行卡信息")
    @PreAuthorize("@pms.hasPermission('shop:shopBankCard:update')")
    public ServerResponseEntity<Void> update(@RequestBody @Valid ShopBankCard shopBankCard) {
        shopBankCardService.updateByShopId(shopBankCard, SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success();
    }

    @PostMapping
    @ApiOperation(value = "添加单个银行卡信息", notes = "添加单个银行卡信息")
    @PreAuthorize("@pms.hasPermission('shop:shopBankCard:save')")
    public ServerResponseEntity<Long> save(@RequestBody @Valid ShopBankCard shopBankCard) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        shopBankCard.setShopId(shopId);
        int count = shopBankCardService.count(new LambdaQueryWrapper<ShopBankCard>().eq(ShopBankCard::getShopId, shopId).eq(ShopBankCard::getStatus, 1));
        if (count >= Constant.MAX_SHOP_BANK_CARD) {
            throw new YamiShopBindException("yami.shop.max.card.num");
        }
        return ServerResponseEntity.success(shopBankCardService.insertSelective(shopBankCard));
    }

    @PutMapping("/setDefault")
    @ApiOperation(value = "设置为默认银行卡", notes = "设置为默认银行卡")
    @PreAuthorize("@pms.hasPermission('shop:shopBankCard:default')")
    public ServerResponseEntity<Void> setDefault(@RequestBody @Valid ShopBankCard shopBankCard) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        shopBankCardService.setDefault(shopBankCard.getShopBankCardId(),shopId);
        return ServerResponseEntity.success();
    }

    @PutMapping("/setNotDefault")
    @ApiOperation(value = "取消默认银行卡", notes = "取消默认银行卡")
    @PreAuthorize("@pms.hasPermission('shop:shopBankCard:noDefault')")
    public ServerResponseEntity<Void> setNotDefault(@RequestParam(value = "shopBankCardId") Long shopBankCardId) {
        shopBankCardService.setNotDefault(shopBankCardId, SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success();
    }

    @DeleteMapping("/{shopBankCardId}")
    @ApiOperation(value = "根据银行卡id删除银行卡信息", notes = "根据银行卡id删除银行卡信息")
    @PreAuthorize("@pms.hasPermission('shop:shopBankCard:delete')")
    public ServerResponseEntity<Void> removeById(@PathVariable("shopBankCardId") Long shopBankCardId) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        ShopBankCard shopBankCard = shopBankCardService.getById(shopBankCardId);
        if (Objects.equals(shopBankCard.getIsDefault(),1)){
            // 不能删除默认银行卡
            throw new YamiShopBindException("yami.shop.cannot.delete.card");
        }
        // 检查是否是最后一张银行卡
        if (shopBankCardService.count(Wrappers.lambdaQuery(ShopBankCard.class).eq(ShopBankCard::getShopId, shopId).eq(ShopBankCard::getStatus, 1)) == 1) {
            throw new YamiShopBindException("yami.shop.least.one.card");
        }
        int count = shopWithdrawCashService.count(new LambdaQueryWrapper<ShopWithdrawCash>()
                .eq(ShopWithdrawCash::getShopBankCardId, shopBankCardId).eq(ShopWithdrawCash::getStatus, 0));
        if(count > 0){
            // 正在用于申请提现的银行卡不能删除
            throw new YamiShopBindException("yami.shop.cannot.delete.check");
        }
        // 进行逻辑删除
        shopBankCardService.update(new LambdaUpdateWrapper<ShopBankCard>().set(ShopBankCard::getStatus,-1).eq(ShopBankCard::getShopBankCardId,shopBankCardId).eq(ShopBankCard::getShopId, shopId));
        return ServerResponseEntity.success();
    }

    @PostMapping("/saveAndApplyShop")
    @ApiOperation(value = "批量保存店铺银行卡信息并提交店铺审核信息", notes = "批量保存店铺银行卡信息并提交店铺审核信息")
    public ServerResponseEntity<Void> saveAndApplyShop(@Valid @RequestBody List<ShopBankCard> shopBankCards) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        Long employeeId = SecurityUtils.getShopUser().getEmployeeId();
        shopBankCardService.insertBatchAndSubmitApply(shopBankCards, shopId, employeeId);
        return ServerResponseEntity.success();
    }
}
