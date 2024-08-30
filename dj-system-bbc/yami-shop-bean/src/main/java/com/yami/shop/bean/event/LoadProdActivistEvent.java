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

import com.yami.shop.bean.app.vo.ProductVO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 加载商品活动信息（直播、套餐）
 * @author LHD
 */
@Data
@AllArgsConstructor
public class LoadProdActivistEvent {

    /**
     * 商品id
     */
    private Long prodId;

    /**
     * 商品信息
     */
    private ProductVO productVO;

    /**
     * 商品类型
     */
    private Integer prodType;

}
