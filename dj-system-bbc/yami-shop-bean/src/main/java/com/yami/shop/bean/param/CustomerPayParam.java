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

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Yami
 */
@Data
public class CustomerPayParam {

    private String currentDay;
    /**
     * 访客数
     */
    private Integer visitor = 0;
    /**
     * 累积粉丝数
     */
    private Integer fashNum = 0;
    /**
     * 累积会员数
     */
    private Integer member = 0;
    /**
     * 成交客户数
     */
    private Integer payNum = 0;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    private Integer number;
}
