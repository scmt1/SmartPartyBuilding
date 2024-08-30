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
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户流量记录
 *
 * @author YXF
 * @date 2020-07-13 13:18:33
 */
@Data
@TableName("tz_flow_log")
public class FlowLog implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long flowLogId;
    /**
     * 会话uuid
     */
    private String uuid;
    /**
     * uuid
     */
    private String uuidSession;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 系统类型 1:pc  2:h5  3:小程序 4:安卓  5:ios
     */
    private Integer systemType;
    /**
     * 用户登陆ip
     */
    private String ip;
    /**
     * 页面id
     */
    private Integer pageId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 1:页面访问  2:分享访问  3:页面点击 4:加购
     */
    private Integer visitType;
    /**
     * 业务数据（商品页：商品id;支付界面：订单编号数组；支付成功界面：订单编号数组）
     */
    private String bizData;

    /**
     * 业务数据（商品页：商品类型）
     */
    private String bizType;
    /**
     * 用户操作步骤数
     */
    private Integer step;
    /**
     * 用户操作数量
     */
    private Integer nums;

    /**
     * 页面结束时间（跳转页面时间）
     */
    @TableField(exist = false)
    private Date endTime;

    /**
     * 用户下一操作页面编号
     */
    @TableField(exist = false)
    private Integer nextPageId;
    /**
     * 用户下一操作页面编号
     */
    @TableField(exist = false)
    private Long stopTime;

    /**
     * 小时（该数据为第几小时的）
     */
    @TableField(exist = false)
    private Integer hour;

}
