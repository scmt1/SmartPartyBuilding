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
public class HotStatisticsVO {

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("商品名称")
    private String prodName;

    @ApiModelProperty("金额")
    private Double amount;

    @ApiModelProperty("销量")
    private Long sales;

    /**
     * 店铺id
     */
    private Long shopId;
}
