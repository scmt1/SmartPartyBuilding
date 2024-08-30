/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author lth
 * @Date 2021/8/3 14:33
 */
@Data
public class ShopDetailVO {
    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺类型1自营店 2普通店")
    private Integer type;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺简介")
    private String intro;

    @ApiModelProperty("接收短信号码")
    private String noticeMobile;

    @ApiModelProperty("店铺logo(可修改)")
    private String shopLogo;

    @ApiModelProperty("店铺状态(-1:已删除 0: 停业中 1:营业中 2:平台下线 3:开店申请待审核 4:店铺申请中 5:上线申请待审核)")
    private Integer shopStatus;

    @ApiModelProperty("是否优选好店 1.是 0.不是")
    private Integer isPreferred;

    @ApiModelProperty("店铺收藏数量")
    private Long collectionNum;

    @ApiModelProperty("移动端背景图")
    private String mobileBackgroundPic;

    @ApiModelProperty("pc背景图")
    private String pcBackgroundPic;

    @ApiModelProperty("联系人姓名")
    private String contactName;

    @ApiModelProperty("联系方式")
    private String contactPhone;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("商家账号")
    private String merchantAccount;

    @ApiModelProperty("账号状态， 1:启用 0:禁用 -1:删除")
    private Integer accountStatus;

    @ApiModelProperty("商家名称")
    private String merchantName;

    @ApiModelProperty("省ID")
    private Long provinceId;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("城市ID")
    private Long cityId;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区ID")
    private Long areaId;

    @ApiModelProperty("区")
    private String area;

    @ApiModelProperty("店铺所在纬度")
    private String shopLat;

    @ApiModelProperty("店铺所在经度")
    private String shopLng;

    @ApiModelProperty("签约起始时间")
    private Date contractStartTime;

    @ApiModelProperty("签约终止时间")
    private Date contractEndTime;
}
