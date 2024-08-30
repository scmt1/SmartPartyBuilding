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

import com.yami.shop.bean.dto.FlowPageAnalysisDto;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2020-07-14 11:26:35
 */
@Data
public class PageAnalysisParam {
    /**
     * 系统类型 1:pc  2:h5  3:小程序 4:安卓  5:ios
     */
    private Integer systemType;
    /**
     *  页面统计数据 key:页面编号
     */
    private Map<Integer,FlowPageAnalysisDto> pageAnalysisMap = new HashMap<>();
    /**
     *  商品详情页面统计数据 Map<商品类型, Map<商品id, FlowPageAnalysisDto>>
     */
    private Map<Integer,Map<String, FlowPageAnalysisDto>> prodPageAnalysisMap = new HashMap<>();
}
