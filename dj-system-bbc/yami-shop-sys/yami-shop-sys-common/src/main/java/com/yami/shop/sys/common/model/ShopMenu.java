/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 店铺菜单管理
 * @author yami
 * @date 2021-03-01 17:42:09
 */
@Data
@TableName("tz_shop_menu")
public class ShopMenu implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "菜单id")
    private Long menuId;
    /**
     * 父菜单ID，一级菜单为0
     */
    @NotNull(message="上级菜单不能为空")
    @ApiModelProperty(value = "父菜单ID，一级菜单为0")
    private Long parentId;

    /**
     * 菜单名称
     *
     */
    @ApiModelProperty(value = "菜单名称")
    @NotBlank(message="菜单名称不能为空")
    private String name;
    /**
     * 菜单URL
     */
    @ApiModelProperty(value = "菜单URL")
    private String url;
    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @ApiModelProperty(value = "授权(多个用逗号分隔，如：user:list,user:create)")
    private String perms;
    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    @ApiModelProperty(value = "类型   0：目录   1：菜单   2：按钮")
    private Integer type;
    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String icon;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    /**
     * 菜单英文名称
     */
    @ApiModelProperty(value = "菜单英文名称")
    private String nameEn;

    /**
     * 是否隐藏 当设置 true 的时候该路由不会在侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
     */
    @ApiModelProperty(value = "是否隐藏 当设置 true 的时候该路由不会在侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1")
    @NotNull(message="是否隐藏属性不能为空")
    private Integer hidden;

    /**
     * 父菜单名称
     */
    @ApiModelProperty(value = "父菜单名称")
    @TableField(exist=false)
    private String parentName;

    @TableField(exist=false)
    private List<?> list;
}
