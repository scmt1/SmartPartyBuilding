/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.enums;

/**
 * 奖品类型
 *
 */
/**
 * @author Yami
 */
public enum ScoreLogType {

    /**
     * 0.注册送积分
     */
    REGISTER(0),
    /**
     * 1. 购物  2.会员等级提升加积分 3.签到加积分 4购物抵扣使用积分
     */
     SHOP(1),

    /**
     * 等级提升
     */
    LEVEL_UP(2),

    /**
     * 签到
     */
    SIGN_IN(3),
    /**
     * 购物抵扣积分
     */
    SCORE_CASH(4),
    /**
     * 积分过期
     */
    EXPIRE(5),
    /**
     * 余额充值
     */
    BALANCE(6),
    /**
     * 系统更改积分
     */
    SYSTEM(7)
    ;


    private Integer num;

    public Integer value() {
        return num;
    }

    ScoreLogType(Integer num){
        this.num = num;
    }

    public static ScoreLogType instance(Integer value) {
        ScoreLogType[] enums = values();
        for (ScoreLogType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
