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
import com.yami.shop.bean.vo.search.EsProductSearchVO;
import com.yami.shop.bean.vo.search.ProductSearchVO;
import com.yami.shop.search.common.param.EsPageParam;
import com.yami.shop.search.common.vo.EsPageVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Yami
 */
public interface SearchProductService {

    /**
     * 分页搜索
     * @param pageParam 分页参数
     * @param productParam 筛选参数
     * @param isAgg 是否需要聚合
     * @return 商品列表
     */
    EsPageVO<EsProductSearchVO> page(EsPageParam pageParam, EsProductParam productParam, Boolean isAgg);

    /**
     * 搜索商品扩展数据（分类、品牌等）
     * @param pageParam 分页参数
     * @param productParam 筛选参数
     * @return
     */
    EsProductSearchVO searchExtension(EsPageParam pageParam, EsProductParam productParam);

    /**
     * 根据商品id，获取商品列表
     * @param productParam 筛选参数
     * @return 商品信息列表
     */
    List<ProductSearchVO> listSpuByProdIds(EsProductParam productParam);

    /**
     * 商家、平台端商品管理分页查询
     * @param pageParam 分页参数
     * @param productParam 筛选参数
     * @return 商品信息列表
     */
    EsPageVO<ProductSearchVO> adminPage(EsPageParam pageParam, EsProductParam productParam);

    /**
     * 定时任务校验商品数据查询
     * @param spuIds 商品id列表
     * @return 商品信息列表
     */
    List<ProductSearchVO> simpleList(List<Long> spuIds);

    /**
     * 装修商品分页
     * @param pageParam
     * @param productParam
     * @return
     */
    EsPageVO<ProductSearchVO> renovationPage(EsPageParam pageParam, EsProductParam productParam);

    /**
     * 导出excel
     * @param response
     * @param productParam
     */
    void export(HttpServletResponse response, EsProductParam productParam);
}
