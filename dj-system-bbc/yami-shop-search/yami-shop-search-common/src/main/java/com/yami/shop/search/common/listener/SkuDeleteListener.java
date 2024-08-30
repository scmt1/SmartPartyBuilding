/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.search.common.listener;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.event.SkuDeleteEvent;
import com.yami.shop.bean.model.StockBillLogItem;
import com.yami.shop.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author Yami
 */
@Slf4j
@Component("skuDeleteListener")
@AllArgsConstructor
public class SkuDeleteListener {

    private final PurchaseProdService purchaseProdService;
    private final SupplierProdService supplierProdService;
    private final TakeStockProdService takeStockProdService;
    private final StockBillLogItemService stockBillLogItemService;
    private final StockBillLogService stockBillLogService;


    @EventListener(SkuDeleteEvent.class)
    @Transactional(rollbackFor = Exception.class)
    public void prodChangeEvent(SkuDeleteEvent event) {
        if (CollUtil.isEmpty(event.getSkuIds())) {
            return;
        }
        List<Long> skuIds = event.getSkuIds();
        // 移除采购订单关联sku信息
        purchaseProdService.deleteByProdId(null, skuIds);
        List<Long> supplierIds = supplierProdService.listSupplierIdBySkuIds(skuIds);
        List<Long> takeStockIds = takeStockProdService.listTakeStockIdBySkuIds(skuIds);
        // 删除供应商商品关联sku信息
        supplierProdService.deleteByProdId(null, skuIds);
        // 删除正在盘点的商品的信息
        takeStockProdService.deleteByProdId(null, skuIds);
        // 删除sku出入库信息
        stockBillLogItemService.remove(new LambdaQueryWrapper<StockBillLogItem>().in(StockBillLogItem::getSkuId, skuIds));
        // 清除已经没有商品记录的出入库记录
        stockBillLogService.removeEmptyLog();
        //清除缓存
        for (Long supplierId : supplierIds) {
            supplierProdService.removeCacheBySupplierId(supplierId);
        }
    }
}
