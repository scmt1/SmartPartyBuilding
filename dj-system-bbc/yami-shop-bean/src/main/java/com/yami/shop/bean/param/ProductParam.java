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

import com.yami.shop.bean.model.ProdParameter;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.model.Sku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * 商品参数
 * @author LGH
 */
@Data
@ApiModel("商品参数")
public class ProductParam {

    @ApiModelProperty(value = "产品id")
    private Long prodId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "商品状态（-1:删除、0:商家下架、1:上架、2:违规下架、3:平台审核）")
    private Integer status;

    @ApiModelProperty(value = "sku状态（0 禁用 1 启用）")
    private Integer skuStatus;

    @ApiModelProperty(value = "预售状态 1：开启 0：未开启")
    private Integer preSellStatus;

    @ApiModelProperty(value = "预售发货时间")
    private Date preSellTime;

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度应该小于{max}")
    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @Size(max = 500, message = "商品卖点长度应该小于{max}")
    @ApiModelProperty(value = "商品卖点")
    private String brief;

    @ApiModelProperty(value = "商品详情")
    private String content;

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度应该小于{max}")
    @ApiModelProperty(value = "商品中文名称")
    private String prodNameCn;

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度应该小于{max}")
    @ApiModelProperty(value = "商品英文名称")
    private String prodNameEn;

    @Size(max = 500, message = "商品卖点长度应该小于{max}")
    @ApiModelProperty(value = "中文简要描述,卖点等")
    private String briefCn;

    @Size(max = 500, message = "商品卖点长度应该小于{max}")
    @ApiModelProperty(value = "英文简要描述,卖点等")
    private String briefEn;

    @ApiModelProperty(value = "商品中文详情")
    private String contentCn;

    @ApiModelProperty(value = "商品英文详情")
    private String contentEn;

    @ApiModelProperty(value = "商品类型(0普通商品 1拼团 2秒杀 3积分 4套餐 5活动)")
    private Integer prodType;

    @NotNull(message = "请输入商品价格")
    @ApiModelProperty(value = "商品价格")
    private Double price;

    @NotNull(message = "请输入商品原价")
    @ApiModelProperty(value = "商品原价")
    private Double oriPrice;

    @ApiModelProperty(value = "商品类别 0.实物商品 1. 虚拟商品")
    private Integer mold;

    @ApiModelProperty(value = "商品ids")
    private List<Long> prodIds;

    @ApiModelProperty(value = "skuIds")
    private List<Long> skuIds;

    @NotNull(message = "请输入商品库存")
    @ApiModelProperty(value = "库存量")
    private Integer totalStocks;

    @NotBlank(message = "请选择图片上传")
    @ApiModelProperty(value = "商品图片")
    private String pic;

    @ApiModelProperty(value = "商品视频")
    private String video;

    @NotBlank(message = "请选择图片上传")
    @ApiModelProperty(value = "商品图片")
    private String imgs;

    @NotNull(message = "请选择商品分类")
    @ApiModelProperty(value = "商品分类")
    private Long categoryId;

    @NotNull(message = "请选择本店商品分类")
    @ApiModelProperty(value = "本店商品分类")
    private Long shopCategoryId;

    @ApiModelProperty(value = "品牌Id")
    private Long brandId;

    @ApiModelProperty(value = "员工Id")
    private Long employeeId;

    @ApiModelProperty("1：商品名称 2：商品编码")
    private Integer prodKeyType;

    @ApiModelProperty("搜索商品关键词(0:商品名称 1：商品编码)")
    private String prodKey;

    @ApiModelProperty("供货商id")
    private Long supplierId;

    @ApiModelProperty(value = "sku列表字符串")
    private List<Sku> skuList;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "当前语言")
    private Integer lang;

    @ApiModelProperty(value = "配送方式")
    private Product.DeliveryModeVO deliveryModeVo;

    @ApiModelProperty(value = "运费模板id（0：统一包邮  -1：统一运费  其他：运费模板id）")
    private Long deliveryTemplateId;

    @ApiModelProperty(value = "统一运费的运费金额")
    private Double deliveryAmount;

    @ApiModelProperty(value = "是否筛掉分销商品 1是 0否")
    private Integer isDistribution;

    @ApiModelProperty(value = "是否置顶，1.置顶 0.不置顶")
    private Integer isTop;

    @ApiModelProperty(value = "积分价格")
    private Integer scorePrice;

    @ApiModelProperty(value = "排序")
    private Integer seq;

    @ApiModelProperty(value = "配送方式 0用户自提, 1店铺配送, 2同城配送")
    private Integer deliveryMode;

    @ApiModelProperty(value = "虚拟商品的留言备注")
    private String virtualRemark;

    @ApiModelProperty(value = "核销次数 -1.多次核销 0.无需核销 1.单次核销")
    @Min(value = -1, message = "只能为-1,0或1")
    @Max(value = 1, message = "只能为-1,0或1")
    private String writeOffNum;

    @ApiModelProperty(value = "多次核销次数 -1.无限次")
    private Integer writeOffMultipleCount;

    @ApiModelProperty(value = "核销有效期 -1.长期有效 0.自定义  x.x天内有效")
    @Max(value = 9999, message = "核销有效期应该小于{max}天")
    private Integer writeOffTime;

    @ApiModelProperty(value = "核销开始时间")
    private Date writeOffStart;

    @ApiModelProperty(value = "核销结束时间")
    private Date writeOffEnd;

    @ApiModelProperty(value = "是否可以退款 1.可以 0不可以")
    @Min(value = 0, message = "只能为0或1")
    @Max(value = 1, message = "只能为0或1")
    private Integer isRefund;

    @ApiModelProperty(value = "分销业绩-推广效果：0无 1.排序号排序")
    private Integer sortParam = 0;

    @ApiModelProperty(value = "排序类型 0无 1 正序 2倒序")
    private Integer sortType = 0;

    @ApiModelProperty(value = "商品编码")
    private String partyCode;

    @ApiModelProperty(value = "员工手机号")
    private String employeeMobile;

    @ApiModelProperty(value = "商品参数列表")
    private List<ProdParameter> prodParameterList;

    @ApiModelProperty(value = "使用语言 0中文 1中英文")
    private Integer useLang;

    @ApiModelProperty(value = "是否筛选掉活动商品")
    private Integer isActive;

    @ApiModelProperty("活动id")
    private Long groupActivityId;
}
