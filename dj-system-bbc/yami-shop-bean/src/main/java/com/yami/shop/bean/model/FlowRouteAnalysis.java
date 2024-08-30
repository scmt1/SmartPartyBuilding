/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 流量分析—用户路径数据
 *
 * @author YXF
 * @date 2020-07-14 11:26:35
 */
@Data
@TableName("tz_flow_route_analysis")
public class FlowRouteAnalysis implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long flowAnalysisRouteId;
    /**
     * 步骤数
     */
    private Integer step;
    /**
     * 步骤数
     */
    private Date createDate;
    /**
     * 页面编号
     */
    private Integer pageId;
    /**
     * 会话次数
     */
    private Integer sessionNums;
    /**
     * 流失数
     */
    private Integer lossNums;
    /**
     * 跳转下个页面的数据
     */
    private String nextPageDate;
    /**
     * 系统类型
     */
    private Integer systemType;
    /**
     * json对象
     */
    @TableField(exist = false)
    private List<NextPageDateModel> nextPageDateModel;
    /**
     * 桑基图顺序编号
     */
    @TableField(exist = false)
    private Integer id;

    @Data
    public static class NextPageDateModel {

        /**
         * 页面编号
         */
        private Integer pageId;

        /**
         * 会话数量
         */
        private Integer sessionNum;
        /**
         * 会话数量
         */
        private List<String> sessionNumList;
    }

}
