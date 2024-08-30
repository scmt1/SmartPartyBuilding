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

import com.yami.shop.bean.model.ShopDetail;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 修改店铺账号，或者联系电话，店铺信息同步到员工表
 * @author Yami
 */
@Data
@AllArgsConstructor
public class UpdateShopAccountEvent {

    private ShopDetail shopDetail;

}
