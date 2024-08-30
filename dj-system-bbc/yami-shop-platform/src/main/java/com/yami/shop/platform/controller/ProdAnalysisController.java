/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.param.*;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商品分析接口
 * @author
 */
@Api(tags = "商品分析接口")
@RestController
@RequestMapping("/platform/prodAnalysis")
@AllArgsConstructor
public class ProdAnalysisController {

    private final ProductService productService;
    private final ProductExcelService productExcelService;
    private final FlowProductAnalysisService flowProductAnalysisService;


    /**
     * 获取商品概况
     */
    @GetMapping("/getProdSurvey")
    @ApiOperation(value = "获取商品概况", notes = "获取商品概况")
    public ServerResponseEntity<ProdAnalysisParam> getProdSurvey(ProdAnalysisSurveyParam param) {
        ProdAnalysisParam analysis = flowProductAnalysisService.getProdSurvey(param);
        return ServerResponseEntity.success(analysis);
    }
    /**
     * 导出商品概况
     */
    @GetMapping("/prodSurveyExport")
    @ApiOperation(value = "导出商品概况", notes = "导出商品概况")
    public void prodSurveyExport(HttpServletResponse response, ProdAnalysisSurveyParam param) {
        flowProductAnalysisService.prodSurveyExport(response, param);
    }

    /**
     * 获取商品趋势分析
     */
    @GetMapping("/getProdTrendAnalysis")
    @ApiOperation(value = "获取商品趋势分析", notes = "获取商品趋势分析")
    public ServerResponseEntity<List<ProdAnalysisDataParam>> getProdTrendAnalysis(ProdAnalysisSurveyParam param) {
        List<ProdAnalysisDataParam> trendAnalysisa = productService.getProdTrendAnalysis(param);
        return ServerResponseEntity.success(trendAnalysisa);
    }

    /**
     * 支付金额TOP
     * 访客数TOP
     */
    @GetMapping("/getPayAmountTop")
    @ApiOperation(value = "支付金额TOP、访客数TOP", notes = "支付金额TOP、访客数TOP")
    public ServerResponseEntity<VisitorAndPayTopParam> getPayAmountTop(PageParam<OrderItem> page, ProdAnalysisSurveyParam param) {
        param.setShopId(null);
        VisitorAndPayTopParam visitorAndPayTopParam = flowProductAnalysisService.getPayAmountTop(page, param);
        return ServerResponseEntity.success(visitorAndPayTopParam);
    }

    /**
     * 导出支付金额TOP
     * 导出访客数TOP
     */
    @GetMapping("/payAmountTopExport")
    @PreAuthorize("@pms.hasPermission('flow:pay:visit:top')")
    @ApiOperation(value = "导出支付金额TOP、访客数TOP", notes = "导出支付金额TOP、访客数TOP")
    public void payAmountTopExport(HttpServletResponse response, ProdAnalysisSurveyParam param) {
        param.setShopId(null);
        flowProductAnalysisService.payAmountTopExport(response, param);
    }


    /**
     * 获取商品效果分析数据
     */
    @GetMapping("/getProdEffect")
    @ApiOperation(value = "获取商品效果分析数据", notes = "获取商品效果分析数据")
    public ServerResponseEntity<IPage<ProdEffectRespParam>> getProdEffect(PageParam<Product> page,ProdEffectParam param) {
        if (Objects.equals(1,param.getGroup())) {
            IPage<ProdEffectRespParam> map = new PageParam<>();
            map.setCurrent(page.getCurrent());
            map.setSize(page.getSize());
            map.setPages(page.getPages());
            map.setRecords(new ArrayList<>());
            return ServerResponseEntity.success(map);
        }
        IPage<ProdEffectRespParam> resPage = productService.pageProdEffect(page,param,I18nMessage.getDbLang(), false);
        return ServerResponseEntity.success(resPage);
    }


    /**
     * 导出商品洞察数据
     */
    @GetMapping("/prodEffectExport")
    @PreAuthorize("@pms.hasPermission('flow:prod:effect')")
    @ApiOperation(value = "导出商品洞察数据", notes = "导出商品洞察数据")
    public void prodEffectExport(HttpServletResponse response, ProdEffectParam param) {
        productExcelService.prodEffectExport(response,param,I18nMessage.getDbLang());
    }

    /**
     * 单个商品的趋势分析
     */
    @GetMapping("/getSingleProdTrend")
    @ApiOperation(value = "获取商品效果分析数据", notes = "获取商品效果分析数据")
    public ServerResponseEntity<List<ProdSingleTrendParam>> getSingleProdTrend(Long prodId,ProdEffectParam param) {
        List<ProdSingleTrendParam> resList = productService.getSingleProdTrend(prodId,param);
        return ServerResponseEntity.success(resList);
    }

//    /**
//     * 卡券分析，卡券昨日关键指标
//     */
//    @ApiOperation(value = "卡券分析，卡券昨日关键指标", notes = "卡券分析，卡券昨日关键指标")
//    @GetMapping("/getCouponAnalysis")
//    public ServerResponseEntity<List<CouponAnalysisParam>> getCouponAnalysis(ProdEffectParam param) {
//        // 统计近7天的数据
//        List<CouponAnalysisParam> resList = couponUserService.getCouponAnalysis(param);
//        return ServerResponseEntity.success(resList);
//    }
//
//    @GetMapping("/getCouponAnalysisParamByDate")
//    @ApiOperation(value = "根据日期获得优惠券详情信息", notes = "根据日期获得优惠券详情信息")
//    public ServerResponseEntity<IPage<CouponAnalysisParam>> getCouponAnalysisParamByDate(PageParam<CouponAnalysisParam> page,ProdEffectParam param){
//        return ServerResponseEntity.success(couponUserService.getCouponAnalysisParamByDate(page,param));
//    }
}
