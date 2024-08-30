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

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.yami.shop.bean.bo.ProductBO;
import com.yami.shop.bean.enums.ProdStatusEnums;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.Json;
import com.yami.shop.search.common.constant.EsConstant;
import com.yami.shop.search.common.constant.EsIndexEnum;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author YXF
 * @date 2021/07/29
 */
@Component
public class EsSearchUtil {

    private static final Logger log = LoggerFactory.getLogger(EsSearchUtil.class);
    private static RestHighLevelClient restHighLevelClient;

    /**
     * 正常来说普通方法才是属于实体对象（也就是New出来的对象）的，spring注入是在容器中实例化对象，静态变量是无法注入的
     * 所以需要通过构造方法的方式来进行注入，或者使用@PostConstruct注解
     */
    @Autowired
    public EsSearchUtil(RestHighLevelClient restHighLevelClient) {
        EsSearchUtil.restHighLevelClient = restHighLevelClient;
    }


    public static void save(Long id, ProductBO productBO) {
        if (Objects.isNull(productBO)) {
            return;
        }
        try {
            IndexRequest request = new IndexRequest(EsIndexEnum.PRODUCT.value())
                    .id(String.valueOf(id))
                    .source(Objects.requireNonNull(Json.toJsonString(productBO)), XContentType.JSON);
            IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            log.info(indexResponse.toString());
        } catch (IOException e) {
            log.error(e.toString());
            throw new YamiShopBindException("保存es信息异常", e);
        }
    }


    public static void saveBatch(Map<Long, ProductBO> map) {
        if (map.size() == 0) {
            return;
        }
        try {
            BulkRequest request = new BulkRequest();
            for (Long id : map.keySet()) {
                IndexRequest indexRequest = new IndexRequest(EsIndexEnum.PRODUCT.value());
                indexRequest.id(String.valueOf(id));
                indexRequest.source(Objects.requireNonNull(Json.toJsonString(map.get(id))), XContentType.JSON);
                request.add(indexRequest);
            }
            BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            log.info(bulkResponse.toString());
        } catch (IOException e) {
            log.error(e.toString());
            throw new YamiShopBindException("批量保存es信息异常", e);
        }
    }


    public static void update(ProductBO productBO) {
        delete(productBO.getProdId());
        save(productBO.getProdId(), productBO);
    }


    public static void partialUpdate(ProductBO productBO) {
        if (Objects.equals(productBO.getStatus(), ProdStatusEnums.DELETE.getValue())) {
            delete(productBO.getProdId());
            return;
        }
        if (Objects.isNull(productBO)) {
            return;
        }
        try {
            UpdateRequest request = new UpdateRequest(EsIndexEnum.PRODUCT.value(), String.valueOf(productBO.getProdId()))
                    .doc(Json.toJsonString(productBO), XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(request, RequestOptions.DEFAULT);
            log.info(updateResponse.toString());
        } catch (IOException e) {
            log.error("部分更新es信息异常信息：{}，商品信息：{}", e.toString(), productBO.toString());
            throw new YamiShopBindException("部分更新es信息异常", e);
        }
    }


    public static void updateBatch(Map<Long, ProductBO> map) {
        deleteBatch(map.keySet());
        saveBatch(map);
    }


    public static void delete(Long id) {
        // 删除数据
        try {
            DeleteRequest request = new DeleteRequest(EsIndexEnum.PRODUCT.value(),String.valueOf(id));
            DeleteResponse deleteResponse = restHighLevelClient.delete(request,RequestOptions.DEFAULT);
            log.info(deleteResponse.toString());
        } catch (IOException e) {
            log.error(e.toString());
            throw new YamiShopBindException("删除es信息异常", e);
        }
    }


    public static void deleteBatch(Collection<Long> ids) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(EsIndexEnum.PRODUCT.value());
        request.setQuery(new TermsQueryBuilder("prodId", ids));
        try {
            log.info("构建的DSL删除语句 {}", request.toString());
            BulkByScrollResponse bulkByScrollResponse = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
            log.info(bulkByScrollResponse.toString());
        } catch (IOException e) {
            log.error(e.toString());
            throw new YamiShopBindException("批量删除es信息异常", e);
        }
    }

    /**
     * 分页检索
     */
    public static SearchResponse search(SearchRequest searchRequest) {
        SearchResponse response = null;
        try {
            //2、执行检索请求
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            log.debug("搜索返回结果：" + response.toString());
        } catch (IOException e) {
            log.error(e.toString());
            throw new YamiShopBindException("搜索服务出了点小差，请稍后再试", e);
        }
        return response;
    }

    /**
     * 滚动检索
     */
    public static SearchResponse scroll(SearchScrollRequest searchScrollRequest) {
        SearchResponse response = null;
        try {
            //2、执行检索请求
            response = restHighLevelClient.scroll(searchScrollRequest, RequestOptions.DEFAULT);

            log.debug("滚动搜索返回结果：" + response.toString());
        } catch (IOException e) {
            log.error(e.toString());
            throw new YamiShopBindException("滚动搜索服务出了点小差，请稍后再试", e);
        }
        return response;
    }

    /**
     * 删除滚动
     */
    public static boolean clearScroll(String scrollId) {

        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = null;
        try {
            //2、执行检索请求
            clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.toString());
            throw new YamiShopBindException("删除滚动服务出了点小差，请稍后再试", e);
        }
        if (!clearScrollResponse.isSucceeded()) {
            log.error("删除滚动返回结果：" + clearScrollResponse.toString());
            throw new YamiShopBindException("");
        }
        return clearScrollResponse.isSucceeded();
    }


    /**
     *
     */
    public static QueryBuilder termsQueryByArray(String name, String data) {
        List<String> array = StrUtil.split(data, Constant.COMMA);
        return QueryBuilders.termsQuery(name, array);
    }


    /**
     *
     */
    public static QueryBuilder nestedQuery(String nestedName, String name, Long data) {
        return nestedQuery(nestedName, name, data.toString());
    }

    /**
     *
     */
    public static QueryBuilder nestedQuery(String nestedName, String name, String data) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.should(QueryBuilders.termsQuery(name, data));
        return QueryBuilders.nestedQuery(nestedName, boolQuery, ScoreMode.None);
    }

    /**
     *
     */
    public static QueryBuilder nestedQuery(String nestedName, String name, List list) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.should(QueryBuilders.termsQuery(name, list));
        return QueryBuilders.nestedQuery(nestedName, boolQuery, ScoreMode.None);
    }

    /**
     *
     */
    public static QueryBuilder nestedQueryByArray(String nestedName, String name, String data) {
        String[] ids = data.split(Constant.COMMA);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (String brandId : ids) {
            boolQuery.should(QueryBuilders.termsQuery(name, brandId));
        }
        return QueryBuilders.nestedQuery(nestedName,boolQuery, ScoreMode.None);
    }

    /**
     *
     */
    public static RangeQueryBuilder rangeQuery(String name, Long minValue, Long maxValue) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(name);
        if (Objects.nonNull(minValue)) {
            rangeQueryBuilder.gte(minValue);
        }
        if (Objects.nonNull(maxValue)) {
            rangeQueryBuilder.lte(maxValue);
        }
        return rangeQueryBuilder;
    }

    /**
     *
     */
    public static NestedAggregationBuilder nestedAggregation(String nestedName, String fieldName, String name) {
        return nestedAggregation(nestedName, fieldName, name, null, null);
    }

    /**
     *
     */
    public static NestedAggregationBuilder nestedAggregation(String nestedName, String fieldName, String name, String[] fetchSource) {
        return nestedAggregation(nestedName, fieldName, name, fetchSource, null);
    }

    /**
     *
     */
    public static NestedAggregationBuilder nestedAggregation(String nestedName, String fieldName, String name, Integer size) {
        return nestedAggregation(nestedName, fieldName, name, null, size);
    }

    /**
     *
     */
    public static NestedAggregationBuilder nestedAggregation(String nestedName, String fieldName, String name, String[] fetchSource, Integer size) {
        if (Objects.isNull(size)) {
            size = 1;
        }
        NestedAggregationBuilder nested = AggregationBuilders.nested(nestedName, nestedName);
        TermsAggregationBuilder terms = AggregationBuilders.terms(name).field(fieldName).size(10);
        TopHitsAggregationBuilder topHits = AggregationBuilders
                .topHits(EsConstant.TOP_HITS_DATA)
                .size(size);
        // 指定响应的数据字段
        if (ArrayUtil.isNotEmpty(fetchSource)) {
            topHits.fetchSource(fetchSource, null);
        }
        terms.subAggregation(topHits);
        nested.subAggregation(terms);
        return nested;
    }

}
