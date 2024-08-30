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

import com.yami.shop.bean.enums.EsOperationType;
import com.yami.shop.bean.event.EsProductUpdateEvent;
import com.yami.shop.search.common.service.EsProductService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 获取es商品数据事件
 * @author yami
 */
@Component("EsProductUpdateListener")
@AllArgsConstructor
public class EsProductUpdateListener {

    private final EsProductService esProductService;

    @EventListener(EsProductUpdateEvent.class)
    public void defaultEsProductListener(EsProductUpdateEvent event) {
        EsOperationType esOperationType = event.getEsOperationType();
        Long id = event.getId();
        List<Long> ids = event.getIds();
        // 保存
        if (Objects.equals(esOperationType, EsOperationType.SAVE)) {
            esProductService.save(id);
        }
        // 更新
        else if (Objects.equals(esOperationType, EsOperationType.UPDATE)) {
            esProductService.update(id);
        }
        // 删除
        else if (Objects.equals(esOperationType, EsOperationType.DELETE)) {
            esProductService.update(id);
            //esProductService.delete(id);
        }
        // 批量保存
        else if (Objects.equals(esOperationType, EsOperationType.SAVE_BATCH)) {
            esProductService.saveBatch(ids);
        }
        // 批量更新
        else if (Objects.equals(esOperationType, EsOperationType.UPDATE_BATCH)) {
            esProductService.updateBatch(ids);
        }
        // 批量删除
        else if (Objects.equals(esOperationType, EsOperationType.DELETE_BATCH)) {
            esProductService.updateBatch(ids);
            //esProductService.deleteBatch(ids);
        }
        // 更新商品评论
        else if (Objects.equals(esOperationType, EsOperationType.UPDATE_PROD_COMM)) {
            esProductService.updateProdComm(id);
        }
        // 更新商品销量
        else if (Objects.equals(esOperationType, EsOperationType.UPDATE_SOLD_NUM)) {
            esProductService.updateSoldNum(id);
        }
        // 根据分类id，更新商品
        else if (Objects.equals(esOperationType, EsOperationType.UPDATE_BY_CATEGORY_ID)) {
            esProductService.updateByCategoryId(id);
        }
        // 根据店铺分类id，更新商品
        else if (Objects.equals(esOperationType, EsOperationType.UPDATE_BY_SHOP_CATEGORY_ID)) {
            esProductService.updateByShopCategoryId(id);
        }
        // 根据店铺id，更新商品
        else if (Objects.equals(esOperationType, EsOperationType.UPDATE_BY_SHOP_ID)) {
            esProductService.updateByShopId(id);
        }
        // 根据拼团活动id，更新商品
        else if (Objects.equals(esOperationType, EsOperationType.UPDATE_BY_GROUP_ID)) {
            esProductService.updateByGroupId(id);
        }

    }
}
