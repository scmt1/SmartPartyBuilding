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

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author lth
 * @Date 2021/7/30 14:07
 */
@Data
@TableName("tz_shop_company")
public class ShopCompany {

    @TableId
    private Long shopCompanyId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @NotNull
    @ApiModelProperty(value = "统一社会信用代码")
    private String creditCode;

    @ApiModelProperty(value = "企业名称")
    private String firmName;

    @ApiModelProperty(value = "住所")
    private String residence;

    @ApiModelProperty(value = "法定代表人")
    private String representative;

    @ApiModelProperty(value = "注册资本（万元）")
    private Double capital;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "成立日期")
    private Date foundTime;

    @ApiModelProperty(value = "营业起始日期")
    private Date startTime;

    @ApiModelProperty(value = "营业终止日期")
    private Date endTime;

    @ApiModelProperty(value = "经营范围")
    private String businessScope;

    @ApiModelProperty(value = "营业执照")
    private String businessLicense;

    @ApiModelProperty(value = "身份证正面")
    private String identityCardFront;

    @ApiModelProperty(value = "身份证反面")
    private String identityCardLater;

    @ApiModelProperty(value = "工商信息状态：1：已通过 0待审核 -1未通过")
    private Integer status;
}
