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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品信息
 *
 * @author YXF
 * @date 2020-12-23 15:27:24
 */
@Data
public class EsProductParam {

    @ApiModelProperty(value = "商品id")
    private Long prodId;

    @ApiModelProperty(value = "页面传递过来的全文匹配关键字")
    private String keyword;

    @ApiModelProperty(value = "商品类型(0普通商品 1拼团 2秒杀 3积分 4套餐 5活动)")
    private Integer prodType;

    @ApiModelProperty(value = "商品类别 0.实物商品 1. 虚拟商品")
    private Integer mold;

    @ApiModelProperty(value = "是否筛选掉活动商品")
    private Integer isActive;

    @ApiModelProperty(value = "预售状态 1：开启 0：未开启")
    private Integer preSellStatus;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "店铺类型1自营店 2普通店")
    private Integer shopType;

    @ApiModelProperty(value = "商品状态")
    private Integer status;

    @ApiModelProperty(value = "是否有库存")
    private Boolean hasStock;

    @ApiModelProperty(value = "库存")
    private Integer totalStocks;

    @ApiModelProperty(value = "销量")
    private Integer soldNum;

    @ApiModelProperty(value = "实际销量")
    private Integer actualSoldNum;

    @ApiModelProperty(value = "注水销量")
    private Integer waterSoldNum;

    @ApiModelProperty(value = "评论数")
    private Integer commentNum;

    @ApiModelProperty(value = "商品创建时间")
    private Long createTime;

    @ApiModelProperty(value = "商品更新时间")
    private Long updateTime;

    @ApiModelProperty(value = "活动开始时间")
    private Long activityStartTime;

    @ApiModelProperty(value = "品牌id列表（多个id使用逗号拼接）")
    private String brandIds;

    @ApiModelProperty(value = "是否置顶")
    private Integer isTop;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "平台一级分类id")
    private Long primaryCategoryId;

    @ApiModelProperty(value = "平台二级分类id")
    private Long secondaryCategoryId;

    @ApiModelProperty(value = "平台三级分类id")
    private Long categoryId;

    @ApiModelProperty(value = "店铺分类id")
    private Long shopCategoryId;

    @ApiModelProperty(value = "价格区间查询-最低价")
    private Long minPrice;

    @ApiModelProperty(value = "价格区间查询-最高价")
    private Long maxPrice;

    @ApiModelProperty(value = "销量区间查询-最低销量")
    private Long minSaleNum;

    @ApiModelProperty(value = "销量区间查询-最高销量")
    private Long maxSaleNum;

    @ApiModelProperty(value = "spuId列表")
    private List<Long> prodIds;

    @ApiModelProperty(value = "不为该spuId列表")
    private List<Long> spuIdsExclude;

    @ApiModelProperty(value = "是否需要活动信息  1:需要  0:不需要")
    private Integer needActivity;

    @ApiModelProperty(value = "配送方式")
    private Integer deliveryMode;

    /**
     * 参考EsProductSortEnum
     */
    @ApiModelProperty(value = "排序：0创建时间正序 1创建时间倒序,2销量倒序,3销量正序,4商品价格倒序,5商品价格正序,7市场价倒序," +
            "8市场价正序,10商品库存倒序,11商品库存正序,12商品序号倒序,13商品序号正序,14评论数量倒序,15评论数量正序,16根据置顶状态排序," +
            "17实际销量倒序,18实际销量正序,19注水销量倒序,20注水销量正序,21发布时间倒序,22发布时间正序")
    private Integer sort;

    /**
     * 用户端显示该商品(0:不显示， 1：显示)
     */
    private Boolean appDisplay;

    @ApiModelProperty(value = "不匹配的商品类型(0普通商品 1拼团 2秒杀 3积分 4套餐 5活动)")
    private List<Integer> mustNotProdTypes;

    /**
     * 不匹配的平台一级分类id
     */
    private Long notPrimaryCategoryId;

    /**
     * 响应数据字段数组
     */
    private String[] fetchSource;

    @ApiModelProperty(value = "是否获取删除商品，装修+商品es定时任务使用")
    private Boolean getDelete;

}
