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

import com.yami.shop.bean.dto.FlowRouteAnalysisDto;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 流量分析—用户路径数据
 *
 * @author YXF
 * @date 2020-07-14 11:26:35
 */
@Data
public class RouteAnalysisParam {

    private Integer step;

    private Integer pageId;

    /**
     * 系统类型 1:pc  2:h5  3:小程序 4:安卓  5:ios
     */
    private Integer systemType;

    /**
     * 跳转下个页面的数据
     */
    private Map<Integer, RouteAnalysisParam> flowRouteAnalysisParamMap = new HashMap<>();
    /**
     * 跳转下个页面的数据
     */
    private Map<Integer, FlowRouteAnalysisDto> flowRouteAnalysisDtoMap = new HashMap<>();

}
