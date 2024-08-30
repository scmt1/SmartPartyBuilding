/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.constants;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
public interface ProductCacheNames {

    /**
     * 前缀
     */
    String PRODUCT_PREFIX = "mall4cloud_product:";

    /**
     * 用户端分类列表缓存key
     */
    String CATEGORY_INFO = PRODUCT_PREFIX + "category:info:";
    /**
     * 根据店铺id和上级id，获取分类列表缓存key
     */
    String CATEGORY_RATE = PRODUCT_PREFIX + "category:category_rate:";
    /**
     * 购物车商品数量
     */
    String SHOP_CART_ITEM_COUNT = "shop_cart:count:";

    /**
     * 获取商品的缓存key
     */
    String PRODUCT_KEY = "product";

    /**
     * 获取商品扩展信息的缓存key
     */
    String PROD_EXTENSION_INFO_KEY = "prodExtensionInfo";


    /**
     * 根据skuId获取sku的缓存key
     */
    String SKU_KEY = "sku";

    /**
     * 获取sku列表的缓存key
     */
    String SKU_LIST_KEY = "skuList";

    /**
     * 获取sku库存列表的缓存key
     */
    String LIST_SKU_STOCK_KEY = "listSkuStock";

    /**
     * 店铺获取签约的所有分类列表
     */
    String LIST_SIGNING_CATEGORY_BY_SHOP_ID = "category_shop:list_signing:";
    /**
     * 店铺签约的可用分类列表(发布商品时使用）
     */
    String LIST_AVAILABLE_SIGNING_CATEGORY_BY_SHOP_ID = "category_shop:list_available:";

}
