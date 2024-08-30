/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 虚拟商品留言信息
 * @author lhd
 */
@Data
public class VirtualRemarkVO {
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long prodId;
    /**
     * 留言标题
     */
    @ApiModelProperty(value = "留言标题", required = true)
    private String name;
    /**
     * 留言内容
     */
    @ApiModelProperty(value = "留言内容", required = true)
    private String value;

    /**
     * 是否必填
     */
    @ApiModelProperty(value = "是否必填", required = true)
    private Boolean isRequired;

}
