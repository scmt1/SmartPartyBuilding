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
public class CategoryScoreConfigParam {


    /**
     * 分类id
     */
    private Long categoryId;


    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类英文名称
     */
    private String categoryNameEn;

    /**
     * 使用上限比例
     */
    private Double useScoreLimit;
    /**
     * 获取上限比例
     */
    private Double getScoreLimit;

}
