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
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@TableName("tz_prod")
public class Product implements Serializable {

    private static final long serialVersionUID = -4644407386444894349L;

    @TableId
    @ApiModelProperty(value = "商品ID", required = true)
    private Long prodId;

    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;

    @ApiModelProperty(value = "在平台当中的分类id")
    private Long categoryId;

    @ApiModelProperty(value = "在店铺当中的分类id")
    private Long shopCategoryId;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @ApiModelProperty(value = "详细描述")
    private String content;

    @ApiModelProperty(value = "简要描述,卖点等")
    private String brief;

    @ApiModelProperty(value = "原价")
    private Double oriPrice;

    @ApiModelProperty(value = "现价")
    private Double price;

    @ApiModelProperty(value = "商品视频")
    private String video;

    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "商品图片")
    private String imgs;

    @ApiModelProperty(value = "默认是1，表示正常状态, -1表示删除, 0下架,2平台下架,3平台审核")
    private Integer status;

    @ApiModelProperty(value = "品牌Id")
    private Long brandId;

    @ApiModelProperty(value = "配送方式json")
    private String deliveryMode;

    @ApiModelProperty(value = "运费模板id（0：统一包邮  -1：统一运费  其他：运费模板id）")
    private Long deliveryTemplateId;

    @ApiModelProperty(value = "统一运费的运费金额")
    private Double deliveryAmount;

    @ApiModelProperty(value = "录入时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "上架时间")
    private Date putawayTime;

    @ApiModelProperty(value = "商品类型(0普通商品 1拼团 2秒杀 3积分)")
    private Integer prodType;

    @ApiModelProperty(value = "商品积分价格")
    private Integer scorePrice;

    @ApiModelProperty(value = "活动id(对应prod_type)")
    private Long activityId;

    @ApiModelProperty(value = "活动价格")
    @TableField(exist = false)
    private Double activityPrice;

    @ApiModelProperty(value = "活动库存")
    @TableField(exist = false)
    private Integer activityTotalStocks;

    @ApiModelProperty(value = "预售状态 1：开启 0：未开启")
    private Integer preSellStatus;

    @ApiModelProperty(value = "预售发货时间")
    private Date preSellTime;

    @ApiModelProperty(value = "是否置顶，1.置顶 0.不置顶")
    private Integer isTop;

    @ApiModelProperty(value = "排序号")
    private Integer seq;

    @ApiModelProperty(value = "虚拟商品的留言备注")
    private String virtualRemark;

    @ApiModelProperty(value = "核销次数 -1.多次核销 0.无需核销 1.单次核销")
    private Integer writeOffNum;

    @ApiModelProperty(value = "多次核销次数 -1.无限次")
    private Integer writeOffMultipleCount;

    @ApiModelProperty(value = "核销有效期 -1.长期有效 0.自定义  x.x天内有效")
    private Integer writeOffTime;

    @ApiModelProperty(value = "核销开始时间")
    private Date writeOffStart;

    @ApiModelProperty(value = "核销结束时间")
    private Date writeOffEnd;

    @ApiModelProperty(value = "是否可以退款 1.可以 0不可以")
    private Integer isRefund;

    @ApiModelProperty(value = "商品类别 0.实物商品 1. 虚拟商品")
    private Integer mold;

    @Version
    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "使用语言 0中文 1中英文")
    private Integer useLang;

    @TableField(exist = false)
    @ApiModelProperty(value = "事件状态")
    private Integer eventStatus;

    @TableField(exist = false)
    @ApiModelProperty(value = "语言列表信息")
    private List<ProdLang> prodLangList;

    @TableField(exist = false)
    @ApiModelProperty(value = "品牌")
    private Brand brand;

    @TableField(exist = false)
    @ApiModelProperty(value = "sku列表")
    private List<Sku> skuList;

    @TableField(exist = false)
    @ApiModelProperty(value = "店铺名称")
    private String shopName;


    @TableField(exist = false)
    @ApiModelProperty(value = "语言")
    private Integer lang;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品参数列表")
    private List<ProdParameter> prodParameterList;

    @TableField(exist = false)
    @ApiModelProperty(value = "已经销售数量")
    private Integer soldNum;

    @TableField(exist = false)
    @ApiModelProperty(value = "注水销量")
    private Integer waterSoldNum;

    @TableField(exist = false)
    @ApiModelProperty(value = "库存量(可售卖库存)")
    private Integer totalStocks;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品中文名称")
    private String prodNameCn;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品英文名称")
    private String prodNameEn;

    @Data
    public static class DeliveryModeVO {
        @ApiModelProperty(value = "用户自提", required = true)
        private Boolean hasUserPickUp;

        @ApiModelProperty(value = "店铺配送", required = true)
        private Boolean hasShopDelivery;

        @ApiModelProperty(value = "同城配送", required = true)
        private Boolean hasCityDelivery;
    }
}
