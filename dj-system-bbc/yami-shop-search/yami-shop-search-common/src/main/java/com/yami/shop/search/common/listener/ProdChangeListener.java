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

import com.yami.shop.bean.event.ProdChangeEvent;
import com.yami.shop.bean.model.Product;
import com.yami.shop.dao.SupplierProdMapper;
import com.yami.shop.dao.TakeStockProdMapper;
import com.yami.shop.service.ProdCommService;
import com.yami.shop.service.PurchaseProdService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


/**
 * @author Yami
 */
@Slf4j
@Component("multishopProdChangeListener")
@AllArgsConstructor
public class ProdChangeListener {


    private final ProdCommService prodCommService;

    private final SupplierProdMapper supplierProdMapper;

    private final TakeStockProdMapper takeStockProdMapper;

    private final PurchaseProdService purchaseProdService;


    @EventListener(ProdChangeEvent.class)
    @Transactional(rollbackFor = Exception.class)
    public void prodChangeEvent(ProdChangeEvent event) {
        Product product = event.getProduct();
        Long prodId = product.getProdId();
        // 删除评价信息
        Set<Long> prodIds = new HashSet<>();
        prodIds.add(prodId);
        prodCommService.deleteByProdIds(prodIds);
        // 删除供应商商品信息
        supplierProdMapper.deleteByProdId(prodId);
        // 删除盘点商品信息
        takeStockProdMapper.deleteByProdId(prodId);
        // 删除盘点商品信息
        takeStockProdMapper.deleteByProdId(prodId);
        //采购入库
        purchaseProdService.deleteByProdId(prodId, null);
    }

}
