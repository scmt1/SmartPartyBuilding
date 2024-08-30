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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author Yami
 */
@Data
public class FormItem{

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Integer id;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private String value;

    /**
     * 选择
     */
    @ApiModelProperty(value = "是否选择")
    private Boolean select = false;

}
