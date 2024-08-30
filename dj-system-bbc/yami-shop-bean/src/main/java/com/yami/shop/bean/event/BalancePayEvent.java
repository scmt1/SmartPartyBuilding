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

import com.yami.shop.bean.app.param.PayInfoParam;
import com.yami.shop.bean.pay.PayInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  用户升级的事件
 * @author lhd
 */
@Data
@AllArgsConstructor
public class BalancePayEvent {

    private PayInfoDto payInfo;

    private PayInfoParam payInfoParam;
}
