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
 * 二维码数据信息
 *
 * @author LGH
 * @date 2020-03-12 16:49:20
 */
@Data
@TableName("tz_qrcode_ticket")
public class QrcodeTicket implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 二维码ticket id
     */
    @TableId
    private Long ticketId;
    /**
     * 二维码ticket
     */
    private String ticket;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 过期时间
     */
    private Date expireTime;
    /**
     * 二维码类型（1 我的邀请二维码）
     */
    private Integer type;
    /**
     * 二维码实际内容
     */
    private String content;
    /**
     * 这个二维码要跳转的url
     */
    private String ticketUrl;

}
