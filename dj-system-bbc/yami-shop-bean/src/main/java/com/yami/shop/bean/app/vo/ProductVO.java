/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.bean.app.dto.SkuDto;
import com.yami.shop.bean.model.ProdParameter;
import com.yami.shop.bean.model.Product;
import com.yami.shop.bean.param.LiveRoomParam;
import com.yami.shop.bean.vo.ComboVO;
import com.yami.shop.bean.vo.GiveawayVO;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@ApiModel("商品")
public class ProductVO {

    @ApiModelProperty(value = "店铺ID", required = true)
    private Long shopId;

    @ApiModelProperty(value = "商品ID", required = true)
    private Long prodId;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @ApiModelProperty(value = "商品价格", required = true)
    private Double price;

    @ApiModelProperty(value = "详细描述")
    private String content;

    @ApiModelProperty(value = "商品原价", required = true)
    private Double oriPrice;

    @ApiModelProperty(value = "注水销量")
    private Integer waterSoldNum;

    @ApiModelProperty(value = "库存量", required = true)
    private Integer totalStocks;

    @ApiModelProperty(value = "销量", required = true)
    private Integer soldNum;

    @ApiModelProperty(value = "简要描述,卖点等", required = true)
    private String brief;

    @ApiModelProperty(value = "0:下架、1:上架", required = true)
    private Integer status;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "商品视频")
    private String video;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "商品主图", required = true)
    private String pic;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "商品图片列表，以逗号分割", required = true)
    private String imgs;

    @ApiModelProperty(value = "预售状态 1：开启 0：未开启")
    private Integer preSellStatus;

    @ApiModelProperty(value = "预售发货时间")
    private Date preSellTime;

    @ApiModelProperty(value = "商品分类id", required = true)
    private Long categoryId;

    @ApiModelProperty(value = "sku列表")
    private List<SkuDto> skuList;

    @ApiModelProperty(value = "商品类型(0普通商品 1拼团 2秒杀 3积分 5活动)")
    private Integer prodType;

    @ApiModelProperty(value = "商品积分价格")
    private Long scorePrice;

    @ApiModelProperty(value = "活动id(prodType)")
    private Long activityId;

    @ApiModelProperty(value = "活动参考价", required = true)
    private Double activityPrice;

    @ApiModelProperty(value = "同城配送起送费", required = true)
    private Double startDeliveryFee;

    @ApiModelProperty(value = "配送方式", required = true)
    private Product.DeliveryModeVO deliveryModeVO;

    @ApiModelProperty(value = "商品直播间列表", required = true)
    private List<LiveRoomParam> liveRoomParams;

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

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "商品参数列表")
    private List<ProdParameter> prodParameterList;

    @ApiModelProperty(value = "商品套餐列表")
    private List<ComboVO> comboList;

    @ApiModelProperty(value = "商品赠品")
    private GiveawayVO giveaway;

    @ApiModelProperty(value = "秒杀信息")
    private SeckillVO seckillVO;

    @ApiModelProperty(value = "团购信息")
    private GroupActivityVO groupActivityVO;

}
