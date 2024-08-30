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

import com.yami.shop.bean.dto.ShopUserRegisterDto;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 新增店铺或者初始化一个店铺的时候，新增一个店铺超级管理员
 */
/**
 * @author Yami
 */
@Data
@AllArgsConstructor
public class ShopEmployeeSuperAdminEvent {

    private ShopUserRegisterDto shopUserRegisterDto;
}
