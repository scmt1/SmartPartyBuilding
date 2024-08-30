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

import com.yami.shop.bean.app.dto.OrderSelfStationDto;
import com.yami.shop.bean.model.OrderInvoice;
import com.yami.shop.bean.vo.VirtualRemarkVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Yami
 */
@Data
@ApiModel(value = "提交订单参数")
public class SubmitOrderParam {
    @ApiModelProperty(value = "每个店铺提交的订单信息", required = true)
    private List<OrderShopParam> orderShopParams;

    @ApiModelProperty(value = "每次订单提交时的uuid")
    private String uuid;

    @ApiModelProperty(value = "自提信息Dto")
    private OrderSelfStationDto orderSelfStationDto;

    @ApiModelProperty(value = "发票信息")
    private List<OrderInvoice> orderInvoiceList;

    @ApiModelProperty(value = "虚拟商品留言备注")
    private List<VirtualRemarkVO> virtualRemarkList;
}
