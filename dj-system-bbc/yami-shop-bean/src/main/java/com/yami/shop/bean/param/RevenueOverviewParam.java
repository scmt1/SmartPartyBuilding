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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 财务管理—营收概况
 *
 * @author SJL
 * @date 2020-08-17
 */
@Data
public class RevenueOverviewParam {

    /**
     * 1今天 2昨天 3最近7天 4最近30天 5这个月 6上个月 7自定义范围(暂时不用)
     */
    private Integer dateType;

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
     * 门店 id
     */
    private Integer stationId;



    public void setTime(Date start, Date end) throws ParseException {
        // 取相邻时间段的实际收入、收入金额、支出金额
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strStart = format.format(start.getTime()) + " 00:00:00";
        String strEnd = format.format(end.getTime()) + " 00:00:00";

        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date changeEndDate = format2.parse(strStart);
        Date endDate = format2.parse(strEnd);

        long dateDiff = endDate.getTime() - changeEndDate.getTime();
        dateDiff = changeEndDate.getTime() - dateDiff - (24 * 60 * 60 * 1000);
        Date changeStartDate = new Date(dateDiff);

        strEnd = format2.format(changeEndDate.getTime() - 1000);
        endDate = format2.parse(strEnd);

        this.startTime = changeStartDate;
        this.endTime = endDate;
    }
}
