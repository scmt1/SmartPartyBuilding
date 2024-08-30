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
 * 用户注销账户时删除相关数据
 *
 * @author chiley
 * @date 2022/9/8 11:11
 */
@Data
@AllArgsConstructor
public class UserDestroyEvent {

    private String userId;
}
