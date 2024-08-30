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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@TableName("tz_sku")
public class Sku implements Serializable {
    /**
     * sku ID
     */
    @TableId
    @ApiModelProperty(value = "sku ID", required = true)
    private Long skuId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID", required = true)
    private Long prodId;

    /**
     * 销售属性组合字符串,格式是p1:v1;p2:v2
     */
    @ApiModelProperty(value = "销售属性组合字符串,格式是p1:v1;p2:v2")
    private String properties;
    /**
     * 销售属性组合字符串,格式是p1:v1;p2:v2,英文
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "销售属性组合字符串,格式是p1:v1;p2:v2,英文")
    private String propertiesEn;

    /**
     * 原价
     */
    @ApiModelProperty(value = "原价")
    private Double oriPrice;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private Double price;
    /**
     * 积分价格
     */
    @ApiModelProperty(value = "积分价格")
    private Long skuScore;

    /**
     * 更新时，变化的库存
     */
    @ApiModelProperty(value = "更新时，变化的库存")
    @TableField(exist = false)
    private Integer changeStock;


    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间", required = true)
    private Date updateTime;

    /**
     * 记录时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "记录时间", required = true)
    private Date recTime;

    /**
     * 商家编码
     */
    @ApiModelProperty(value = "商家编码")
    private String partyCode;

    /**
     * 商品条形码
     */
    @ApiModelProperty(value = "商品条形码")
    private String modelId;

    /**
     * sku图片
     */
    @ApiModelProperty(value = "sku图片")
    private String pic;

    /**
     * sku名称
     */
    @ApiModelProperty(value = "sku名称")
    private String skuName;
    /**
     * sku 英文名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "sku 英文名称")
    private String skuNameEn;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String prodName;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private Integer version;

    /**
     * 重量
     */
    @ApiModelProperty(value = "重量")
    private Double weight;

    /**
     * 体积
     */
    @ApiModelProperty(value = "体积")
    private Double volume;

    /**
     * 状态：0禁用 1 启用
     */
    @ApiModelProperty(value = "状态：0禁用 1 启用")
    private Integer status;

    /**
     * 0 正常 1 已被删除
     */
    @ApiModelProperty(value = "0 正常 1 已被删除")
    private Integer isDelete;

    /**
     * 商品中文名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "商品中文名称")
    private String prodNameCn;
    /**
     * 商品英文名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "商品英文名称")
    private String prodNameEn;
    /**
     * 语言信息
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "语言信息")
    private List<SkuLang> skuLangList;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品状态")
    private Integer prodStatus;

    @TableField(exist = false)
    @ApiModelProperty(value = "店铺id")
    private Long shopId;


    @ApiModelProperty(value = "是否参加了套餐或赠品活动，1是0否")
    @TableField(exist = false)
    private Integer isParticipate;

    /**
     * 库存
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "可售卖库存", required = true)
    private Integer stocks;

    /**
     * 实际库存
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "实际库存")
    private Integer actualStock;
}
