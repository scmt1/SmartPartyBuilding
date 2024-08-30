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

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  用户没有权限的错误处理事件
 * @author lhd
 */
@Data
@AllArgsConstructor
public class PermissionErrorHandleEvent {
    private String perms;
    private Long shopId;
}
