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
 * 会员活跃参数
 */
/**
 * @author Yami
 */
@Data
public class CustomerMemberLivelyParam {

    /**
     * 访问会员数：筛选时间内，访问过店铺的会员数量,一人多次访问记为一人
     * 访问会员数占比： 筛选时间内，访问会员数 / 累积会员数
     */
    private Integer memberV = 0;
    private Double memberVRate = 0.0;
    /**
     * 领券会员数：筛选时间内，领取了优惠券的会员数，一人多次领券记为一人
     * 领取会员数占比： 筛选时间内，领券会员数 / 访问会员数
     */
    private Integer memberGetCoupon = 0;
    private Double memberGetCouponRate = 0.0;
    /**
     * 加购会员数：筛选时间内，将商品添加购物车的会员数，一人多次添加购物车记为一人
     * 加购会员数占比： 筛选时间内，加购会员数 / 访问会员数
     */
    private Integer memberAddCat = 0;
    private Double memberAddCatRate = 0.0;
    /**
     * 成交会员数：筛选时间内，付款成功的会员数，一人多次付款成功记为一人
     * 成交会员数占比： 筛选时间内，成交会员数 / 访问会员数
     */
   private Integer memberPay = 0;
   private Double memberPayRate = 0.0;

   List<CustomerMemberLivelyTrendParam> memberLivelyTrend;

}
