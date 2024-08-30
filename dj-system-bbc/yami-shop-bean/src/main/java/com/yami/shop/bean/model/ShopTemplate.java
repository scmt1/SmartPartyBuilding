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

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author LGH
 * @date 2022-07-29 16:54:02
 */
@Data
@TableName("tz_shop_template")
public class ShopTemplate implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    private Long templateId;
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "装修内容")
    private String content;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "图片")
    private String image;
    @ApiModelProperty(value = "1PC端 2移动端")
    private Integer type;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
