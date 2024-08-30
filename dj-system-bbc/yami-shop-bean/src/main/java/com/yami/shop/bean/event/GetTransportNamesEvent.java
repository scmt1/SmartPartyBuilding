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

import com.yami.shop.bean.param.TransportParam;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 获取所有的运费模板名称
 * @author LHD
 */
@Data
@AllArgsConstructor
public class GetTransportNamesEvent {

    /**
     * 运费模板集合
     */
    private List<TransportParam> transportParams;

    /**
     * 店铺id
     */
    private Long shopId;

}
