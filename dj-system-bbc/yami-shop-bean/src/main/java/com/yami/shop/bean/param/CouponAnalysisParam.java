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

/**
 * @author Yami
 */
@Data
public class CouponAnalysisParam {

    /**
     * 天数
     */
    private String currentDay;
    /**
     * 领取次数
     */
    private Integer takeNum = 0;
    /**
     * 验证次数
     */
    private Integer verifyNum = 0;
    /**
     * 微商城使用次数
     */
    private Integer useNum = 0;
    /**
     * 分享次数
     */
    private Integer shareNum = 0;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠类型 1:代金券 2:折扣券 3:兑换券
     */
    private Integer couponType;

    /**
     * 优惠券id
     */
    private Long couponId;



}
