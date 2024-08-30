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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Yami
 */
@Data
@TableName("tz_prod_prop_value")
public class ProdPropValue implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 6604406039354172708L;

    /**
     * 属性值ID
     */
    @TableId
    @ApiModelProperty(value = "属性值ID", required = true)
    private Long valueId;

    /**
     * 属性值名称
     */
    @Size(message = "属性值名称不能超过20个字")
    @ApiModelProperty(value = "属性值名称不能超过20个字")
    private String propValue;
    /**
     * 属性值英文名称
     */
    @TableField(exist=false)
    @ApiModelProperty(value = "属性值英文名称")
    private String propValueEn;

    /**
     * 属性ID
     */
    @ApiModelProperty(value = "属性ID")
    private Long propId;

}
