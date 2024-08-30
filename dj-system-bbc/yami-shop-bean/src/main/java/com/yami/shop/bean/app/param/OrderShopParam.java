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

import com.yami.shop.bean.model.OrderInvoice;
import com.yami.shop.bean.vo.VirtualRemarkVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author LGH
 */
@Data
public class OrderShopParam {

    /** 店铺ID **/
    @ApiModelProperty(value = "店铺id",required=true)
    private Long shopId;

    /**
     * 订单备注信息
     */
    @ApiModelProperty(value = "订单备注信息",required=true)
    private String remarks;

    @ApiModelProperty(value = "每次订单提交时的uuid")
    private String uuid;

    @ApiModelProperty(value = "订单发票信息")
    private OrderInvoice orderInvoice;

    @ApiModelProperty(value = "虚拟商品留言备注")
    private List<VirtualRemarkVO> virtualRemarkList;
}
