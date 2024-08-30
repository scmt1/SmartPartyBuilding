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
public class CustomerMemberTrendParam {

    private String currentDay;

    /**
     * 累积会员数
     */
    private Integer accumulate = 0;
    /**
     * 新增会员数
     */
    private Integer newAddMember = 0;
    /**
     * 升级会员数
     */
    private Integer growthMember = 0;
    /**
     * 付费会员数
     */
    private Integer payMember = 0;
    /**
     * 储值会员数, 暂不计算
     */
    private Integer storedValue = 0;

}
