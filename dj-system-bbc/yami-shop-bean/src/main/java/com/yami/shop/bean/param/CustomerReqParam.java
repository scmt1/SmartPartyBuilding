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
import java.util.List;

/**
 * @author Yami
 */
@Data
public class CustomerReqParam {

    @ApiModelProperty("时间类型 1今日实时 2 近7天 3 近30天 4自然日 5自然月")
    private Integer dateType;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 时间
     */
    @ApiModelProperty("不传字段")
    private Date dateTime;

    @ApiModelProperty("店铺id")
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

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺id列表，勾选导出")
    private List<Long> shopIds;

}
