/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.search.api.controller;

import cn.hutool.core.collection.CollUtil;
import com.yami.shop.bean.enums.ProdType;
import com.yami.shop.bean.event.EsProductActivityInfoEvent;
import com.yami.shop.bean.param.EsProductParam;
import com.yami.shop.bean.vo.search.EsProductSearchVO;
import com.yami.shop.bean.vo.search.ProductSearchVO;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.search.common.param.EsPageParam;
import com.yami.shop.search.common.service.SearchProductService;
import com.yami.shop.search.common.vo.EsPageVO;
import com.yami.shop.service.SkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 商品搜索
 * @author FrozenWatermelon
 * @date 2020/11/16
 */
@RestController("appSearchSpuController")
@RequestMapping("/search")
@Api(tags = "商品搜索接口")
public class ProductSearchController {

    @Autowired
    private SearchProductService searchProductService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private SkuService skuService;

    @GetMapping("/page")
    @ApiOperation(value = "商品搜索接口（仅商品信息）", notes = "商品搜索接口")
    public ServerResponseEntity<EsPageVO<EsProductSearchVO>> page(@Valid EsPageParam pageParam, EsProductParam productParam) {
        loadSearchProdType(productParam);
        EsPageVO<EsProductSearchVO> esProductSearchVOEsPageVO = simplePage(pageParam, productParam);
        return ServerResponseEntity.success(esProductSearchVOEsPageVO);
    }

    @GetMapping("/renovationPage")
    @ApiOperation(value = "商品信息列表(装修商品列表)", notes = "商品信息列表(装修商品列表)")
    public ServerResponseEntity<EsPageVO<ProductSearchVO>> renovationPage(@Valid EsPageParam pageParam, EsProductParam productParam) {
        EsPageVO<ProductSearchVO> searchPage = searchProductService.renovationPage(pageParam, productParam);
        return ServerResponseEntity.success(searchPage);
    }

    private EsPageVO<EsProductSearchVO> simplePage(EsPageParam pageDTO, EsProductParam productSearchDTO) {
        loadSearchProdType(productSearchDTO);
        EsPageVO<EsProductSearchVO> searchPage =  searchProductService.page(pageDTO, productSearchDTO, Boolean.FALSE);
        loadData(productSearchDTO, searchPage.getRecords());
        return searchPage;
    }

    /**
     * 处理搜索数据
     * @param productParam
     * @param list
     */
    private void loadData(EsProductParam productParam, List<EsProductSearchVO> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (EsProductSearchVO productSearchVO : list) {
            // 如果包含秒杀、团购活动商品，就插入活动信息
            if (containActivityProd(productSearchVO)) {
                eventPublisher.publishEvent(new EsProductActivityInfoEvent(productParam.getProdType(), productSearchVO.getProducts()));
            }
        }
    }

    private boolean containActivityProd(EsProductSearchVO productSearchVO) {
        if (CollUtil.isEmpty(productSearchVO.getProducts())) {
            return Boolean.FALSE;
        }
        long count = productSearchVO.getProducts().stream().filter(product -> Objects.equals(product.getProdType(), ProdType.PROD_TYPE_GROUP.value()) || Objects.equals(product.getProdType(), ProdType.PROD_TYPE_SECKILL.value())).count();
        return count > 0;
    }

    private void loadSearchProdType(EsProductParam productParam) {
        // 用户端没有搜索全部商品需求， 当不指定商品类型时默认为非积分商品和活动商品
        if (Objects.isNull(productParam.getProdType())) {
            List<Integer> prodTypeList = new ArrayList<>();
            prodTypeList.add(ProdType.PROD_TYPE_SCORE.value());
            prodTypeList.add(ProdType.PROD_TYPE_ACTIVE.value());
            productParam.setMustNotProdTypes(prodTypeList);
        }
    }
}
