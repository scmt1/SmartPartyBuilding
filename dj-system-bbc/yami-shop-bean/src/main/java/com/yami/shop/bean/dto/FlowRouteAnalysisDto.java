/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto;

import com.yami.shop.bean.model.FlowRouteAnalysis;
import lombok.Data;

import java.util.*;

/**
 * 流量分析—用户路径数据
 *
 * @author YXF
 * @date 2020-07-14 11:26:35
 */
@Data
public class FlowRouteAnalysisDto{

    /**
     *
     */
    private Long flowRouteAnalysisId;
    /**
     * 步骤数
     */
    private Integer step;
    /**
     * 系统类型
     */
    private Integer systemType;
    /**
     * 页面编号
     */
    private Integer pageId;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 会话次数
     */
    private Set<String> sessionSet = new HashSet<>();
    /**
     * 流失数
     */
    private Integer lossNums = 0;

    /**
     * 跳转下个页面的数据
     */
    private Map<Integer,FlowRouteAnalysis.NextPageDateModel> nextPageDateModelMap = new HashMap<>();

}
