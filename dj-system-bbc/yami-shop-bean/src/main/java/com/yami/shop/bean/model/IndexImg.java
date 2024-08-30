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
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yami
 */
@Data
@TableName("tz_index_img")
public class IndexImg implements Serializable {
    private static final long serialVersionUID = -3468251351681518798L;
    /**
     * 主键
     */
    @TableId
    @ApiModelProperty("轮播图id")
    private Long imgId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("图片")
    private String imgUrl;

    @ApiModelProperty("描述")
    private String des;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("链接")
    private String link;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("顺序")
    private Integer seq;

    @ApiModelProperty("上传时间")
    private Date uploadTime;

    @ApiModelProperty("类型")
    private int type;

    @ApiModelProperty("图片类型 0:移动端 1:pc")
    private Integer imgType;

    @ApiModelProperty("关联id")
    private Long relation;

    @ApiModelProperty("商品图片")
    @TableField(exist = false)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty("商品名称")
    @TableField(exist = false)
    private String prodName;

}
