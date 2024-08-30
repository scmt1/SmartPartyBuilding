/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.search.common.constant;

import com.yami.shop.common.config.Constant;

/**
 * 配置缓存名字
 *
 * @author lhd
 * @date 2020/12/30
 */
public interface EsConstant {

    /**
     * 商品
     */
    String PROD_ID = "prodId";
    String PROD_NAME_ZH = "prodNameZh";
    String PROD_NAME_EN = "prodNameEn";
    String BRIEF_ZH = "briefZh";
    String BRIEF_EN = "briefEn";
    String PRICE = "price";
    String ORI_PRICE = "oriPrice";
    String ACTIVITY_PRICE = "activityPrice";
    String ACTIVITY_ORIGINAL_PRICE = "activityOriginalPrice";
    String SCORE_PRICE = "scorePrice";
    String PIC = "pic";
    String IMGS = "imgs";
    String PROD_TYPE = "prodType";
    String MOLD = "mold";
    String PRE_SELL_STATUS = "preSellStatus";
    String SHOP_NAME = "shopName";
    String SHOP_ID = "shopId";
    String SHOP_TYPE = "shopType";
    String STATUS = "status";
    String HAS_STOCK = "hasStock";
    String TOTAL_STOCKS = "totalStocks";
    String SOLD_NUM = "soldNum";
    String ACTUAL_SOLD_NUM = "actualSoldNum";
    String WATER_SOLD_NUM = "waterSoldNum";
    String COMMENT_NUM = "commentNum";
    String POSITIVE_RATING = "positiveRating";
    String DELIVERY_MODE = "deliveryMode";
    String DELIVERIES = "deliveries";
    String CREATE_TIME = "createTime";
    String UPDATE_TIME = "updateTime";
    String PUTAWAY_TIME = "putawayTime";
    String ACTIVITY_START_TIME = "activityStartTime";
    String BRAND = "brand";
    String SEQ = "seq";
    String IS_TOP = "isTop";
    String ACTIVITY_ID = "activityId";
    String PRIMARY_CATEGORY_ID = "primaryCategoryId";
    String SECONDARY_CATEGORY_ID = "secondaryCategoryId";
    String CATEGORY = "category";
    String SHOP_CATEGORY_ID = "shopCategoryId";
    String APP_DISPLAY = "appDisplay";



    /**
     * 品牌
     */
    String BRAND_ID = "brandId";
    String BRAND_IMG = "brandImg";
    String BRAND_NAME_ZH = "brandNameZh";
    String BRAND_NAME_EN = "brandNameEn";
    String BRAND_UNION_ID = BRAND + Constant.PERIOD + BRAND_ID;
    String BRAND_UNION_IMG = BRAND + Constant.PERIOD + BRAND_IMG;
    String BRAND_UNION_NAME_ZH = BRAND + Constant.PERIOD + BRAND_NAME_ZH;
    String BRAND_UNION_NAME_EN = BRAND + Constant.PERIOD + BRAND_NAME_EN;

    /**
     * 分类
     */
    String CATEGORY_ID = "categoryId";
    String CATEGORY_NAME_ZH = "categoryNameZh";
    String CATEGORY_NAME_EN = "categoryNameEn";
    String CATEGORY_UNION_ID = CATEGORY + Constant.PERIOD + CATEGORY_ID;
    String CATEGORY_UNION_NAME_ZH = CATEGORY + Constant.PERIOD + CATEGORY_NAME_ZH;
    String CATEGORY_UNION_NAME_EN = CATEGORY + Constant.PERIOD + CATEGORY_NAME_EN;


    /**
     * 其余字段
     */
    String TOP_HITS_DATA = "top_hits_data";
    String SHOP = "shop";
    String SHOP_CATEGORY = "shopCategory";
    String PROD_LIST = "prodList";

    /**
     * 指定返回字段
     */
    String[] BRAND_INCLUDE = {BRAND_UNION_ID, BRAND_UNION_IMG, BRAND_UNION_NAME_ZH, BRAND_UNION_NAME_EN};
    String[] CATEGORY_INCLUDE = {CATEGORY_UNION_ID, CATEGORY_UNION_NAME_ZH, CATEGORY_UNION_NAME_EN};
    String[] APP_FETCH_SOURCE = {PROD_ID,PROD_NAME_ZH,PROD_NAME_EN,BRIEF_ZH,BRIEF_EN,SHOP_ID,PRICE,PROD_TYPE,ORI_PRICE,ACTIVITY_PRICE,SCORE_PRICE,SOLD_NUM,COMMENT_NUM,POSITIVE_RATING,PIC,IMGS,ACTIVITY_ID,ACTIVITY_ORIGINAL_PRICE,ACTIVITY_START_TIME};
    String[] SHOP_FETCH_SOURCE = {PROD_ID,PROD_NAME_ZH,PROD_NAME_EN,PIC,PRICE,ORI_PRICE,SCORE_PRICE,ACTUAL_SOLD_NUM,TOTAL_STOCKS,SEQ,STATUS,BRIEF_ZH,BRIEF_EN,IS_TOP,DELIVERY_MODE,PROD_TYPE,MOLD};
    String[] PLATFORM_FETCH_SOURCE = {SHOP_ID,SHOP_NAME,PROD_ID,PROD_NAME_ZH,PROD_NAME_EN,PIC,PRICE,ORI_PRICE,SCORE_PRICE,ACTUAL_SOLD_NUM,TOTAL_STOCKS,SEQ,STATUS,BRIEF_ZH,BRIEF_EN,IS_TOP,MOLD,DELIVERY_MODE,WATER_SOLD_NUM,PROD_TYPE};
    String[] SIMPLE_FETCH_SOURCE = {PROD_ID,UPDATE_TIME};
    String[] RENOVATION_FETCH_SOURCE = {PROD_ID,PROD_NAME_ZH,PROD_NAME_EN,PROD_TYPE,STATUS,BRIEF_ZH,BRIEF_EN,SHOP_ID,PRICE,ORI_PRICE,ACTIVITY_PRICE,SCORE_PRICE,SOLD_NUM,PIC,ACTIVITY_ID,ACTIVITY_ORIGINAL_PRICE,SHOP_NAME,TOTAL_STOCKS,ACTIVITY_START_TIME};
    String[] EXCEL_FETCH_SOURCE = {PROD_ID,PROD_NAME_ZH,PROD_NAME_EN,PROD_TYPE,STATUS,BRIEF_ZH,BRIEF_EN,SHOP_NAME};
}
