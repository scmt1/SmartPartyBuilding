/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 阿里订单快递信息VO
 *
 * @author lhd
 * @date 2020-05-18 15:10:00
 */
@Data
public class DeliveryAliInfoBO {

    @ApiModelProperty(value = "物流公司名称",required=true)
    private String expName;

    @ApiModelProperty(value = "物流公司官网",required=true)
    private String expSite;

    @ApiModelProperty(value = "物流订单号",required=true)
    private String number;

    @ApiModelProperty(value = "物流状态",required=true)
    private Integer deliverystatus;

    @ApiModelProperty(value = "物流状态 0:没有记录 1:已揽收 2:运输途中 201:达到目的城市 3:已签收 4:问题件",required=true)
    private List<DeliveryAliItemInfoBO> list;

    public void setDeliverystatus(Integer deliverystatus) {
        this.deliverystatus = switchState(deliverystatus);
    }

    /**
     * 阿里快递的6种状态码转成快递鸟的6种
     *
     * @param deliverystatus 阿里快递状态码
     * 0 快递收件(揽件)
     * 1 在途中
     * 2 正在派件
     * 3 已签收
     * 4 派送失败（无法联系到收件人或客户要求择日派送，地址不详或手机号不清）
     * 5 疑难件（收件人拒绝签收，地址有误或不能送达派送区域，收费等原因无法正常派送）
     * 6 退件签收
     * @return 快递鸟状态码： 0:没有记录 1:已揽收 2:运输途中 201:达到目的城市 3:已签收 4:问题件
     */
    private Integer switchState(Integer deliverystatus) {
        int status = deliverystatus;
        switch (deliverystatus) {
            case 0:
                status = 1;
                break;
            case 1:
                status = 2;
                break;
            case 5:
            case 6:
                status = 4;
                break;
            default:
                break;
        }
        return status;
    }

}
