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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商家提现申请信息
 *
 * @author Dwl
 * @date 2019-09-19 14:22:08
 */
@Data
@TableName("tz_shop_withdraw_cash")
public class ShopWithdrawCash implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty("提现id")
    private Long cashId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("审核人id")
    private Long auditorId;

    @ApiModelProperty("提现金额")
    private Double amount;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("审核时间")
    private Date auditingTime;

    @ApiModelProperty("0 审核中 1审核成功 -1审核失败")
    private Integer status;

    @ApiModelProperty("平台备注")
    private String remarks;

    @ApiModelProperty("店铺备注")
    private String shopRemarks;

    @ApiModelProperty("付款户名")
    private String payingAccount;

    @ApiModelProperty("汇款账户")
    private String payingCardNo;

    @ApiModelProperty("汇款时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String payingTime;

    @ApiModelProperty("收款银行卡id")
    private Long shopBankCardId;

    @ApiModelProperty("店铺名称")
    @TableField(exist = false)
    private String shopName;

    @ApiModelProperty("店铺名称")
    @TableField(exist = false)
    private String code;

    @ApiModelProperty("店铺logo")
    @TableField(exist = false)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String shopLogo;

    @ApiModelProperty("收款银行卡信息")
    @TableField(exist = false)
    private ShopBankCard shopBankCard;


}
