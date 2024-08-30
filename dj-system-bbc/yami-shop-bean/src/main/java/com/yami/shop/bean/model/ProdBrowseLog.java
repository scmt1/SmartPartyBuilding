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

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品浏览记录表
 *
 * @author LGH
 * @date 2021-11-01 10:43:09
 */
@Data
@TableName("tz_prod_browse_log")
public class ProdBrowseLog implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "主键id")
    private Long prodBrowseLogId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "浏览日期")
    private Date browseTime;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "商品id")
    @NotNull(message = "商品id不能为空")
    private Long prodId;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "商品类型")
    private Integer prodType;

    @ApiModelProperty(value = "商品名称")
    @TableField(exist = false)
    private String prodName;

    @ApiModelProperty(value = "商品主图")
    @TableField(exist = false)
    private String pic;

    @ApiModelProperty(value = "商品价格")
    @TableField(exist = false)
    private Double price;

    @ApiModelProperty(value = "积分价格")
    @TableField(exist = false)
    private Double scoreFee;

    @ApiModelProperty("商品状态 1: 上架，其他：下架")
    @TableField(exist = false)
    private Integer prodStatus;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;
}
