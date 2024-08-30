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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 商家提现申请信息
 *
 * @author YXF
 * @date 2020-04-07 14:22:08
 */
@Data
@TableName("tz_shop_bank_card")
public class ShopBankCard implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("id")
    private Long shopBankCardId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @Size(max = 20, message = "{yami.shop.bank.name.len.less}")
    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("银行开户支行")
    private String openingBank;

    @ApiModelProperty("收款方户名")
    private String recipientName;

    @ApiModelProperty("收款方账户")
    private String cardNo;

    @ApiModelProperty("是否默认  1:默认 0:非默认")
    private Integer isDefault;

    @ApiModelProperty("1.正常，-1.已删除")
    private Integer status;
}
