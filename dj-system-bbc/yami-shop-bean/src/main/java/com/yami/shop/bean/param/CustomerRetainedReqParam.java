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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Yami
 */
@Data
public class CustomerRetainedReqParam {

    /**
     * 时间类型 1最近1个月 2最近3个月 3最近6个月 4最近1年
     * @see com.yami.shop.bean.enums.RetainedDateType
     */
    @ApiModelProperty("时间类型 1最近1个月 2最近3个月 3最近6个月 4最近1年")
    private Integer dateType;
    /**
     * 留存类型 1访问留存  2成交留存
     */
    @ApiModelProperty("留存类型 1访问留存  2成交留存")
    private Integer retainType;
    /**
     *  1月留存 2周留存
     */
    @ApiModelProperty("1月留存 2周留存,暂时不统计周留存")
    private Integer dateRetainType;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
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
}
