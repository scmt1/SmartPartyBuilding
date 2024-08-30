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
public class SendUserCouponsParam {
    /**
     * 发送的优惠券id
     */
    private Long couponId;
    /**
     * 发放给用户优惠券的数量
     */
    private Integer nums;

}
