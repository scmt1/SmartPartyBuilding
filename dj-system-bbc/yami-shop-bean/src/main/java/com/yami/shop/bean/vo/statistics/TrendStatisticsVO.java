/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo.statistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 热卖信息统计
 * @author Yami
 */
@Data
public class TrendStatisticsVO {

    @ApiModelProperty("时间")
    private String date;

    @ApiModelProperty("浏览量")
    private Long visitNum;

    @ApiModelProperty("访客数")
    private Long userNum;

    @ApiModelProperty("交易金额")
    private Double amount;
}
