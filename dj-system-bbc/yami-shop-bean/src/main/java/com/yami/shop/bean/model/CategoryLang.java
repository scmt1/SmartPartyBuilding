/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 */
/**
 * @author Yami
 */
@Data
@TableName("tz_category_lang")
public class CategoryLang implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private Long categoryId;
    /**
     * 语言
     */
    private Integer lang;
    /**
     * 分类名称
     */
    private String categoryName;

}
