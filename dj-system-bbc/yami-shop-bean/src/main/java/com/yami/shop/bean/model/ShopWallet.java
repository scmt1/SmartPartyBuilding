/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商家钱包信息
 *
 * @author Dwl
 * @date 2019-09-19 14:02:57
 */
@Data
@TableName("tz_shop_wallet")
public class ShopWallet implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
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
    /**
     * 乐观锁
     */
    @Version
    private Long version;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("店铺名称")
    @TableField(exist = false)
    private String shopName;

    @ApiModelProperty("店铺logo")
    @TableField(exist = false)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String shopLogo;
}
