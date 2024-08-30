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

import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.Sku;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 商品参数
 * @author LGH
 */
@Data
public class ProductScoreParam{

    @ApiModelProperty("产品ID")
    private Long prodId;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("商品名称")
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度应该小于{max}")
    private String prodName;

    @ApiModelProperty("商品价格")
    @NotNull(message = "请输入商品价格")
    private Double price;

    @ApiModelProperty(value = "积分商品的配送方式，默认店铺配送")
    private Product.DeliveryModeVO deliveryModeVo;

    @ApiModelProperty("商品价格")
    @NotNull(message = "请输入商品原价")
    private Double oriPrice;

    @ApiModelProperty("库存量")
    @NotNull(message = "请输入商品库存")
    private Integer totalStocks;

    @ApiModelProperty("简要描述,卖点等")
    @Size(max = 500, message = "商品卖点长度应该小于{max}")
    private String brief;

    @ApiModelProperty("商品主图")
    @NotBlank(message = "请选择图片上传")
    private String pic;

    @ApiModelProperty("商品视频")
    private String video;

    @ApiModelProperty("品牌Id")
    private Long brandId;

    @ApiModelProperty("商品类型(0普通商品 1拼团 2秒杀 3积分)")
    private Integer prodType;

    @ApiModelProperty("商品图片")
    @NotBlank(message = "请选择图片上传")
    private String imgs;

    @ApiModelProperty("商品中文名称")
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度应该小于{max}")
    private String prodNameCn;

    @ApiModelProperty("商品英文名称")
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度应该小于{max}")
    private String prodNameEn;

    @ApiModelProperty("简要描述,卖点等(中文)")
    @Size(max = 500, message = "商品卖点长度应该小于{max}")
    private String briefCn;

    @ApiModelProperty("简要描述,卖点等(英文)")
    @Size(max = 500, message = "商品卖点长度应该小于{max}")
    private String briefEn;

    @ApiModelProperty("商品详情(中文)")
    private String contentCn;

    @ApiModelProperty("商品详情(英文)")
    private String contentEn;

    @ApiModelProperty("sku列表字符串")
    private List<Sku> skuList;

    @ApiModelProperty("商品详情")
    private String content;

    @ApiModelProperty("商品积分价格")
    @NotNull(message = "请输入商品价格")
    private Integer scorePrice;

    @ApiModelProperty(value = "运费模板id")
    @NotNull(message = "请选择运费模板")
    private Long deliveryTemplateId;

    @ApiModelProperty(value = "统一运费的运费金额")
    private Double deliveryAmount;

    @ApiModelProperty("积分商品类型")
    private Long scoreProdType;

}
