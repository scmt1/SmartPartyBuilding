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
import com.yami.shop.bean.enums.EsOperationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 *  es商品事件， 获取商品信息
 * @author lhd
 */
@Data
@AllArgsConstructor
public class EsProductUpdateEvent {
    /**
     * 单条数据操作字段 (商品id、分类id、店铺id)
     */
    private Long id;
    /**
     *  批量操作使用字段
     */
    private List<Long> ids;
    /**
     * 操作类型（保存、更新、删除等）
     */
    private EsOperationType esOperationType;
}
