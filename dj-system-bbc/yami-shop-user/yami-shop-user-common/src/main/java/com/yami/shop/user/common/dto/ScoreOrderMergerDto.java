/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.dto;

import com.yami.shop.bean.app.dto.ProductItemDto;
import com.yami.shop.bean.app.dto.UserAddrDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 积分商品订单合并信息
 * @author LHD
 */
@Data
public class ScoreOrderMergerDto {

    @ApiModelProperty(value = "实际总值", required = true)
    private Double actualTotal;

    @ApiModelProperty(value = "商品总值", required = true)
    private Double total;

    @ApiModelProperty(value = "用户等级免运费金额", required = true)
    private Double freeTransfee = 0.0;

    @ApiModelProperty(value = "总运费", required = true)
    private Double totalTransfee;

    @ApiModelProperty(value = "商品总数", required = true)
    private Integer totalCount;

    @ApiModelProperty(value = "地址Dto", required = true)
    private UserAddrDto userAddr;

    @ApiModelProperty(value = "商品信息", required = true)
    private ProductItemDto productItemDto;

    @ApiModelProperty(value = "msgId",required=true)
    private String msgId;

    @ApiModelProperty(value = "userId",required=true)
    private String userId;

    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNumber;

    @ApiModelProperty(value = "用户是否选择积分抵现(0不使用 1使用 默认不使用)")
    private Integer isScorePay;

    @ApiModelProperty(value = "订单优惠金额(所有店铺优惠金额和使用积分抵现相加)", required = true)
    private Double orderReduce = 0.0;

    @ApiModelProperty(value = "订单备注",required=true)
    private String remarks;

    @ApiModelProperty(value = "每次订单提交时的uuid")
    private String uuid;
}
