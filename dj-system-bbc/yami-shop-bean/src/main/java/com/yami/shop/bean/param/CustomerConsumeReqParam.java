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
public class CustomerConsumeReqParam {

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * com.yami.shop.bean.enums.TransactionUserType
     */
    @ApiModelProperty(" 0全部成交客户 1新成交客户 2老成交客户")
    private Integer type;

    @ApiModelProperty("回购周期间隔 10天 30天")
    private Integer cycle;

    @ApiModelProperty("shopId 前端不用传值字段")
    private Long shopId;

}
