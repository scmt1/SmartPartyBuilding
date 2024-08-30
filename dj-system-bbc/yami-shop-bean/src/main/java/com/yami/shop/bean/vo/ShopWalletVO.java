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

/**
 * 商家钱包信息
 *
 * @author Dwl
 * @date 2019-09-19 14:02:57
 */
@Data
public class ShopWalletVO {

    @ApiModelProperty("店铺钱包id")
    private Long shopWalletId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("未结算金额（用户支付）")
    private Double unsettledAmount;

    @ApiModelProperty("已结算金额（用户确认收货后，可以提现）")
    private Double settledAmount;

    @ApiModelProperty("冻结金额（用户提现申请）")
    private Double freezeAmount;

    @ApiModelProperty("累积结算金额")
    private Double totalSettledAmount;

    @ApiModelProperty("交易笔数（支付订单数）")
    private Long transactionCount;

    @ApiModelProperty("昨天结算金额")
    private Double yesterdaySettledAmount;
}
