/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.search.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.bo.ProductBO;
import com.yami.shop.bean.enums.ProdStatusEnums;
import com.yami.shop.bean.enums.ProdType;
import com.yami.shop.bean.event.EsProductEvent;
import com.yami.shop.bean.model.Product;
import com.yami.shop.dao.ProductMapper;
import com.yami.shop.search.common.service.EsProductService;
import com.yami.shop.search.common.util.EsSearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Yami
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EsProductServiceImpl implements EsProductService {

    private final ApplicationEventPublisher eventPublisher;
    private final ProductMapper productMapper;
    private final RestHighLevelClient restHighLevelClient;

    @Override
    public void save(Long prodId) {
        ProductBO productBO = getProductBo(prodId);
        if (Objects.equals(productBO.getStatus(), ProdStatusEnums.DELETE.getValue())) {
            return;
        }
        EsSearchUtil.save(productBO.getProdId(), productBO);
    }

    @Override
    public void saveBatch(List<Long> prodIds) {
        List<ProductBO> productBOList = getProductBo(prodIds);
        if (CollUtil.isEmpty(productBOList)) {
            return;
        }
        Map<Long, ProductBO> map = productBOList.stream()
                .filter(productBO -> !Objects.equals(productBO.getStatus(), ProdStatusEnums.DELETE.getValue()))
                .collect(Collectors.toMap(ProductBO::getProdId, p -> p));
        EsSearchUtil.saveBatch(map);
    }

    @Override
    public void update(Long prodId) {
        if (Objects.isNull(prodId)) {
            return;
        }
        ProductBO productBO = getProductBo(prodId);
        if (Objects.equals(productBO.getStatus(), ProdStatusEnums.DELETE.getValue())) {
            delete(prodId);
            return;
        }
        EsSearchUtil.update(productBO);
    }

    @Override
    public void updateBatch(List<Long> prodIds) {
        if (CollUtil.isEmpty(prodIds)) {
            return;
        }
        List<ProductBO> productBOList = getProductBo(prodIds);
        Map<Long, ProductBO> map = new HashMap<>(productBOList.size());
        for (ProductBO productBO : productBOList) {
//            if (Objects.equals(productBO.getStatus(), ProdStatusEnums.DELETE.getValue())) {
//                delete(productBO.getProdId());
//                continue;
//            }
            map.put(productBO.getProdId(), productBO);
        }
        EsSearchUtil.updateBatch(map);
    }

    @Override
    public void updateProdComm(Long prodId) {
//        if (Objects.isNull(prodId)) {
//            return;
//        }
//        ProductBO productDb = getProductBo(prodId);
//        ProductBO productBO = new ProductBO();
//        productBO.setProdId(productDb.getProdId());
//        productBO.setStatus(productDb.getStatus());
//        productBO.setCommentNum(productDb.getCommentNum());
//        productBO.setPositiveRating(productDb.getPositiveRating());
//        EsSearchUtil.partialUpdate(productBO);
        update(prodId);

    }

    @Override
    public void updateByCategoryId(Long categoryId) {
        if (Objects.isNull(categoryId)) {
            return;
        }
        List<Long> ids = productMapper.listProdId(categoryId, null, null);
        updateBatch(ids);
    }

    @Override
    public void updateByShopCategoryId(Long shopCategoryId) {
        if (Objects.isNull(shopCategoryId)) {
            return;
        }
        List<Long> ids = productMapper.listProdId(null, shopCategoryId, null);
        updateBatch(ids);
    }

    @Override
    public void updateSoldNum(Long prodId) {
//        if (Objects.isNull(prodId)) {
//            return;
//        }
//        ProductBO productDb = getProductBo(prodId);
//        ProductBO productBO = new ProductBO();
//        productBO.setProdId(productDb.getProdId());
//        productBO.setStatus(productDb.getStatus());
//        productBO.setTotalStocks(productDb.getTotalStocks());
//        productBO.setSoldNum(productDb.getSoldNum());
//        productBO.setWaterSoldNum(productDb.getWaterSoldNum());
//        productBO.setActualSoldNum(productDb.getActualSoldNum());
//        productBO.setStatus(productDb.getStatus());
//        EsSearchUtil.partialUpdate(productBO);
        update(prodId);
    }

    @Override
    public void updateByShopId(Long shopId) {
        if (Objects.isNull(shopId)) {
            return;
        }
        List<Long> ids = productMapper.listProdId(null, null, shopId);
        updateBatch(ids);
    }

    @Override
    public void delete(Long prodId) {
        if (Objects.isNull(prodId)) {
            return;
        }
        EsSearchUtil.delete(prodId);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        EsSearchUtil.deleteBatch(ids);
    }

    @Override
    public void updateByGroupId(Long id) {
        if (Objects.isNull(id)) {
            return;
        }
        List<Product> products = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .eq(Product::getProdType, ProdType.PROD_TYPE_GROUP.value())
                .eq(Product::getActivityId, id));
        List<Long> prodIds = products.stream().map(Product::getProdId).collect(Collectors.toList());
        updateBatch(prodIds);
    }

    /**
     * 获取商品信息
     * @param prodId 商品id
     * @return
     */
    private ProductBO getProductBo(Long prodId) {
        if (Objects.isNull(prodId)) {
            return null;
        }
        List<ProductBO> productList = getProductBo(Collections.singletonList(prodId));
        if (CollUtil.isEmpty(productList)) {
            return null;
        }
        return productList.get(0);
    }

    /**
     * 获取商品信息
     * @param prodIds 商品id列表
     * @return
     */
    private List<ProductBO> getProductBo(List<Long> prodIds) {
        if (CollUtil.isEmpty(prodIds)) {
            return null;
        }
        List<ProductBO> productList = new ArrayList<>();
        try {
            eventPublisher.publishEvent(new EsProductEvent(prodIds, productList));
        } catch (Exception e) {
            log.error("获取es商品数据异常：{}", e);
        }
        if (CollUtil.isEmpty(productList) || Objects.isNull(productList.get(0))) {
            log.error("商品id为：{}的商品数据异常！", prodIds);
            return null;
        }
        return productList;
    }
}
