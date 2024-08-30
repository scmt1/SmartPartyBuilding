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
public class CustomerMemberRespParam {
    /**
     * 会员统计
     */
    private CustomerMemberParam memberCount;
    /**
     * 会员活跃
     */
    private CustomerMemberLivelyParam memberLively;
}
