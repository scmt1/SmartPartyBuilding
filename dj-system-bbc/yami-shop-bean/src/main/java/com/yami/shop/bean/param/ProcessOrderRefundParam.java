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

import javax.validation.constraints.NotNull;

/**
 * @author Yami
 */
@Data
public class ProcessOrderRefundParam {
    /**
     * 记录id
     */
    @NotNull(message = "退款记录id不能为空")
    private Long refundId;

    /**
     * 处理状态:(1为待审核,2为同意,3为不同意)
     */
    @NotNull(message = "处理状态不能为空")
    private Integer refundSts;

    /**
     * 拒绝原因
     */
    private String rejectMessage;

}
