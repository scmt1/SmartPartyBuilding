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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.bean.vo.UserVO;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品评论
 *
 * @author xwc
 * @date 2019-04-19 10:43:57
 */
@Data
@TableName("tz_prod_comm")
@EqualsAndHashCode
public class ProdComm implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "商品评论ID")
    private Long prodCommId;

    @ApiModelProperty(value = "商品ID")
    private Long prodId;

    @ApiModelProperty(value = "订单项ID")
    private Long orderItemId;

    @ApiModelProperty(value = "评论用户ID")
    private String userId;

    @TableField(exist = false)
    @ApiModelProperty(value = "评论用户昵称")
    private String nickName;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "掌柜回复")
    private String replyContent;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "记录时间")
    private Date recTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "回复时间")
    private Date replyTime;

    @ApiModelProperty(value = "是否回复 0:未回复  1:已回复")
    private Integer replySts;

    @ApiModelProperty(value = "IP来源")
    private String postip;

    @ApiModelProperty(value = "得分，0-5分")
    private Integer score;

    @ApiModelProperty(value = "有用的计数")
    private Integer usefulCounts;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "晒图的字符串 以逗号分隔")
    private String pics;

    @ApiModelProperty(value = "是否匿名(1:是  0:否)")
    private Integer isAnonymous;

    @ApiModelProperty(value = "是否显示，1:为显示，0:待审核， -1：不通过审核，不显示。 如果需要审核评论，则是0,，否则1")
    private Integer status;

    @ApiModelProperty(value = "评价(0好评 1中评 2差评)")
    private Integer evaluate;

    @TableField(exist = false)
    @ApiModelProperty(value = "好评数")
    private Integer goodCommNum;

    @TableField(exist = false)
    @ApiModelProperty(value = "关联用户")
    private UserVO user;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @TableField(exist = false)
    @ApiModelProperty(value = "图片数组")
    private String[] picsArray;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "筛选条件，记录时间的开始时间")
    private Date recStartTime;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "筛选条件，记录时间的结束时间")
    private Date recEndTime;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "筛选条件，回复时间的开始时间")
    private Date replyTimeStart;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "筛选条件，回复时间的结束时间")
    private Date replyTimeEnd;

    @TableField(exist = false)
    @ApiModelProperty(value = "评论数量")
    private Integer prodCommNum;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品图片")
    private String prodPic;



}
