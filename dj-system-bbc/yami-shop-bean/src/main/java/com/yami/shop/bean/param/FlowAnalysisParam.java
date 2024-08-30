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
import com.yami.shop.bean.enums.FlowTimeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2020-07-14 11:26:35
 */
@Data
public class FlowAnalysisParam {
    /**
     * 时间类型 1:自然日  2:自然周 3:自然月 4:今日实时 5:近七天 6:近30天 7:自定义
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
     * 结束时间（时间戳）
     */
    private Long end;
    /**
     * 类型 1:页面统计数据  2:商品详情页数据
     */
    private Integer pageType;
    /**
     * 系统类型 0:全部 1:pc  2:h5  3:小程序 4:安卓  5:ios
     */
    private Integer systemType;
    /**
     * 排序 0:默认 1:降序  2:升序
     */
    @ApiModelProperty(value = "0:默认 1:降序  2:升序")
    private Integer sort;
    /**
     * 排序类型
     *  0：默认  1:浏览量   2:访客数    3:点击次数   4:点击人数   5:分享访问次数
     *  6:分享访问人数  7:引导下单金额  8:引导下单人数  9:引导支付金额   10:引导支付人数
     */
    @ApiModelProperty(value = "0：默认1:浏览量2:访客数3:点击次数4:点击人数5:分享访问次数6:分享访问人数7:引导下单金额8:引导下单人数9:引导支付金额10:引导支付人数")
    private Integer sortType;
    /**
     * 1:两周 2.一年
     * @param type
     */
    public void setTime(Integer type){
        if (Objects.isNull(start)) {
            startTime = new Date();
            endTime = startTime;
            return;
        }
        // 1:自然日  2:自然周 3:自然月 4:今日实时 5:近七天 6:近30天 7:自定义
        if (timeType.equals(FlowTimeTypeEnum.DAY.value())){
            startTime = new Date(start);
            startTime = DateUtil.beginOfDay(startTime);
            endTime = DateUtil.offsetDay(startTime,1);
        }else if (timeType.equals(FlowTimeTypeEnum.WEEK.value())){
            startTime = new Date(start);
            endTime = DateUtil.beginOfWeek(startTime);
            endTime = DateUtil.offsetWeek(endTime,1);
            if (type.equals(1)){
                //上周的第一天
                startTime = DateUtil.offsetWeek(endTime,-1);
            }else{
                //12个周前的第一天
                startTime = DateUtil.offsetWeek(endTime,-12);
            }
        }else if (timeType.equals(FlowTimeTypeEnum.MONTH.value())){
            startTime = new Date(start);
            endTime = DateUtil.beginOfMonth(startTime);
            endTime = DateUtil.offsetMonth(endTime,1);
            if (type.equals(1)){
                //上月的第一天
                startTime = DateUtil.beginOfMonth(DateUtil.offsetMonth(endTime,-1));
            }else{
                //11个月前的第一天
                startTime = DateUtil.beginOfMonth(DateUtil.offsetMonth(endTime,-12));
            }
        }else if (timeType.equals(FlowTimeTypeEnum.REAL_TIME.value())){
            startTime = DateUtil.beginOfDay(new Date());
            endTime = DateUtil.offsetDay(startTime,1);
        }else if (timeType.equals(FlowTimeTypeEnum.SEVEN_DAYS.value())){
            endTime = DateUtil.beginOfDay(new Date());
            startTime = DateUtil.offsetDay(endTime,-7);
        }else if (timeType.equals(FlowTimeTypeEnum.ONE_MONTH.value())){
            endTime = DateUtil.beginOfDay(new Date());
            startTime = DateUtil.offsetDay(endTime,-30);
        }else if (timeType.equals(FlowTimeTypeEnum.CUSTOM.value())){
            endTime = new Date(end);
            startTime = new Date(start);
//            flowUserAnalysisParam.setEndTime(DateUtil.endOfDay(new Date(flowUserAnalysisParam.getEnd())));
//            flowUserAnalysisParam.setStartTime(new Date(flowUserAnalysisParam.getStart()));
        }
    }
}
