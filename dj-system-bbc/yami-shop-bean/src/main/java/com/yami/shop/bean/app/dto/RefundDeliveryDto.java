/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Yami
 */
@Data
public class RefundDeliveryDto {

    @ApiModelProperty("物流公司id")
    private Long deyId;

    @ApiModelProperty("物流公司名称")
    private String deyName;

    @ApiModelProperty("物流编号")
    private String deyNu;

    @ApiModelProperty("收件人姓名")
    private String receiverName;

    @ApiModelProperty("收件人手机")
    private String receiverMobile;

    @ApiModelProperty("收件人座机")
    private String receiverTelephone;

    @ApiModelProperty("收件人邮政编码")
    private String receiverPostCode;

    @ApiModelProperty("收件人地址")
    private String receiverAddr;

    @ApiModelProperty("发送人手机号码")
    private String senderMobile;

    @ApiModelProperty("描述")
    private String senderRemarks;

    @ApiModelProperty("图片凭证")
    private String imgs;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
