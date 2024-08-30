/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 *
 *
 * @author YXF
 * @date 2020-07-17 09:50:13
 */
@Data
public class FlowUserAnalysisDto{
    /**
     * 省Id
     */
    private Long provinceId;
    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 浏览量
     */
    private Integer visitNums = 0;
    /**
     * 访客数
     */
    private Integer visitUser = 0;
    /**
     * 停留时长
     */
    private Long stopTime = 0L;
    /**
     * 商品浏览量
     */
    private Integer prodVisitNums = 0;
    /**
     * 商品访客数
     */
    private Integer prodVisitUser = 0;
    /**
     * 访客数
     */
    private Set<String> visitUserSet = new HashSet<>();
    /**
     * 商品访客数
     */
    private Set<String> prodVisitSet = new HashSet<>();

    /**
     * 浏览商品id集合
     */
    @TableField(exist = false)
    private Set<String> visitProdSet = new HashSet<>();


}
