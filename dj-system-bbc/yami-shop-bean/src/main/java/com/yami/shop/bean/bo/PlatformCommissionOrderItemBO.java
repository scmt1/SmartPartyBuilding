/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.bo;

import lombok.Data;

/**
 * 订单项平台佣金需要的参数
 * @author lhd
 * @date 2021/8/5
 */
@Data
public class PlatformCommissionOrderItemBO {

    private Long shopId;

    private Long categoryId;

    private Double rate;

    private Long skuId;
}

