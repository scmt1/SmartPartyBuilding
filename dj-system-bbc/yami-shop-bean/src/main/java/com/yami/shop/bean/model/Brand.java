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

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@TableName("tz_brand")
public class Brand implements Serializable {
    /**
     * 主键
     */
    @TableId
    @ApiModelProperty(value = "主键")
    private Long brandId;

    @NotNull(message = "logo图片不能为空")
    @ApiModelProperty("品牌logo图片")
    private String imgUrl;

    @NotNull(message = "首字母不能为空")
    @ApiModelProperty("检索首字母")
    private String firstLetter;

    @NotNull(message = "序号不能为空")
    @ApiModelProperty("排序")
    @Max(value = 32767, message = "排序号最大为32767")
    private Integer seq;

    @ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
    private Integer status;

    @ApiModelProperty("是否置顶 0：不置顶  1：置顶")
    private Integer isTop;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty("品牌名称")
    private String name;

    @TableField(exist = false)
    @ApiModelProperty("品牌描述")
    private String desc;

    @TableField(exist = false)
    @ApiModelProperty("品牌多语言列表")
    private List<BrandLang> brandLangList;

    @TableField(exist = false)
    @ApiModelProperty("分类id")
    private List<Long> categoryIds;

    @TableField(exist = false)
    @ApiModelProperty("分类")
    private List<Category> categories;
}
