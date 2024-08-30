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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author SJL
 * @date 2021-02-20 13:25:13
 */
@Data
public class WebConfig implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;
    /**
     * 配置类型（PLATFROM:平台端   MULTISHOP:商家端   PC:PC端   H5:h5端   STATION:自提点端）
     * @see com.yami.shop.bean.enums.WebConfigTypeEnum 配置类型枚举类
     */
    @ApiModelProperty("配置类型")
    private String paramKey;

    @ApiModelProperty("激活（0:否 1:是）")
    private Integer isActivity;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("后台-登录logo")
    private String bsLoginLogoImg;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("后台-登录背景")
    private String bsLoginBgImg;

    @ApiModelProperty("后台-版权声明-中文")
    private String bsCopyrightCn;

    @ApiModelProperty("后台-版权声明-英文")
    private String bsCopyrightEn;

    @ApiModelProperty("后台-标题文本-中文")
    private String bsTitleContentCn;

    @ApiModelProperty("后台-标题文本-英文")
    private String bsTitleContentEn;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("后台-标题图标")
    private String bsTitleImg;

    @ApiModelProperty("后台-菜单展开时的文本-中文")
    private String bsMenuTitleOpenCn;

    @ApiModelProperty("后台-菜单展开时的文本-英文")
    private String bsMenuTitleOpenEn;

    @ApiModelProperty("后台-菜单缩小时的文本-中文")
    private String bsMenuTitleCloseCn;

    @ApiModelProperty("后台-菜单缩小时的文本-英文")
    private String bsMenuTitleCloseEn;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("PC-登录logo")
    private String pcLogoImg;

    @ApiModelProperty("PC-版权声明-中文")
    private String pcCopyrightCn;

    @ApiModelProperty("PC-版权声明-英文")
    private String pcCopyrightEn;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("PC-底部二维码")
    private String pcQrcodeImg;

    @ApiModelProperty("PC-公司全名-中文")
    private String pcCompanyNameCn;

    @ApiModelProperty("PC-公司全名-英文")
    private String pcCompanyNameEn;

    @ApiModelProperty("PC-底部公司地址等信息-中文")
    private String pcCompanyInfoCn;

    @ApiModelProperty("PC-底部公司地址等信息-英文")
    private String pcCompanyInfoEn;

    @ApiModelProperty("PC-网站标题内容-中文")
    private String pcTitleContentCn;

    @ApiModelProperty("PC-网站标题内容-英文")
    private String pcTitleContentEn;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("PC-网站标题图标")
    private String pcTitleImg;

    @ApiModelProperty("PC-公司名简写-中文")
    private String pcCompanyNameShortCn;

    @ApiModelProperty("PC-公司名简写-英文")
    private String pcCompanyNameShortEn;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("PC-公司图文logo（注册、登录、改密时显示）")
    private String pcLogoImgText;

    @ApiModelProperty("PC-导航栏欢迎语-中文")
    private String pcWelcomeCn;

    @ApiModelProperty("PC-导航栏欢迎语-英文")
    private String pcWelcomeEn;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("PC-登录背景")
    private String pcLoginBgImg;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("h5-登录logo")
    private String uniLoginLogoImg;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("Station-自提点登录logo")
    private String stationLoginLogoImg;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty("后台-顶部栏图标")
    private String bsTopBarIcon;

    @ApiModelProperty("h5-标题-中文")
    private String uniTitleContentCn;

    @ApiModelProperty("h5-标题-英文")
    private String uniTitleContentEn;

    @ApiModelProperty("是否开启商家注册协议")
    @TableField(exist = false)
    private Boolean merchantRegisterProtocolSwitch;
}
