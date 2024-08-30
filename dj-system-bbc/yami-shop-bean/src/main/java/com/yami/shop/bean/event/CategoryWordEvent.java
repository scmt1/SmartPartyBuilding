package com.yami.shop.bean.event;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


/**
 * 分类下架的事件信息
 */
@Data
@AllArgsConstructor
public class CategoryWordEvent {

    /**
     * 商品id集合
     */
    private List<Long> prodIdList;
}
