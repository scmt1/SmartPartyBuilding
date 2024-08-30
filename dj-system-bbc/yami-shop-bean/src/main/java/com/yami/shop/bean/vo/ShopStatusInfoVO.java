/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author lth
 * @Date 2021/8/12 15:20
 */
@Data
public class ShopStatusInfoVO {
    @ApiModelProperty("账号状态， 1:启用 0:禁用 -1:删除")
    private Integer accountStatus;

    @ApiModelProperty("店铺状态(-1:已删除 0: 停业中 1:营业中 2:平台下线 3:平台下线待审核 4:开店申请中 5:开店申请待审核)")
    private Integer shopStatus;

    @ApiModelProperty("下线状态 1平台进行下线 2 重新申请，等待审核 3.审核通过 4 审核未通过")
    private Integer offlineStatus;

    @ApiModelProperty("下线原因")
    private String offlineReason;

    @ApiModelProperty("签约起始时间")
    private Date contractStartTime;

    @ApiModelProperty("签约终止时间")
    private Date contractEndTime;
}
