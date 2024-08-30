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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺装修信息
 *
 * @author lhd
 * @date 2021-01-05 11:03:38
 */
@Data
@TableName("tz_shop_renovation")
public class ShopRenovation implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("店铺装修id")
    private Long renovationId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("装修名称")
    private String name;

    @ApiModelProperty("装修内容")
    private String content;

    @ApiModelProperty("是否主页 1.是 0.不是")
    private Integer homeStatus;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("装修类型 1Pc 2移动端")
    private Integer renovationType;

}
