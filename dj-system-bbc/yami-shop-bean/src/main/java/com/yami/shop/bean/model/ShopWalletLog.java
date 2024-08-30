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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 商家钱包记录
 *
 * @author Dwl
 * @date 2019-09-19 14:02:57
 */
@Data
@TableName("tz_shop_wallet_log")
public class ShopWalletLog implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "店铺钱包id")
    private Long walletLogId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "关联订单号")
    private String orderNumber;

    @ApiModelProperty(value = "退款单id")
    private String refundSn;

    @ApiModelProperty(value = "收支类型 0支出 1收入")
    private Integer ioType;

    @ApiModelProperty(value = "金额类型 0 未结算金额 1可提现金额  2冻结金额 3总结算金额")
    private Integer amountType;

    @ApiModelProperty(value = "改变金额")
    private Double changeAomunt;

    @ApiModelProperty(value = "原因 0用户支付 1订单结算 2 用户申请退款 3 拒绝用户退款申请 4 提现申请 5 提现申请被拒绝 6 提现申请通过")
    private Integer reason;

    @ApiModelProperty(value = "用户金额（支付实付金额，退款实际申请金额）")
    private Double userAmount;

    @ApiModelProperty(value = "平台补贴金额")
    private Double platformAmount;

    @ApiModelProperty(value = "分销占用金额")
    private Double distributionAmount;

    @ApiModelProperty(value = "平台佣金")
    private Double platformCommission;

    @ApiModelProperty(value = "商家优惠金额")
    private Double shopReduceAmount;

    @ApiModelProperty(value = "商品总金额")
    private Double totalAmount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty("店铺名称")
    @TableField(exist = false)
    private String shopName;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date endTime;
}
