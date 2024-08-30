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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 赠品
 *
 * @author LGH
 * @date 2021-11-08 13:29:16
 */
@Data
@TableName("tz_giveaway")
public class Giveaway implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "赠品id")
    private Long giveawayId;

    @ApiModelProperty(value = "赠品名称")
    @NotBlank(message = "赠品名称不能为空")
    @Length(max = 20, message = "赠品名称长度不能超过20")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "活动开始时间")
    @NotNull(message = "活动开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间")
    @NotNull(message = "活动结束时间不能为空")
    private Date endTime;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "主商品id")
    @NotNull(message = "主商品不能为空")
    private Long prodId;

    @ApiModelProperty(value = "商品价格")
    @TableField(exist = false)
    private Double price;

    @ApiModelProperty(value = "主商品图片")
    @TableField(exist = false)
    private String pic;

    @ApiModelProperty(value = "主商品名称")
    @TableField(exist = false)
    private String prodName;

    @ApiModelProperty(value = "主商品名称")
    @TableField(exist = false)
    private String giveawayProdName;

    @ApiModelProperty(value = "购买数量（购买了多少件才赠送赠品）")
    @NotNull(message = "购买数量不能为空")
    private Integer buyNum;

    @ApiModelProperty(value = "状态 -1：已删除 0：关闭 1:开启")
    private Integer status;

    @ApiModelProperty(value = "赠送商品列表")
    @TableField(exist = false)
    @Valid
    private List<GiveawayProd> giveawayProds;
}
