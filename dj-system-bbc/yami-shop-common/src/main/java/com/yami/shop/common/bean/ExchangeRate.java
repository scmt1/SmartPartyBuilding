/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 汇率： 本系统货币 和其他币种的兑换比例
 * @author yami
 */
@Data
public class ExchangeRate {

    /** 本系统 1 */
    private BigDecimal currencyLocal;

    /** 美元 */
    private BigDecimal currencyUsd;

    private BigDecimal zero = new BigDecimal("0.0");

    /** 获取美元的汇率 */
    public BigDecimal getUsdExchangeRate() {
        return div(this.currencyUsd,this.currencyLocal,4);
    }

    public BigDecimal getCurExchangeRate() {
        return div(this.currencyLocal, this.currencyUsd, 4);
    }

    private BigDecimal div(BigDecimal v1, BigDecimal v2,int scale) {
        if (Objects.isNull(v1) || Objects.isNull(v2)) {
            return this.zero;
        }
        if (v1.compareTo(this.zero) <=0 || v2.compareTo(this.zero) <= 0) {
            return this.zero;
        }
        return v1.divide(v2,scale, RoundingMode.HALF_EVEN);
    }

}
