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
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yami
 */
@Data
@TableName("tz_refund_delivery")
public class RefundDelivery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 退货物流信息id
     */
    @TableId
    private Long refundDeliveryId;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 退款编号
     */
    private String refundSn;
    /**
     * 物流公司id
     */
    private Long deyId;
    /**
     * 物流公司名称
     */
    private String deyName;
    /**
     * 物流编号
     */
    private String deyNu;
    /**
     * 收件人姓名
     */
    private String receiverName;
    /**
     * 收件人手机
     */
    private String receiverMobile;
    /**
     * 收件人座机
     */
    private String receiverTelephone;
    /**
     * 收件人邮政编码
     */
    private String receiverPostCode;
    /**
     * 收件人地址
     */
    private String receiverAddr;
    /**
     * 发送人手机号码
     */
    private String senderMobile;
    /**
     * 描述
     */
    private String senderRemarks;
    /**
     * 图片凭证
     */
    private String imgs;
    /**
     * 创建时间
     */
    private Date createTime;
}
