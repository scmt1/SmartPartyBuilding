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
import com.yami.shop.bean.enums.EsOperationType;
import com.yami.shop.bean.event.EsProductUpdateEvent;
import com.yami.shop.bean.model.Product;
import com.yami.shop.service.ProductService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yami
 */
@Component
public class ProdTask {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ProductService productService;
    @Autowired
    private ApplicationContext applicationContext;


    @XxlJob("recoveryPreSaleProd")
    public void recoveryPreSaleProd(){
        logger.info("过了预售时间的商品，恢复成普通商品状态。。。");
        // 获取30分钟之前未支付的订单
        List<Product> products = productService.recoveryPreSaleProd();
        if(CollectionUtils.isEmpty(products)){
            return;
        }
        for (Product product : products) {
            //清除缓存
            productService.removeProdCacheByProdId(product.getProdId());
        }
    }

    @XxlJob("offlineExpireVirtualProd")
    public void offlineExpireVirtualProd(){
        logger.info("过了核销时间的虚拟商品，进行下架操作。");
        // 获取30分钟之前未支付的订单
        List<Product> products = productService.handleExpireVirtualProd();
        if(CollectionUtils.isEmpty(products)){
            return;
        }
        List<Long> ids = new ArrayList<>();
        for (Product product : products) {
            //清除缓存
            productService.removeProdCacheByProdId(product.getProdId());
            ids.add(product.getProdId());
        }
        applicationContext.publishEvent(new EsProductUpdateEvent(null, ids, EsOperationType.UPDATE_BATCH));
    }


}
