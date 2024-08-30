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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Yami
 */
@Data
public class OrderRefundParam {

    @ApiModelProperty(value = "订单编号", required = true)
    @NotEmpty(message = "订单编号不能为空")
    private String orderNumber;

    @ApiModelProperty(value = "退款单类型（1:整单退款,2:单个物品退款）", required = true)
    @NotNull(message = "退款单类型不能为空")
    private Integer refundType;

    @ApiModelProperty(value = "订单项ID（单个物品退款时使用）", required = true)
    private Long orderItemId;

    @ApiModelProperty(value = "退款数量（0或不传值则为全部数量）", required = true)
    private Integer goodsNum = 0;

    @ApiModelProperty(value = "申请类型(1:仅退款 2退款退货)", required = true)
    @NotNull(message = "申请类型不能为空")
    private Integer applyType;

    @ApiModelProperty(value = "货物状态(1:已收到货 0:未收到货)", required = true)
    @NotNull(message = "货物状态不能为空")
    private Boolean isReceiver;

    /**
     * 仅退款-未收到货申请原因
     * 11(质量问题)
     * 12(拍错/多拍/不喜欢)
     * 3(商品描述不符)
     * 14(假货), 15(商家发错货)
     * 16(商品破损/少件)
     * 17(其他)
     * 仅退款-已收到货申请原因
     * 51(多买/买错/不想要)
     * 52(快递无记录)
     * 53(少货/空包裹)
     * 54(未按约定时间发货)
     * 55(快递一直未送达)
     * 56(其他)
     * 退货退款-申请原因
     * 101(商品破损/少件)
     * 102(商家发错货)
     * 103(商品描述不符)
     * 104(拍错/多拍/不喜欢)
     * 105(质量问题)
     * 107(其他)
     */
    @ApiModelProperty(value = "申请原因(下拉选择)", required = true)
    @NotNull(message = "申请原因不能为空")
    private String buyerReason;

    @ApiModelProperty(value = "退款金额", required = true)
    @NotNull(message = "退款金额不能为空")
    private Double refundAmount;

    @ApiModelProperty(value = "手机号码（默认当前订单手机号码）", required = true)
    @NotNull(message = "手机号码不能为空")
    private String buyerMobile;

    @ApiModelProperty(value = "备注说明", required = true)
    private String buyerDesc;

    @ApiModelProperty(value = "凭证图片列表", required = true)
    private String photoFiles;

    @ApiModelProperty(value = "选中的赠品orderItemId列表", required = true)
    private List<Long> giveawayItemIds;
}
