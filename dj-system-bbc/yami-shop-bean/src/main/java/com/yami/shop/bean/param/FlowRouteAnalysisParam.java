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

import cn.hutool.core.date.DateUtil;
import com.yami.shop.bean.dto.SankeyDto;
import com.yami.shop.bean.enums.FlowTimeTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * 流量分析—路径数据统计表
 *
 * @author YXF
 * @date 2020-07-14 11:26:35
 */
@Data
public class FlowRouteAnalysisParam {
    /**
     * 时间类型 1:自然日  2:自然周 3:自然月 5:近七天 6:近30天
     */
    private Integer timeType;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 开始时间（时间戳）
     */
    private Long start;
    /**
     * 系统类型 0:全部 1:pc  2:h5  3:小程序 4:安卓  5:ios
     */
    private Integer systemType;

    private SankeyDto sankeyDto;

    public void setTime(){
        if (Objects.isNull(start)) {
            startTime = new Date();
            endTime = startTime;
            return;
        }
        if (timeType.equals(FlowTimeTypeEnum.DAY.value())){
            startTime = DateUtil.beginOfDay(new Date(start));
            endTime = DateUtil.offsetDay(startTime,1);
        }else if (timeType.equals(FlowTimeTypeEnum.WEEK.value())){
            startTime = DateUtil.beginOfWeek(new Date(start));
            endTime = DateUtil.offsetWeek(startTime,1);
            start = startTime.getTime();
        }else if (timeType.equals(FlowTimeTypeEnum.MONTH.value())){
            startTime = DateUtil.beginOfMonth(new Date(start));
            endTime = DateUtil.offsetMonth(startTime,1);
        }else if (timeType.equals(FlowTimeTypeEnum.SEVEN_DAYS.value())){
            endTime = DateUtil.beginOfDay(new Date());
            startTime = DateUtil.offsetDay(endTime,-7);
        }else if (timeType.equals(FlowTimeTypeEnum.ONE_MONTH.value())){
            endTime = DateUtil.beginOfDay(new Date());
            startTime = DateUtil.offsetDay(endTime,-30);
        }
    }
}
