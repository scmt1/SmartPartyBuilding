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

import com.yami.shop.bean.dto.FlowUserAnalysisDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 *
 * @author YXF
 * @date 2020-07-17 09:50:13
 */
@Data
public class FlowUserAnalysisParam {

    /**
     * 1:近七天  2：近30天 3:自定义
     */
    private Integer type;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 开始时间
     */
    private Long start;
    /**
     * 结束时间
     */
    private Long end;
    /**
     *访问深度
     */
    private List<VisitNum> visitPageList;
    /**
     *访客地域分布
     */
    private List<FlowUserAnalysisDto> visitUserList;

    /**
     *系统设备-pc
     */
    private int pc;
    /**
     *系统设备-h5
     */
    private int h5;
    /**
     *系统设备-小程序
     */
    private int applets;
    /**
     *系统设备-安卓
     */
    private int android;
    /**
     *系统设备-ios
     */
    private int ios;

    @Data
    @AllArgsConstructor
    public static class VisitNum {
        /**
         * 访问页面数量 (编号:访问页面数量)1:1 2:2 3:3 4:4 5:5 6:6-10 7:11-20 8:20+
         */
        private Integer visitId;

        /**
         * 用户数
         */
        private Integer userNums;
    }
    @Data
    public static class System {
        /**
         *系统设备-pc
         */
        private Set<String> pc = new HashSet<>();
        /**
         *系统设备-h5
         */
        private Set<String> h5 = new HashSet<>();
        /**
         *系统设备-小程序
         */
        private Set<String> applets = new HashSet<>();
        /**
         *系统设备-安卓
         */
        private Set<String> android = new HashSet<>();
        /**
         *系统设备-ios
         */
        private Set<String> ios = new HashSet<>();
    }
}
