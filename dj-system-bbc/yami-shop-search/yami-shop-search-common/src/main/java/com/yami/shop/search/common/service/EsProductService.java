/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.search.common.service;

import com.yami.shop.bean.param.EsProductParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Yami
 */
public interface EsProductService {

    /**
     * 保存
     * @param prodId
     */
    void save(Long prodId);

    /**
     * 批量保存
     * @param prodIds
     */
    void saveBatch(List<Long> prodIds);

    /**
     * 更新
     * @param prodId
     */
    void update(Long prodId);

    /**
     * 批量更新
     * @param prodIds
     */
    void updateBatch(List<Long> prodIds);

    /**
     * 更新商品评论
     * @param prodId
     */
    void updateProdComm(Long prodId);

    /**
     * 根据平台分类，更新商品
     * @param categoryId
     */
    void updateByCategoryId(Long categoryId);

    /**
     * 根据店铺分类，更新商品
     * @param shopCategoryId
     */
    void updateByShopCategoryId(Long shopCategoryId);

    /**
     * 更新商品销量
     * @param prodId
     */
    void updateSoldNum(Long prodId);


    /**
     * 根据店铺id， 更新店铺内的商品
     * @param shopId
     */
    void updateByShopId(Long shopId);

    /**
     * 删除
     * @param prodId
     */
    void delete(Long prodId);

    /**
     * 批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据拼团活动id， 更新商品
     * @param id
     */
    void updateByGroupId(Long id);
}
