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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.bean.model.ProdLang;
import com.yami.shop.bean.model.ProdParameter;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.Sku;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品参数
 * @author LGH
 */
@Data
public class ProductExportParam {

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 状态
     */
    private Integer status;


    /**
     * 商品名称
     */

    private String prodNameZh;

    /**
     * 商品名称
     */

    private String prodNameEn;

    /**
     * 商品价格
     */
    private Double price;

    /**
     * 商品原价
     */
    private Double oriPrice;

    /**
     * 注水销量
     */
    private Integer waterSoldNum;

    /**
     * 库存量
     */
    private Integer totalStocks;

    /**
     * 简要描述,卖点等
     */
    private String briefZh;

    /**
     * 简要描述,卖点等
     */
    private String briefEn;

    private String pic;

    /**
     * 商品视频
     */
    private String video;

    /**
     * 商品图片
     */
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String imgs;

    /**
     * 商品分类名称
     */
    private String categoryName;

    /**
     * 商品分类中文名称
     */
    private String categoryNameZh;

    /**
     * 商品分类英文名称
     */
    private String categoryNameEn;

    /**
     * 商品本店分类
     */
    private String shopCategoryName;

    /**
     * 已经销售数量
     */
    private Integer soldNum;
    /**
     * 品牌Id
     */
    private Long brandId;

    /**
     * 上架时间
     */
    private Date putawayTime;

    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 运费模板名称
     */
    private String transName;
    /**
     * 统一运费
     */
    private Double deliveryAmount;

    /**
     * 语言列表信息
     */
    @TableField(exist = false)
    private List<ProdLang> prodLangList;

    /**
     * sku列表字符串
     */
    private List<Sku> skuList;

    /**
     * content 商品详情
     */
    private String content;
    /**
     * content 店铺名称
     */
    private String shopName;

    /**
     * 是否能够用户自提
     */
    private Product.DeliveryModeVO deliveryModeVo;
    /**
     * 配送方式json
     */
    private String deliveryMode;
    /**
     * 配送方式json
     */
    private Long deliveryTemplateId;

    @ApiModelProperty(value = "商品类型(0普通商品 1拼团 2秒杀 3积分)")
    private Integer prodType;

    @ApiModelProperty(value = "商品类别 0.实物商品 1. 虚拟商品")
    private Integer mold;

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

    @ApiModelProperty(value = "是否置顶 1.置顶 0不置顶")
    private Integer isTop;

    @ApiModelProperty(value = "商品参数列表")
    private List<ProdParameter> prodParameterList;

    @ApiModelProperty(value = "语言 0.中文 1.英文")
    private Integer lang;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

}
