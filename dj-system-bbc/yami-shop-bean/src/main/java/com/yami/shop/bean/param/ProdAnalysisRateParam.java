/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import lombok.Data;

/**
 * 商品分析百分比数据
 */
/**
 * @author Yami
 */
@Data
public class ProdAnalysisRateParam {
    /**
     * 新增商品数
     */
    private Double newProdRate = 0.00;
    /**
     * 被访问商品数
     */
    private Double visitedProdRate = 0.00;
    /**
     * 动销商品数
     */
    private Double dynamicSaleRate = 0.00;
    /**
     * 商品曝光数
     */
    private Double exposeRate = 0.00;
    /**
     * 商品浏览量
     */
    private Double browseRate = 0.00;
    /**
     * 商品访客数
     */
    private Double visitorRate = 0.00;
    /**
     * 加购件数
     */
    private Double addCartRate = 0.00;
    /**
     * 下单件数
     */
    private Double orderNumRate = 0.00;
    /**
     * 支付件数
     */
    private Double payNumRate = 0.00;
    /**
     * 分享访问数
     */
    private Double shareVisitRate = 0.00;
}
