/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.task;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.yami.shop.service.ShopDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author lth
 * @Date 2021/8/9 14:45
 */
@Slf4j
@Component
@AllArgsConstructor
public class ShopTask {

    private final ShopDetailService shopDetailService;

    /**
     * 根据签约时间改变店铺状态
     */
    @XxlJob("changeShopStatusByContractTime")
    public void changeShopStatusByContractTime() {
        log.info("根据签约时间改变店铺状态");
        Date now = new Date();
        shopDetailService.changeShopStatusByContractTime(now);
    }
}
