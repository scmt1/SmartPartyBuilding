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

import lombok.Data;

import java.util.List;

/**
 * 商品参数
 * @author LGH
 */
@Data
public class ProductTagParam {

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 语言
     */
    private Integer lang;
    /**
     * 商品价格
     */
    private Double price;


    private String pic;

    /**
     * 状态
     */
    private Integer status;
    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 商品类型(0普通商品 1拼团 2秒杀 3积分)
     */
    private Integer prodType;

    /**
     * 商品店铺分类
     */
    private Long shopCategoryId;

    /**
     * 商品分类
     */
    private Long categoryId;

    /**
     * 分组id
     */
    private Long id;

    /**
     * 分组商品关联id
     */
    private Long referenceId;
    /**
     * 排序
     */
    private Integer seq;
    /**
     * 商品信息
     */
    private List<ProductParam> products;
}
