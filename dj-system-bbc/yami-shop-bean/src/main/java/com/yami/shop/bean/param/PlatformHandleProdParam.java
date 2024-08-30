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
import lombok.EqualsAndHashCode;

/**
 * 平台处理商家商品
 */
/**
 * @author Yami
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlatformHandleProdParam extends ProductParam {
    /**
     * 下线原因
     */
    private String offlineReason;
}
