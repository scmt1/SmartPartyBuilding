/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto.flow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lhd
 */
@Data
public class CustomerReqDTO {
    /**
     * 时间类型 1今日实时 2 近7天 3 近30天 4自然日 5自然月
     */
    @ApiModelProperty("时间类型 1今日实时 2 近7天 3 近30天 4自然日 5自然月")
    private Integer dateType;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dayStartTime;
    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 时间
     */
    @ApiModelProperty("不传字段")
    private Date dateTime;

    /**
     * 店铺id
     */
    @ApiModelProperty("不传字段")
    private Long shopId;

    /**
     * 第三方系统id 1：微信小程序
     */
    @ApiModelProperty("不传字段")
    private Integer appId;

    /**
     * 粉丝=付费会员
     */
    private Integer member;
}
