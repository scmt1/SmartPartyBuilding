/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.search.common.util;

import com.yami.shop.bean.vo.search.*;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.i18n.LanguageEnum;
import com.yami.shop.common.util.Json;
import com.yami.shop.search.common.constant.EsConstant;
import com.yami.shop.search.common.param.EsPageParam;
import com.yami.shop.search.common.vo.EsPageVO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author YXF
 * @date 2021/07/29
 */
@Component
public class SearchResponseUtil {

    /**
     *
     */
    public static EsProductSearchVO getProductSearch(SearchResponse response) {

        // 1、返回的所有查询到的商品
        EsProductSearchVO esProductSearchVO = new EsProductSearchVO();
        //===============spu列表信息====================//
        esProductSearchVO.setProducts(buildSpuSearchList(response.getHits()));

        //===============聚合信息====================//
        Aggregations aggregations = response.getAggregations();
        if (Objects.nonNull(aggregations)) {
            processingAggregationsData(esProductSearchVO,aggregations);
        }

        return esProductSearchVO;
    }

    /**
     * 从es返回的数据中获取spu列表
     * @param hits es返回的数据
     * @return
     */
    public static List<ProductSearchVO> buildSpuSearchList(SearchHits hits) {
        String spuName = null;
        String sellingPoin = null;
        if (Objects.equals(I18nMessage.getLang(), LanguageEnum.LANGUAGE_ZH_CN.getLang())) {
            spuName = EsConstant.PROD_NAME_ZH;
            sellingPoin = EsConstant.BRIEF_ZH;
        } else {
            spuName = EsConstant.PROD_NAME_EN;
            sellingPoin = EsConstant.BRIEF_EN;
        }
        List<ProductSearchVO> prodList = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            String json = hit.getSourceAsString();
            ProductSearchVO productSearchVO = Json.parseObject(json, ProductSearchVO.class);
            productSearchVO.setProdName(handleAggregationsLang(json, spuName, EsConstant.PROD_NAME_ZH));
            productSearchVO.setBrief(handleAggregationsLang(json, sellingPoin, EsConstant.BRIEF_ZH));
            prodList.add(productSearchVO);
        }
        return prodList;
    }
    /**
     * 处理聚合数据
     * @param aggregations
     */
    private static void processingAggregationsData(EsProductSearchVO esProductSearchVO, Aggregations aggregations) {
        String brandName = null;
        String categoryName = null;
        String attrName = null;
        String attrValueName = null;
        if (Objects.equals(I18nMessage.getLang(), LanguageEnum.LANGUAGE_ZH_CN.getLang())) {
            brandName = EsConstant.BRAND_NAME_ZH;
            categoryName = EsConstant.CATEGORY_NAME_ZH;
        } else {
            brandName = EsConstant.BRAND_NAME_EN;
            categoryName = EsConstant.CATEGORY_NAME_EN;
        }
        //===============品牌信息====================//
        ParsedNested brandNested = aggregations.get(EsConstant.BRAND);
        if (Objects.nonNull(brandNested)) {
            esProductSearchVO.setBrands(new ArrayList<>());
            List<String> brandJson = handleAggregations(brandNested, EsConstant.BRAND_ID);
            for (String json : brandJson) {
                BrandSearchVO brandSearchVO = Json.parseObject(json, BrandSearchVO.class);
                brandSearchVO.setBrandName(handleAggregationsLang(json, brandName, EsConstant.BRAND_NAME_ZH));
                esProductSearchVO.getBrands().add(brandSearchVO);
            }
        }
        //===============分类信息====================//
        loadCategoryAggregationsData(esProductSearchVO, aggregations, categoryName);

        //===============店铺信息====================//
        ParsedLongTerms shopTerms = aggregations.get(EsConstant.SHOP);
        if (Objects.nonNull(shopTerms)) {
            List<? extends Terms.Bucket> shopBuckets = shopTerms.getBuckets();
            for (Terms.Bucket bucket : shopBuckets) {
                esProductSearchVO.setShopInfo(new ShopSearchVO());
                esProductSearchVO.getShopInfo().setShopId(Long.valueOf(bucket.getKey().toString()));
            }
        }
    }

    private static void loadCategoryAggregationsData(EsProductSearchVO esProductSearchVO, Aggregations aggregations, String categoryName) {
        esProductSearchVO.setCategories(new ArrayList<>());
        String categoryId = null;
        ParsedNested categoriesNested = null;
        // 平台分类
        if (Objects.nonNull(aggregations.get(EsConstant.CATEGORY))) {
            categoryId = EsConstant.CATEGORY_ID;
            categoriesNested = aggregations.get(EsConstant.CATEGORY);
        }
        if (Objects.nonNull(categoriesNested)) {
            esProductSearchVO.setCategories(new ArrayList<>());
            List<String> categoryJson = handleAggregations(categoriesNested, categoryId);
            for (String json : categoryJson) {
                CategorySearchVO categorySearchVO = Json.parseObject(json, CategorySearchVO.class);
                categorySearchVO.setName(handleAggregationsLang(json, categoryName, EsConstant.CATEGORY_NAME_ZH));
                esProductSearchVO.getCategories().add(categorySearchVO);
            }
        }
    }

    /**
     * 处理聚合数据
     * @param brandNested
     * @return
     */
    public static List<String> handleAggregations(ParsedNested brandNested, String id) {
        List<String> dataList = new ArrayList<>();
        ParsedLongTerms brandLongTerms = brandNested.getAggregations().get(id);
        List<? extends Terms.Bucket> buckets = brandLongTerms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            ParsedTopHits parsedTopHits = bucket.getAggregations().get(EsConstant.TOP_HITS_DATA);
            for (SearchHit hit : parsedTopHits.getHits().getHits()) {
                String sourceAsString = hit.getSourceAsString();
                dataList.add(sourceAsString);
            }
        }
        return dataList;
    }

    /**
     * 从聚合数据中获取商品列表
     * @param response
     * @return
     */
    public static Map<Long, List<ProductSearchVO>> loadSpuMapByAggregations(SearchResponse response) {
        Aggregations aggregations = response.getAggregations();
        if (Objects.isNull(aggregations.getAsMap())) {
            return new HashMap<>(0);
        }
        ParsedLongTerms shopTerm = aggregations.get(EsConstant.PROD_LIST);
        Map<Long, List<ProductSearchVO>> prodMap = new HashMap<>(10);
        if (Objects.nonNull(shopTerm)) {
            List<? extends Terms.Bucket> buckets = shopTerm.getBuckets();
            for (Terms.Bucket bucket : buckets) {
                Aggregations shopAgg = bucket.getAggregations();
                ParsedTopHits shopHits = shopAgg.get(EsConstant.TOP_HITS_DATA);
                prodMap.put(Long.valueOf(bucket.getKey().toString()), buildSpuSearchList(shopHits.getHits()));
            }
        }
        return prodMap;
    }

    /**
     * 构建分页数据
     * @param pageParam
     * @param esPageVO
     * @param response
     */
    public static void buildSearchPage(EsPageParam pageParam, EsPageVO<?> esPageVO, SearchResponse response) {
        //总记录数
        long total = response.getHits().getTotalHits().value;
        esPageVO.setTotal(total);
        // 总页码
        int totalPages = (int)total % pageParam.getSize() == 0 ?
                (int)total / pageParam.getSize() : ((int)total / pageParam.getSize() + 1);
        esPageVO.setPages(totalPages);
    }

    /**
     * 处理聚合国际化信息
     * @param json 数据
     * @param field 字段
     * @return 对应语言的字段
     */
    private static String handleAggregationsLang(String json, String field, String defaultField) {
        Map<String, Object> map = Json.parseObject(json, Map.class);
        Object object;
        // 找不到指定语言的数据，就查默认语言
        if (Objects.isNull(map.get(field))) {
            object = map.get(defaultField);
        }
        // 获取指定语言的数据
        else {
            object = map.get(field);
        }
        // 没有查到数据
        if (Objects.isNull(object)) {
            return null;
        }
        return object.toString();
    }

}
