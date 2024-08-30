/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * 审核参数
 * @author LGH
 */
@Data
public class ShopAuditingParam {

    @NotNull(message = "审核状态不能为空")
    @ApiModelProperty("0 未审核 1已通过 -1未通过 2平台下线 3 平台下线待审核")
    private Integer status;

    @ApiModelProperty("0普通店铺 1优选好店")
    private Integer shopType;

    @ApiModelProperty("签约起始时间")
    private Date contractStartTime;

    @ApiModelProperty("签约终止时间")
    private Date contractEndTime;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("审核人id")
    private Long auditorId;

    @ApiModelProperty("店铺id")
    private Long shopId;

}
