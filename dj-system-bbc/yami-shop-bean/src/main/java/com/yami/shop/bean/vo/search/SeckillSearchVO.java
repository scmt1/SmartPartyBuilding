/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author FrozenWatermelon
 */
@Data
public class SeckillSearchVO {

    @ApiModelProperty(value = "秒杀活动id", required = true)
    private Long seckillId;

    @ApiModelProperty(value = "活动名称", required = true)
    private String seckillName;

    @ApiModelProperty(value = "开始时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "秒杀活动剩余总库存", required = true)
    private Integer seckillTotalStocks;

    @ApiModelProperty(value = "秒杀活动原始库存", required = true)
    private Integer seckillOriginStocks;
}
