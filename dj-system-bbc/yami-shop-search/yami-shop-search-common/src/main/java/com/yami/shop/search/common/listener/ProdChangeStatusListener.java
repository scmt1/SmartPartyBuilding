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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.enums.ProdStatusEnums;
import com.yami.shop.bean.event.ProdChangeStatusEvent;
import com.yami.shop.bean.model.IndexImg;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.order.GeneralActivitiesOrder;
import com.yami.shop.service.IndexImgService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 商品状态修改监听
 * @author LGH
 */
@Component("prodChangeStatus")
@AllArgsConstructor
public class ProdChangeStatusListener {
    private final IndexImgService indexImgService;

    /**
     *
     */
    @EventListener(ProdChangeStatusEvent.class)
    @Order(GeneralActivitiesOrder.DEFAULT)
    public void prodChangeStatusListener(ProdChangeStatusEvent event) {
        Product product = event.getProduct();
        Integer status = event.getStatus();
        // 商品上线，则不进行处理
        // 或者进入待审核状态6也不处理，因为进入该状态要么是新商品，要么是下架商品
        if (Objects.equals(status, ProdStatusEnums.NORMAL.getValue()) || Objects.equals(status,ProdStatusEnums.AUDIT.getValue())) {
            return;
        }
        //移除轮播图中绑定的商品
        List<IndexImg> list = indexImgService.list(new LambdaQueryWrapper<IndexImg>()
                .eq(IndexImg::getType, 0)
                .eq(IndexImg::getRelation, product.getProdId())
        );
        List<Long> ids = new ArrayList<>();
        Set<Long> shopIds = new HashSet<>();
        for (IndexImg indexImg : list) {
            ids.add(indexImg.getImgId());
            shopIds.add(indexImg.getShopId());
        }
        // 更新轮播图数据
        indexImgService.updateImgProd(ids);
        // 清除缓存
        for (Long shopId : shopIds) {
            indexImgService.removeIndexImgCacheByShopId(shopId);
        }
    }
}
