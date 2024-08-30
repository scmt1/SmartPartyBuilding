/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.event;

import com.yami.shop.bean.enums.ScoreLogType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  积分订单事件如 积分购买商城的积分商品、积分购买优惠券
 */
/**
 * @author Yami
 */
@Data
@AllArgsConstructor
public class ScoreOrderEvent {


    /**
     * 源，如果是订单则是订单号，如果是优惠券则是优惠券id
     */
    private Object source;

    /**
     * 积分改变原因
     */
    private ScoreLogType scoreLogType;

    /**
     * 积分价格
     */
    private Integer score;

}
