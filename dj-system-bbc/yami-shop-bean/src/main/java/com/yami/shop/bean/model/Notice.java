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

import com.alipay.api.domain.UserDetailInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 公告管理
 *
 * @author hzm
 * @date 2019-04-18 21:21:40
 */
@Data
@TableName("tz_notice")
public class Notice implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty("公告id")
    private Long id;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("类型不能为空（平台端同时创建多类型公告,用逗号隔开）(1:在商家端展示 2:在用户端展示 ")
    private String types;

    @ApiModelProperty("公告标题")
    private String title;

    @ApiModelProperty("公告内容")
    private String content;

    @ApiModelProperty("商家可见范围 (用逗号隔开，为空则全部可见)")
    private String multiShopIds;

    @ApiModelProperty("用户可见范围 (用逗号隔开，为空则全部可见)")
    private String userIds;

    @ApiModelProperty("状态(1:公布 0:撤回)")
    private Integer status;

    @ApiModelProperty("是否置顶（1:是 0：否）")
    private Integer isTop;

    @ApiModelProperty("发布时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    @ApiModelProperty("更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date endTime;


    @ApiModelProperty("是否立即发送 1：立即发送  0：定时发送")
    private Integer immediatelySend;

    @ApiModelProperty("定时发送时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;


    @ApiModelProperty("可见范围的商家详情")
    @TableField(exist = false)
    private List<ShopDetail> shopDetailList;

    @ApiModelProperty("可见范围的用户详情")
    @TableField(exist = false)
    private List<User> userDetailList;

    @ApiModelProperty("当前账号id(商家：租户id   用户：userId)")
    @TableField(exist = false)
    private String accountId;

    @ApiModelProperty("(根据公告类型查找  1:在商家端展示 2:在用户端展示")
    @TableField(exist = false)
    private Integer type;
}
