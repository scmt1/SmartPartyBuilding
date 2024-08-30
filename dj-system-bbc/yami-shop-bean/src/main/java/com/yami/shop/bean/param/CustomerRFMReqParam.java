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
public class CustomerRFMReqParam {

    /**
     *  最近付款时间在 recentTime 前 【全部/一年】的成交客户数据
     * 最近付款时间
     */
    @ApiModelProperty("最近付款时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recentTime;
    /**
     * 0全部 1一年
     */
    @ApiModelProperty("0全部 1一年")
    private Integer type;
}
