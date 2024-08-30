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
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.yami.shop.bean.enums.ProdStatusEnums;
import com.yami.shop.bean.enums.ProdType;
import com.yami.shop.bean.param.EsProductParam;
import com.yami.shop.bean.param.ProductExportParam;
import com.yami.shop.bean.vo.search.EsProductSearchVO;
import com.yami.shop.bean.vo.search.ProductSearchVO;
import com.yami.shop.common.enums.StatusEnum;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.i18n.LanguageEnum;
import com.yami.shop.common.util.PoiExcelUtil;
import com.yami.shop.search.common.constant.EsConstant;
import com.yami.shop.search.common.constant.EsIndexEnum;
import com.yami.shop.search.common.constant.EsProductSortEnum;
import com.yami.shop.search.common.param.EsPageParam;
import com.yami.shop.search.common.service.SearchProductService;
import com.yami.shop.search.common.util.EsSearchUtil;
import com.yami.shop.search.common.util.SearchResponseUtil;
import com.yami.shop.search.common.vo.EsPageVO;
import com.yami.shop.service.ProductExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Yami
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SearchProductServiceImpl implements SearchProductService {

    private final ProductExcelService productExcelService;

    /**
     * 通过搜索信息分页搜索es数据并聚合返回的信息
     * @param pageParam 分页数据
     * @param productParam 商品搜索条件
     * @return 搜索结果
     */
    @Override
    public EsPageVO<EsProductSearchVO> page(EsPageParam pageParam, EsProductParam productParam, Boolean isAgg) {
        if (Objects.isNull(productParam.getAppDisplay())) {
            productParam.setAppDisplay(Boolean.TRUE);
        }
        SearchResponse response = pageSearchResult(pageParam, productParam, isAgg);
        return buildSearchResult(pageParam,response);
    }


    /**
     * 获取搜索扩展信息
     * @param pageParam 分页数据
     * @param productParam 商品搜索条件
     * @return 搜索结果
     */
    @Override
    public EsProductSearchVO searchExtension(EsPageParam pageParam, EsProductParam productParam) {
        EsProductParam newEsProductParam = new EsProductParam();
        newEsProductParam.setKeyword(productParam.getKeyword());
        newEsProductParam.setPrimaryCategoryId(productParam.getPrimaryCategoryId());
        newEsProductParam.setSecondaryCategoryId(productParam.getSecondaryCategoryId());
        newEsProductParam.setShopCategoryId(productParam.getShopCategoryId());
        pageParam.setSize(0);
        SearchResponse response = pageSearchResult(pageParam, newEsProductParam, Boolean.TRUE);
        return SearchResponseUtil.getProductSearch(response);
    }

    /**
     * 根据spuId列表，获取spu信息
     * @param productParam  商品搜索条件
     * @return spu列表
     */
    @Override
    public List<ProductSearchVO> listSpuByProdIds(EsProductParam productParam) {
        EsPageParam pageParam = new EsPageParam();
        pageParam.setCurrent(1);
        pageParam.setSize(productParam.getProdIds().size());
        SearchResponse response = pageSearchResult(pageParam, productParam, Boolean.FALSE);
        return SearchResponseUtil.buildSpuSearchList(response.getHits());
    }

    @Override
    public EsPageVO<ProductSearchVO> adminPage(EsPageParam pageParam, EsProductParam productParam) {
        EsPageVO<ProductSearchVO> result = new EsPageVO<>();
        SearchResponse response = pageSearchResult(pageParam, productParam, Boolean.FALSE);
        // 商品信息
        result.setRecords(SearchResponseUtil.buildSpuSearchList(response.getHits()));
        // 分页信息
        SearchResponseUtil.buildSearchPage(pageParam, result, response);
        return result;
    }

    @Override
    public List<ProductSearchVO> simpleList(List<Long> spuIds) {
        EsProductParam productParam = new EsProductParam();
        productParam.setFetchSource(EsConstant.SIMPLE_FETCH_SOURCE);
        productParam.setProdIds(spuIds);
        productParam.setGetDelete(true);
        List<ProductSearchVO> list = listSpuByProdIds(productParam);
        return list;
    }

    @Override
    public EsPageVO<ProductSearchVO> renovationPage(EsPageParam pageParam, EsProductParam productParam) {
        if (Objects.isNull(pageParam.getSize()) || pageParam.getSize() == 0) {
            return new EsPageVO();
        }
        productParam.setFetchSource(EsConstant.RENOVATION_FETCH_SOURCE);
        // 不需要积分商品和活动商品
        if (Objects.isNull(productParam.getProdType())) {
            List<Integer> types = new ArrayList<>();
            types.add(ProdType.PROD_TYPE_ACTIVE.value());
            types.add(ProdType.PROD_TYPE_SCORE.value());
            productParam.setMustNotProdTypes(types);
        }

        // 如果不是搜索指定商品，就只查询可以在用户端显示的商品
        if (CollUtil.isEmpty(productParam.getProdIds())) {
            productParam.setAppDisplay(Boolean.TRUE);
        }

        EsPageVO<ProductSearchVO> searchPage =  this.adminPage(pageParam, productParam);
        long currentTime = System.currentTimeMillis();

        for (ProductSearchVO record : searchPage.getRecords()) {
            // 不是秒杀或者团购商品,或者活动还没开始的商品，就不用处理活动价格
            boolean notHandleActivityPrice = (!Objects.equals(record.getProdType(), ProdType.PROD_TYPE_GROUP.value()) &&
                    !Objects.equals(record.getProdType(), ProdType.PROD_TYPE_SECKILL.value())) ||
                    Objects.isNull(record.getActivityStartTime()) ||
                    record.getActivityStartTime() > currentTime;
            if (notHandleActivityPrice) {
                continue;
            }
            record.setOriPrice(record.getPrice());
            record.setPrice(record.getActivityPrice());
            record.setActivityPrice(null);
            record.setActivityOriginalPrice(null);
        }
        return searchPage;
    }

    @Override
    public void export(HttpServletResponse response, EsProductParam productParam) {
        productParam.setFetchSource(EsConstant.EXCEL_FETCH_SOURCE);
        productParam.setAppDisplay(Boolean.FALSE);
        ExcelWriter writer = productExcelService.getProdExcelWriter(productParam.getShopId());
        try (Workbook workbook = writer.getWorkbook()) {
            Sheet sheet = writer.getSheet();
            // excel下拉数据列表组装
            productExcelService.dropDownList(productParam.getShopId(), sheet, workbook);
            writerProdToExcel(writer, productParam);
            PoiExcelUtil.writeExcel(response, writer);
        } catch (Exception e) {
            log.error("Exception:", e);
        }
    }

    /**
     * 写入导出的商品数据
     * 使用es的scroll方法来滚动查询es的数据，可以有效的解决大数据容量读取的限制
     * @param writer
     * @param param
     */
    private void writerProdToExcel(ExcelWriter writer, EsProductParam param) {

        int row = 1;
        //设置查询超时时间
        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(5L));

        Integer lang = I18nMessage.getDbLang();

        SearchRequest searchRequest = buildScrollSearchRequest(param, scroll, lang);
        // 进行第一次滚动查询
        SearchResponse searchResponse = EsSearchUtil.search(searchRequest);

        // 将商品数据写入excel
        List<ProductExportParam> prodList = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            prodList.add(JSON.parseObject(hit.getSourceAsString(), ProductExportParam.class));
        }
        row = productExcelService.writerProdToExcel(prodList, writer, param.getShopId(), row);

        SearchHits hits= searchResponse.getHits();
        /**
         *在这个位置已经读到了前一千条数据，可以在这先对这一千数据进行处理。下面滚动查询剩下的数据
         */
        //记录要滚动的ID
        String scrollId = searchResponse.getScrollId();
        //滚动查询部分，将从第1001笔数据开始取
        SearchHit[] hitsScroll = hits.getHits();
        while (hitsScroll != null && hitsScroll.length > 0 ) {
            //构造滚动查询条件
            SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
            searchScrollRequest.scroll(scroll);
            searchResponse = EsSearchUtil.scroll(searchScrollRequest);
            scrollId = searchResponse.getScrollId();
            hits = searchResponse.getHits();
            hitsScroll = hits.getHits();

            // 将商品数据写入excel
            prodList.clear();
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                prodList.add(JSON.parseObject(hit.getSourceAsString(), ProductExportParam.class));
            }
            // 将查询的商品数据插入excel中
            row = productExcelService.writerProdToExcel(prodList, writer, param.getShopId(), row);
        }
        //清除滚动，否则影响下次查询
        EsSearchUtil.clearScroll(scrollId);
    }




    /**
     * 构建搜索结果数据
     * @param pageParam
     * @param response
     * @return
     */
    private static EsPageVO<EsProductSearchVO> buildSearchResult(EsPageParam pageParam, SearchResponse response) {
        EsPageVO<EsProductSearchVO> esPageVO = new EsPageVO<>();

        // 1、返回的所有查询到的商品
        List<EsProductSearchVO> productSearchs = new ArrayList<>();
        productSearchs.add(SearchResponseUtil.getProductSearch(response));
        esPageVO.setRecords(productSearchs);

        // 2、分页信息
        SearchResponseUtil.buildSearchPage(pageParam, esPageVO, response);
        return esPageVO;
    }

    /**
     * 通过搜索信息分页搜索es数据的信息
     * @param pageParam 分页数据
     * @param productParam 商品搜索条件
     * @param isAgg true:聚合搜索  false:非聚合搜索  null:非聚合搜索
     * @return 搜索结果
     */
    private SearchResponse pageSearchResult(EsPageParam pageParam, EsProductParam productParam, Boolean isAgg) {
        //1、准备检索请求
        SearchRequest searchRequest = buildSearchRequest(pageParam, productParam, isAgg);
        return EsSearchUtil.search(searchRequest);
    }

    /**
     * 准备滚动检索请求
     *
     * @param param 搜索参数
     * @param lang
     * @return
     */
    private SearchRequest buildScrollSearchRequest(EsProductParam param, Scroll scroll, Integer lang) {
        // 构建bool-query
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 指定返回数组中的字段
        if (ArrayUtil.isNotEmpty(param.getFetchSource())) {
            searchSourceBuilder.fetchSource(param.getFetchSource(), null);
        }

        // 过滤
        filterQueryIfNecessary(param, boolQueryBuilder);

        // 关键字搜索
        keywordSearch(param, boolQueryBuilder, lang);

        // 排序
        sort(param, searchSourceBuilder, boolQueryBuilder, lang);

        //设置最多一次能够取出1000笔数据，从第1001笔数据开始，将开启滚动查询
        //PS:滚动查询也属于这一次查询，只不过因为一次查不完，分多次查
        searchSourceBuilder.size(1000);


        log.debug("构建的DSL语句 {}",searchSourceBuilder.toString());

        SearchRequest searchRequest = new SearchRequest(new String[]{EsIndexEnum.PRODUCT.value()}, searchSourceBuilder);
        //将滚动放入
        searchRequest.scroll(scroll);
        return searchRequest;
    }

    /**
     * 准备检索请求
     * @param pageParam 分页参数
     * @param param 搜索参数
     * @param isAgg true:聚合搜索  false:非聚合搜索  null:非聚合搜索
     * @return
     */
    private SearchRequest buildSearchRequest(EsPageParam pageParam,EsProductParam param, Boolean isAgg) {
        if (Objects.isNull(param.getAppDisplay())) {
            param.setAppDisplay(Boolean.FALSE);
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        Integer lang = I18nMessage.getLang();

        // 指定返回数组中的字段
        if (ArrayUtil.isNotEmpty(param.getFetchSource())) {
            searchSourceBuilder.fetchSource(param.getFetchSource(), null);
        } else {
            searchSourceBuilder.fetchSource(EsConstant.APP_FETCH_SOURCE, null);
        }

        // 构建bool-query
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        // 过滤
        filterQueryIfNecessary(param, boolQueryBuilder);

        // 关键字搜索
        keywordSearch(param, boolQueryBuilder, lang);

        // 排序
        sort(param, searchSourceBuilder, boolQueryBuilder, lang);

        //分页
        if (Objects.nonNull(pageParam)) {
            if (pageParam.getCurrent() <= 0) {
                pageParam.setCurrent(1);
            }
            searchSourceBuilder.from((pageParam.getCurrent()-1)*pageParam.getSize());
            searchSourceBuilder.size(pageParam.getSize());
        }

        // 进行聚合分析
        agg(param, searchSourceBuilder, isAgg);

        log.debug("构建的DSL语句 {}",searchSourceBuilder.toString());

        return new SearchRequest(new String[]{EsIndexEnum.PRODUCT.value()},searchSourceBuilder);
    }

    /**
     * 聚合分析
     */
    private void agg(EsProductParam param, SearchSourceBuilder searchSourceBuilder, Boolean isAgg) {
        // 店铺进行聚合
        if (param.getKeyword() != null && param.getKeyword().length() > 0) {
            searchSourceBuilder.aggregation(AggregationBuilders.terms(EsConstant.SHOP).field(EsConstant.SHOP_ID).size(1));
        }
        if (Objects.isNull(isAgg) || !isAgg) {
            return;
        }
        // 品牌聚合
        searchSourceBuilder.aggregation(EsSearchUtil.nestedAggregation(EsConstant.BRAND, EsConstant.BRAND_UNION_ID, EsConstant.BRAND_ID, EsConstant.BRAND_INCLUDE));

        // 搜索平台商品，按照平台分类信息进行聚合
        if (Objects.isNull(param.getShopId()) && Objects.isNull(param.getCategoryId())) {
            searchSourceBuilder.aggregation(EsSearchUtil.nestedAggregation(EsConstant.CATEGORY, EsConstant.CATEGORY_UNION_ID, EsConstant.CATEGORY_ID, EsConstant.CATEGORY_INCLUDE));
        }
    }

    /**
     * 关键字搜索
     */
    private void keywordSearch(EsProductParam param, BoolQueryBuilder boolQueryBuilder, Integer lang) {
        if (StrUtil.isBlank(param.getKeyword())) {
            return;
        }
        // 创建查询语句 ES中must和should不能同时使用 同时使用should失效 嵌套多个must 将should条件拼接在一个must中即可
        BoolQueryBuilder keywordShouldQuery = QueryBuilders.boolQuery();
        // 提升商品名称搜索的权重
        if (Objects.equals(lang, LanguageEnum.LANGUAGE_ZH_CN.getLang())) {
            keywordShouldQuery.should(QueryBuilders.matchQuery(EsConstant.PROD_NAME_ZH, param.getKeyword()).boost(10));
        } else {
            keywordShouldQuery.should(QueryBuilders.matchQuery(EsConstant.PROD_NAME_EN, param.getKeyword()).boost(10));
        }


        if (param.getKeyword().length()>1) {
            // 卖点，不分词
            if (Objects.equals(lang, LanguageEnum.LANGUAGE_ZH_CN.getLang())) {
                keywordShouldQuery.should(QueryBuilders.matchPhraseQuery(EsConstant.BRIEF_ZH, param.getKeyword()).boost(2));
            } else {
                keywordShouldQuery.should(QueryBuilders.matchPhraseQuery(EsConstant.BRIEF_EN, param.getKeyword()).boost(2));
            }
            // 店铺名，不分词
            keywordShouldQuery.should(QueryBuilders.matchPhraseQuery(EsConstant.SHOP_NAME, param.getKeyword()));
        }
        boolQueryBuilder.must(keywordShouldQuery);
    }

    /**
     * 进行排序
     */
    private void sort(EsProductParam param, SearchSourceBuilder searchSourceBuilder, BoolQueryBuilder boolQueryBuilder, Integer lang) {
        // 用户端、商家端、平台端自定义排序
        if (Objects.nonNull(param.getSort())) {
            for (EsProductSortEnum enumValue : EsProductSortEnum.values()) {
                if (!Objects.equals(enumValue.value(), param.getSort())) {
                    continue;
                }
                searchSourceBuilder.sort(enumValue.param(), enumValue.order());
            }
            //封装所有的查询条件(没有function score)
            searchSourceBuilder.query(boolQueryBuilder);
            return;
        }

        // 1.关键字排序 -- 没有指定排序规则，且是关键字搜索的查询，统一按关键字优先排序（防止关键字搜素时，关键字的商品没有优先显示）
        // 2.用户端默认排序 -- 如果排序规则设为空，则按照一定的算分规则进行排序，否则按照用户指定排序规则进行排序()
        if (StrUtil.isNotBlank(param.getKeyword()) || param.getAppDisplay()) {
            keywordSort(param, searchSourceBuilder, boolQueryBuilder, lang);
            return;
        }

        // 商家、平台默认排序--商品序号 倒序， 创建时间 倒序
        if (Objects.nonNull(param.getShopId())) {
            // 商家端优先显示序号大的商品
            searchSourceBuilder.sort(EsProductSortEnum.SEQ_DESC.param(), EsProductSortEnum.SEQ_DESC.order());
        } else {
            // 平台端优先置顶的商品, 再到销量（实际销量+注水销量）
            searchSourceBuilder.sort(EsProductSortEnum.IS_TOP_DESC.param(), EsProductSortEnum.IS_TOP_DESC.order());
            searchSourceBuilder.sort(EsProductSortEnum.SALE_NUM_DESC.param(), EsProductSortEnum.SALE_NUM_DESC.order());
        }
        searchSourceBuilder.sort(EsProductSortEnum.CREATE_TIME_DESC.param(), EsProductSortEnum.CREATE_TIME_DESC.order());
        searchSourceBuilder.query(boolQueryBuilder);
    }


    /**
     * 关键字搜索排序
     * @param param
     * @param searchSourceBuilder
     * @param boolQueryBuilder
     * @param lang
     */
    private void keywordSort(EsProductParam param, SearchSourceBuilder searchSourceBuilder, BoolQueryBuilder boolQueryBuilder, Integer lang) {
        List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
        // 关键字搜索，优先匹配
        if (StrUtil.isNotBlank(param.getKeyword())) {
            FunctionScoreQueryBuilder.FilterFunctionBuilder spuName;
            // 权重调大，防止销量大的商品排在关键词商品的前面
            if (Objects.equals(lang, LanguageEnum.LANGUAGE_ZH_CN.getLang())) {
                spuName = new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery(EsConstant.PROD_NAME_ZH, param.getKeyword()), ScoreFunctionBuilders.weightFactorFunction(200));
            } else {
                spuName = new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery(EsConstant.PROD_NAME_EN, param.getKeyword()), ScoreFunctionBuilders.weightFactorFunction(200));
            }
            filterFunctionBuilders.add(spuName);
        }

        // 用户端默认排序优先使用是否置顶参数
        FunctionScoreQueryBuilder.FilterFunctionBuilder isTop = new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery(EsConstant.IS_TOP, 50), ScoreFunctionBuilders.weightFactorFunction(1));
        filterFunctionBuilders.add(isTop);

        // 评论数 log1p
        ScoreFunctionBuilder<FieldValueFactorFunctionBuilder> commentNumScoreFunction = new FieldValueFactorFunctionBuilder(EsConstant.COMMENT_NUM).modifier(FieldValueFactorFunction.Modifier.LOG1P).factor(0.5f);
        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(commentNumScoreFunction));
        // 销量数 log1p
        ScoreFunctionBuilder<FieldValueFactorFunctionBuilder> saleNumScoreFunction = new FieldValueFactorFunctionBuilder(EsConstant.SOLD_NUM).modifier(FieldValueFactorFunction.Modifier.LOG1P).factor(0.5f);
        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(saleNumScoreFunction));

        filterFunctionBuilders.toArray();

        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder, ArrayUtil.toArray(filterFunctionBuilders, FunctionScoreQueryBuilder.FilterFunctionBuilder.class))
                .scoreMode(FunctionScoreQuery.ScoreMode.SUM).boostMode(CombineFunction.SUM);
        // 封装所有的查询条件(带有function score)
        searchSourceBuilder.query(functionScoreQueryBuilder);
    }

    /**
     * 过滤查询条件，如果有必要的话
     * @param param 查询条件
     * @param boolQueryBuilder 组合进boolQueryBuilder
     */
    private void filterQueryIfNecessary(EsProductParam param, BoolQueryBuilder boolQueryBuilder) {

        // 用户端搜索
        if(param.getAppDisplay()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(EsConstant.APP_DISPLAY, param.getAppDisplay()));
        }
        // 商品（状态、库存、商品类型、商品id、商品id列表, 组合商品,配送方式,是否置顶）
        spuFilterQuery(param, boolQueryBuilder);

        // 分类（商家分类，平台一二三级分类）
        categoryFilterQuery(param, boolQueryBuilder);

        // 活动 （活动商品id）
        activityFilterQuery(param, boolQueryBuilder);

        // 商品扩展信息筛选（店铺id、店铺类型、店铺名称、品牌）
        extensionFilterQuery(param, boolQueryBuilder);

        // 范围筛选（价格、销量）
        rangeFilterQuery(param, boolQueryBuilder);
    }

    /**
     * 范围过滤
     * @param param 查询条件
     * @param boolQueryBuilder
     */
    private void rangeFilterQuery(EsProductParam param, BoolQueryBuilder boolQueryBuilder) {
        // 价格区间
        if(param.getMinPrice() != null || param.getMaxPrice() != null){
            boolQueryBuilder.filter(EsSearchUtil.rangeQuery(EsConstant.PRICE,param.getMinPrice(), param.getMaxPrice()));
        }
        // 销量区间
        if(param.getMinSaleNum() != null || param.getMaxSaleNum() != null){
            boolQueryBuilder.filter(EsSearchUtil.rangeQuery(param.getAppDisplay() ? EsConstant.SOLD_NUM : EsConstant.ACTUAL_SOLD_NUM,
                    param.getMinSaleNum(), param.getMaxSaleNum()));
        }
    }

    /**
     * 商品扩展信息过滤
     * @param param 查询条件
     * @param boolQueryBuilder
     */
    private void extensionFilterQuery(EsProductParam param, BoolQueryBuilder boolQueryBuilder) {
        // 店铺id
        if(Objects.nonNull(param.getShopId())){
            boolQueryBuilder.filter(QueryBuilders.termQuery(EsConstant.SHOP_ID, param.getShopId()));
        }

        // 店铺类型
        if(Objects.nonNull(param.getShopType())){
            boolQueryBuilder.filter(QueryBuilders.termQuery(EsConstant.SHOP_TYPE, param.getShopType()));
        }

        // 店铺名称
        if(StrUtil.isNotBlank(param.getShopName())){
            BoolQueryBuilder keywordShouldQuery = QueryBuilders.boolQuery()
                    .should(QueryBuilders.matchQuery(EsConstant.SHOP_NAME, param.getShopName()));
            boolQueryBuilder.must(keywordShouldQuery);
        }


        // 品牌
        if(StrUtil.isNotBlank(param.getBrandIds())){
            boolQueryBuilder.filter(EsSearchUtil.nestedQueryByArray(EsConstant.BRAND, EsConstant.BRAND_UNION_ID, param.getBrandIds()));
        }

    }

    /**
     * 商品活动信息过滤
     * @param param 查询条件
     * @param boolQueryBuilder
     */
    private void activityFilterQuery(EsProductParam param, BoolQueryBuilder boolQueryBuilder) {
        // 商品活动Id
        if(Objects.nonNull(param.getActivityId())){
            boolQueryBuilder.filter(QueryBuilders.termQuery(EsConstant.ACTIVITY_ID, param.getActivityId()));
            boolQueryBuilder.filter(QueryBuilders.termQuery(EsConstant.PROD_TYPE, param.getProdType()));
        }
        if (Objects.equals(param.getProdType(), ProdType.PROD_TYPE_GROUP.value()) && param.getAppDisplay()){
            boolQueryBuilder.filter(EsSearchUtil.rangeQuery(EsConstant.ACTIVITY_START_TIME, null, System.currentTimeMillis()));
        } else if (Objects.equals(param.getProdType(), ProdType.PROD_TYPE_SECKILL.value()) && param.getAppDisplay()){
            boolQueryBuilder.must(QueryBuilders.existsQuery(EsConstant.ACTIVITY_START_TIME));
        }
    }

    /**
     * 商品信息过滤
     * @param param 查询条件
     * @param boolQueryBuilder
     */
    private void spuFilterQuery(EsProductParam param, BoolQueryBuilder boolQueryBuilder) {
        // spu状态
        List<Integer> statusList = new ArrayList<>();
        if (Objects.nonNull(param.getStatus())) {
            statusList.add(param.getStatus());
        }
        // 装修以及定时任务获取删除商品
        else if (!BooleanUtil.isTrue(param.getGetDelete())) {
            statusList.add(StatusEnum.ENABLE.value());
            statusList.add(StatusEnum.DISABLE.value());
            statusList.add(StatusEnum.OFFLINE.value());
            statusList.add(StatusEnum.WAIT_AUDIT.value());
            statusList.add(ProdStatusEnums.AUDIT.getValue());
        }
        if(CollUtil.isNotEmpty(statusList)) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery(EsConstant.STATUS, statusList));
        }

        // 是否有库存
        if(Objects.nonNull(param.getHasStock())){
            boolQueryBuilder.filter(QueryBuilders.termQuery(EsConstant.HAS_STOCK, BooleanUtil.isTrue(param.getHasStock())));
        }
        // 商品类型
        if(Objects.nonNull(param.getProdType())){
            boolQueryBuilder.filter(QueryBuilders.termQuery(EsConstant.PROD_TYPE, param.getProdType()));
        }
        // 不匹配的商品类型
        if(CollUtil.isNotEmpty(param.getMustNotProdTypes())){
            for (Integer prodType : param.getMustNotProdTypes()) {
                boolQueryBuilder.mustNot(QueryBuilders.termQuery(EsConstant.PROD_TYPE, prodType.toString()));
            }
        }

        // 商品类别
        if(Objects.nonNull(param.getMold())){
            boolQueryBuilder.filter(QueryBuilders.termQuery(EsConstant.MOLD, param.getMold()));
        }
        // spuId
        if(Objects.nonNull(param.getProdId())){
            boolQueryBuilder.filter(QueryBuilders.termsQuery(EsConstant.PROD_ID,param.getProdId().toString()));
        }
        // spuId列表
        else if(CollectionUtil.isNotEmpty(param.getProdIds())){
            boolQueryBuilder.filter(QueryBuilders.termsQuery(EsConstant.PROD_ID,param.getProdIds()));
        }
        // 查询不在该集合中的商品
        if(Objects.nonNull(param.getSpuIdsExclude())){
            boolQueryBuilder.mustNot(QueryBuilders.termsQuery(EsConstant.PROD_ID,param.getSpuIdsExclude()));
        }
        // 配送方式
        if(Objects.nonNull(param.getDeliveryMode())){
            boolQueryBuilder.filter(QueryBuilders.termsQuery(EsConstant.DELIVERIES,param.getDeliveryMode().toString()));
        }
        // 是否置顶
        if(Objects.nonNull(param.getIsTop())){
            boolQueryBuilder.filter(QueryBuilders.termsQuery(EsConstant.IS_TOP,param.getIsTop().toString()));
        }
    }

    /**
     * 商品分类信息过滤
     * @param param
     * @param boolQueryBuilder
     */
    private void categoryFilterQuery(EsProductParam param, BoolQueryBuilder boolQueryBuilder) {
        //商家分类
        if(Objects.nonNull(param.getShopCategoryId())){
            boolQueryBuilder.filter(QueryBuilders.termQuery(EsConstant.SHOP_CATEGORY_ID, param.getShopCategoryId()));
        }

        // 平台一级分类
        if(Objects.nonNull(param.getPrimaryCategoryId())){
            boolQueryBuilder.filter(QueryBuilders.termQuery(EsConstant.PRIMARY_CATEGORY_ID, param.getPrimaryCategoryId()));
        }

        // 查询不在该集合中的商品
        if(Objects.nonNull(param.getNotPrimaryCategoryId())) {
            boolQueryBuilder.mustNot(QueryBuilders.termsQuery(EsConstant.PRIMARY_CATEGORY_ID, param.getNotPrimaryCategoryId().toString()));
        }
        
        // 平台二级分类
        if(Objects.nonNull(param.getSecondaryCategoryId())){
            boolQueryBuilder.filter(QueryBuilders.termQuery(EsConstant.SECONDARY_CATEGORY_ID, param.getSecondaryCategoryId()));
        }

        // 平台三级分类
        if(Objects.nonNull(param.getCategoryId())){
            boolQueryBuilder.filter(EsSearchUtil.nestedQuery(EsConstant.CATEGORY, EsConstant.CATEGORY_UNION_ID, param.getCategoryId()));
        }
    }

}
