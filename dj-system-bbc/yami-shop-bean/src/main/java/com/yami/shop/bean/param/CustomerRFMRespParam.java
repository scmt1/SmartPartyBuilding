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

/**
 * @author Yami
 */
@Data
public class CustomerRFMRespParam {

    @ApiModelProperty("频次")
    private Integer frequency = 0;

    @ApiModelProperty("支付金额")
    private Double payAmount = 0.0;

    @ApiModelProperty("购买客户数")
    private Integer payBuyers = 0;

    @ApiModelProperty("近期时间；1：R<=30 2：30<R<=90 3：90<R<=180 4：180<R<=365 5：R>365")
    private Integer recency;

}
