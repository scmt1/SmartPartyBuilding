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
 * @author Yami
 */
@Data
public class CustomerMemberParam {

    /**
     * 累积会员数
     */
    private Integer accumulate = 0;
    private Double accumulateRate = 0.0;
    /**
     * 新增会员数
     */
    private Integer newAddMember = 0;
    private Double newAddMemberRate = 0.0;
    /**
     * 升级会员数
     */
    private Integer growthMember = 0;
    private Double growthMemberRate = 0.0;
    /**
     * 付费会员数
     */
    private Integer payMember = 0;
    private Double payMemberRate;

    /**
     * 商城暂无储值设计
     * 储值会员数, 暂不计算
     */
    private Integer storedValue = 0;
    private Double storedValueRate = 0.0;


    private List<CustomerMemberTrendParam> list;

}
