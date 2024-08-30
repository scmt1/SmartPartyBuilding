/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.search.platform.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.param.EsProductParam;
import com.yami.shop.bean.vo.search.ProductSearchVO;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.dao.ProductMapper;
import com.yami.shop.search.common.constant.EsConstant;
import com.yami.shop.search.common.constant.EsIndexEnum;
import com.yami.shop.search.common.param.EsPageParam;
import com.yami.shop.search.common.service.EsProductService;
import com.yami.shop.search.common.service.SearchProductService;
import com.yami.shop.search.common.util.EsSearchUtil;
import com.yami.shop.search.common.vo.EsPageVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品定时任务
 *
 * @author FrozenWatermelon
 */
@Component
@Slf4j
public class SearchTask {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private EsProductService esProductService;
    @Autowired
    private SearchProductService searchProductService;

    /**
     * 校验商品数量是否完整
     * 商品数据是否为最新的：根据商品更新时间判断
     */
    @XxlJob("verifySpuData")
    public void verifySpuData(){
        Date currentTime = new Date();
        // 数量相同，不需要更新
        if (verifySpuNum() == 0) {
            return;
        }

        //先更新近3个小时的数据
        DateTime beginTime = DateUtil.beginOfHour(DateUtil.offsetHour(currentTime, -3));
        updateDbDate(beginTime);
        //更新后校验
        if (verifySpuNum() == 0) {
            return;
        }

        // 更新数据库中的所有商品数据， 保证数据库的数据都更新到es
        updateDbDate(null);

        //如果es中的商品数量大于mysql中的商品数量-es有部分已失效的商品数据需要删除
        if (verifySpuNum() < 0) {
            // 滚动查询es中的数据，删除已失效的
            updateEsDateByScroll();
        }

    }


    private void updateDbDate(Date beginTime){
        PageParam page = new PageParam();
        page.setCurrent(1);
        page.setSize(Constant.MAX_PAGE_SIZE);
        List<Long> updateList = new ArrayList<>();
        boolean update = true;
        while (update) {
            PageParam<Product> prodPage = productMapper.selectPage(page, new LambdaQueryWrapper<Product>()
//                    .ne(Product::getStatus, -1)
                    .gt(Objects.nonNull(beginTime), Product::getUpdateTime, beginTime)
            );
            if (Objects.isNull(prodPage) || Objects.isNull(prodPage.getTotal()) || prodPage.getTotal() <= page.getCurrent()){
                update = false;
            }
            List<Product> records = prodPage.getRecords();
            if (CollUtil.isEmpty(records)) {
                break;
            }
            List<Long> spuIds = new ArrayList<>();
            for (Product product : records) {
                spuIds.add(product.getProdId());
            }
            List<ProductSearchVO> productSearchVOList = searchProductService.simpleList(spuIds);
            Map<Long, Long> prodMap = productSearchVOList.stream()
                    .filter(productSearch -> Objects.nonNull(productSearch.getUpdateTime()))
                    .collect(Collectors.toMap(ProductSearchVO::getProdId, ProductSearchVO::getUpdateTime)
            );
            for (Product product : records) {
                boolean isSame = prodMap.containsKey(product.getProdId()) && Objects.equals(product.getUpdateTime().getTime(), prodMap.get(product.getProdId()));
                if (isSame) {
                    continue;
                }
                updateList.add(product.getProdId());
            }
            if (updateList.size() > Constant.MAX_DATA_HANDLE_NUM) {
                esProductService.updateBatch(updateList);
                updateList.clear();
            }
            page.setCurrent(page.getCurrent() + 1);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            esProductService.updateBatch(updateList);
        }
    }

    private void updateEsDateByScroll() {
        // 设置超时时间
        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(5L));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 指定返回数组中的字段
        searchSourceBuilder.fetchSource(new String[]{EsConstant.PROD_ID}, null);

        // 定时任务使用的所以不会有任何条件查询，只是要查出会不会存在其他环境的商品将其删除
        // 设置最多一次能够取出1000笔数据，从第1001笔数据开始，将开启滚动查询
        // PS:滚动查询也属于这一次查询，只不过因为一次查不完，分多次查
        searchSourceBuilder.size(100);

        log.debug("构建的DSL语句 {}", searchSourceBuilder);

        SearchRequest searchRequest = new SearchRequest(new String[]{EsIndexEnum.PRODUCT.value()}, searchSourceBuilder);
        // 将滚动放入
        searchRequest.scroll(scroll);

        // 进行第一次滚动查询
        SearchResponse searchResponse = EsSearchUtil.search(searchRequest);

        // 将商品id写入list
        List<Long> prodIds = new ArrayList<>();

        for (SearchHit hit : searchResponse.getHits().getHits()) {
            prodIds.add(Long.parseLong(JSON.parseObject(hit.getSourceAsString()).get("prodId").toString()));
        }
        // 删除不存在商品
        checkAndDelete(prodIds);

        SearchHits hits = searchResponse.getHits();
        // 记录要滚动的ID
        String scrollId = searchResponse.getScrollId();
        // 滚动查询从第1001笔数据开始取
        SearchHit[] hitsScroll = hits.getHits();
        while (hitsScroll != null && hitsScroll.length > 0) {
            //构造滚动查询条件
            SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
            searchScrollRequest.scroll(scroll);
            searchResponse = EsSearchUtil.scroll(searchScrollRequest);
            scrollId = searchResponse.getScrollId();
            hits = searchResponse.getHits();
            hitsScroll = hits.getHits();

            prodIds.clear();
            // 写入id
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                prodIds.add(Long.parseLong(JSON.parseObject(hit.getSourceAsString()).get("prodId").toString()));
            }
            checkAndDelete(prodIds);
        }
        //清除滚动，否则影响下次查询
        EsSearchUtil.clearScroll(scrollId);
    }

    private void checkAndDelete(List<Long> prodIds) {
        if (CollUtil.isEmpty(prodIds)) {
            return;
        }

        List<Long> esProdIds = productMapper.verifySpuExist(prodIds);
        prodIds.removeAll(esProdIds);
        List<Long> deleteList = new ArrayList<>(prodIds);
        if (deleteList.size() > Constant.MAX_DATA_HANDLE_NUM) {
            esProductService.deleteBatch(deleteList);
            deleteList.clear();
        }
    }


    private void updateEsDate(){
        EsPageParam page = new EsPageParam();
        page.setCurrent(1);
        page.setSize(Constant.MAX_PAGE_SIZE);
        List<Long> deleteList = new ArrayList<>();
        boolean update = true;
        EsProductParam esProductParam = new EsProductParam();
        esProductParam.setFetchSource(new String[]{EsConstant.PROD_ID});
        while (update) {
            EsPageVO<ProductSearchVO> productPage = searchProductService.adminPage(page, esProductParam);
            if (Objects.isNull(productPage) || Objects.isNull(productPage.getPages()) || productPage.getPages() <= page.getCurrent()){
                update = false;
            }
            List<ProductSearchVO> records = productPage.getRecords();
            if (CollUtil.isEmpty(records)) {
                break;
            }
            List<Long> prodIds = records.stream().map(ProductSearchVO::getProdId).collect(Collectors.toList());
            //List<Long> esProdIds = productMapper.verifySpuHasDelete(prodIds);
            List<Long> esProdIds = productMapper.verifySpuExist(prodIds);
            prodIds.removeAll(esProdIds);
            deleteList.addAll(prodIds);
            if (deleteList.size() > Constant.MAX_DATA_HANDLE_NUM) {
                esProductService.deleteBatch(deleteList);
                deleteList.clear();
            }
            page.setCurrent(page.getCurrent() + 1);
        }
        if (CollUtil.isNotEmpty(deleteList)) {
            esProductService.deleteBatch(deleteList);
        }
    }


    /**
     * 校验mysql、es中的商品数据是否一致
     * @return 返回值大于0：数据库有部分数据没同步到es中， 返回值等于0：数据库和es中的商品数据一致， 返回值小于0：es有部分失效的数据未删除
     */
    private Long verifySpuNum() {
        // 查找删除的商品一起存进去
        Integer spuNum = productMapper.selectCount(new LambdaQueryWrapper<>());

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // es默认输出最多一万条,设置trackTotalHits为true，取消限制
        searchSourceBuilder.trackTotalHits(true);
        SearchRequest searchRequest = new SearchRequest(new String[]{EsIndexEnum.PRODUCT.value()}, searchSourceBuilder);
        SearchResponse searchResponse = EsSearchUtil.search(searchRequest);
        Long total = searchResponse.getHits().getTotalHits().value;
        // 数据库商品数量 - es商品数量
        return Long.valueOf(spuNum) - total;
    }
}
