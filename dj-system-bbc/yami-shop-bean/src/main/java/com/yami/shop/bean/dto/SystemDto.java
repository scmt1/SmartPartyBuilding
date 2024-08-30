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

/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2020-07-14 11:26:35
 */
@Data
public class SystemDto {
    private Integer userNum;
    private Integer systemType;
    /**
     * 全部
     */
    @TableField(exist = false)
    private Integer all;
    /**
     * pc
     */
    @TableField(exist = false)
    private Integer pc;
    /**
     * h5
     */
    @TableField(exist = false)
    private Integer h5;
    /**
     * 小程序
     */
    @TableField(exist = false)
    private Integer applets;
    /**
     * 安卓
     */
    @TableField(exist = false)
    private Integer android;
    /**
     * ios
     */
    @TableField(exist = false)
    private Integer ios;

    public SystemDto(){
        pc = 0;
        h5 = 0;
        applets = 0;
        android = 0;
        ios = 0;
    }
}
