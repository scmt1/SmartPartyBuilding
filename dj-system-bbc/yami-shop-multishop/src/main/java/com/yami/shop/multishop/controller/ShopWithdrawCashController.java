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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.enums.PositionType;
import com.yami.shop.bean.enums.SendType;
import com.yami.shop.bean.enums.ShopWithdrawCashStatus;
import com.yami.shop.bean.model.ShopDetail;
import com.yami.shop.bean.model.ShopWithdrawCash;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.ShopDetailService;
import com.yami.shop.service.ShopWithdrawCashService;
import com.yami.shop.service.SmsLogService;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * 商家提现申请信息
 *
 * @author Dwl
 * @date 2019-09-19 14:22:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/shop/withdrawCash" )
@Api(tags = "商家提现申请信息接口")
public class ShopWithdrawCashController {

    private final ShopWithdrawCashService shopWithdrawCashService;
    private final ShopEmployeeService shopEmployeeService;
    private ShopDetailService shopDetailService;
    private SmsLogService smsLogService;


    @PostMapping("/apply")
    @PreAuthorize("@pms.hasPermission('shop:withdrawCash:apply')")
    @ApiOperation(value = "商家提交申请提现", notes = "商家提交申请提现")
    public ServerResponseEntity<Void> apply(@RequestBody ShopWithdrawCash shopWithdrawCash) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        ShopDetail shopDetail = shopDetailService.getById(shopId);
        // 查询店长信息
        ShopEmployee shopEmployee = shopEmployeeService.getOne(Wrappers.lambdaQuery(ShopEmployee.class).eq(ShopEmployee::getShopId, shopId).eq(ShopEmployee::getType, PositionType.ADMIN.value()));

//        if(Objects.equals(shopDetail.getShopId(), Constant.MAIN_SHOP)){
//            // 自营店无法提现
//            throw  new YamiShopBindException("yami.my.store.no.cash");
//        }
        if(shopWithdrawCash.getAmount() < 1){
            // 提现金额不能少于1.00元
            throw new YamiShopBindException("yami.shop.cash.too.less");
        }
        if (!smsLogService.checkValidCode(shopDetail.getReceiveMobile(), shopWithdrawCash.getCode(), SendType.VALID)){
            // 验证码有误或已过期
            throw new YamiShopBindException("yami.user.code.error");
        }

        shopWithdrawCash.setUpdateTime(new Date());
        shopWithdrawCash.setCreateTime(new Date());
        shopWithdrawCash.setStatus(ShopWithdrawCashStatus.WAITAUDIT.value());
        shopWithdrawCash.setShopId(SecurityUtils.getShopUser().getShopId());

        shopWithdrawCashService.withdrawCash(shopWithdrawCash);
        return ServerResponseEntity.success();
    }

    @GetMapping("/getShopInfo")
    @ApiOperation(value = "获取店铺钱包信息", notes = "获取店铺钱包信息")
    public ServerResponseEntity<ShopWithdrawCash> getShopInfo() {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        ShopWithdrawCash shopWithdrawCash = shopWithdrawCashService.getShopWithdrawCashByShopId(shopId);
        return ServerResponseEntity.success(shopWithdrawCash);
    }

    /**
     * 分页查询
     *
     * @param page             分页对象
     * @param shopWithdrawCash 商家提现申请信息
     * @return 分页数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询商家提现信息", notes = "分页查询商家提现信息")
    public ServerResponseEntity<IPage<ShopWithdrawCash>> getShopWithdrawCashPage(PageParam<ShopWithdrawCash> page, ShopWithdrawCash shopWithdrawCash) {
        shopWithdrawCash.setShopId(SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success(shopWithdrawCashService.pageShopWithdrawCash(page, shopWithdrawCash));
    }
}
