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
 * @date 2021-02-24 17:02:07
 */
/**
 * @author Yami
 */
@Data
@TableName("tz_prod_prop_lang")
public class ProdPropLang implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private Long propId;

    private Integer lang;
    /**
     * 属性名称
     */
    private String propName;

}
