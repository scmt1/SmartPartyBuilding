/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.wx.bean.resp;

import lombok.Data;

/**
 * 微信小程序直播响应参数
 * @author yami
 */
@Data
public class GoodsInfoRespParam {
    /**
     *     商品ID
     *     "goodsId":"5","auditId":430376413,"errcode":0
     *     {"goodsId":"8","auditId":430376427,"errcode":0}
     */
    private Integer goodsId;
    /**
     * 审核单ID
     */
    private Integer auditId;

    /**
     * 商品图片链接
     */
    private String coverImgUrl;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 1:一口价，此时读price字段; 2:价格区间，此时price字段为左边界，price2字段为右边界; 3:折扣价，此时price字段为原价，price2字段为现价；
     */
    private Integer priceType;

    /**
     * 价格左区间，单位“元”
     */
    private Double price;

    /**
     * 价格右区间，单位“元”
     */
    private Double price2;

    /**
     * 商品小程序路径
     */
    private String url;
    /**
     * 1、2：表示是为 API 添加商品，否则是直播控制台添加的商品
     */
    private Integer thirdPartyTag;

    private Integer errcode;

    /**
     * 0：未审核，1：审核中，2:审核通过，3审核失败
     */
    private Integer auditStatus;

}
