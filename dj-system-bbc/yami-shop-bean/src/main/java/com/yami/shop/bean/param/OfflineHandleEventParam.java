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
 * 下线活动时间参数
 */
/**
 * @author Yami
 */
@Data
public class OfflineHandleEventParam {
    /**
     * 对象id
     */
    private Long handleId;

    /**
     * 申请理由
     */
    private String reapplyReason;
}
