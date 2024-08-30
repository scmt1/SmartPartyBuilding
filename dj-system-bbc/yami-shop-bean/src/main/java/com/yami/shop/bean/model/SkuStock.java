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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 库存信息
 *
 * @author LGH
 * @date 2022-05-06 14:59:42
 */
@Data
@TableName("tz_sku_stock")
@ApiModel("库存信息")
public class SkuStock implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    private Long stockId;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "spu id")
    private Long prodId;
    @ApiModelProperty(value = "SKU ID")
    private Long skuId;
    @ApiModelProperty(value = "实际库存")
    private Integer actualStock;
    @ApiModelProperty(value = "锁定库存")
    private Integer lockStock;
    @ApiModelProperty(value = "可售卖库存")
    private Integer stocks;
}
