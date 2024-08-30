/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author lhd
 * @date 2020-08-06 16:29:53
 */
@Data
public class LiveRoomParam implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "微信直播间id")
    private Long id;

    @ApiModelProperty(value = "微信直播间id")
    private Long roomId;

    @ApiModelProperty(value = "搜索类型 1.搜索直播间信息 2.搜索商品名 3.搜索商品id")
    private Integer searchType;

    @ApiModelProperty(value = "直播间名称")
    private String name;

    @ApiModelProperty(value = "主播昵称")
    private String anchorName;

    @ApiModelProperty(value = "是否置顶")
    private Integer roomTop;

    @ApiModelProperty(value = "主播微信号")
    private String anchorWechat;

    @ApiModelProperty(value = "背景图")
    private String coverImg;

    @ApiModelProperty(value = "主播分享图")
    private String shareImg;

    @ApiModelProperty(value = "购物直播频道封面图")
    private String feedsImg;

    @ApiModelProperty(value = "1.竖屏 2.横屏")
    private Integer screenType;

    @ApiModelProperty(value = "直播间状态。101：直播中，102：未开始，103已结束，104禁播，105：暂停，106：异常，107：已过期")
    private Integer liveStatus;

    @ApiModelProperty(value = "直播开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "直播结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "商品id")
    @TableField(exist = false)
    private String prodId;
}
