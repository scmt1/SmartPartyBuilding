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
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author chiley
 * @date 2022/9/21 10:33
 */
@Data
@TableName("tz_company_auditing")
public class CompanyAuditing {

    /**
     * 工商信息审核id
     */
    @TableId
    @NotNull
    private Long companyAuditingId;

    /**
     * 申请人id
     */
    @ApiModelProperty(value = "申请人id")
    private String userId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 工商信息id
     */
    @ApiModelProperty(value = "工商信息id")
    private Long shopCompanyId;

    /**
     * 审核人id
     */
    @ApiModelProperty(value = "审核人id")
    private Long auditorId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 审核状态 0:未审核 1:已通过 -1:未通过
     */
    @ApiModelProperty(value = "审核状态 0:未审核 1:已通过 -1:未通过")
    @Max(value = 1, message = "最大为1")
    @Min(value = -1, message = "最小为-1")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 申请开始时间
     */
    @ApiModelProperty(value = "申请开始时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 申请结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 店铺名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    /**
     * 店铺logo
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "店铺logo")
    private String shopLogo;

    /**
     * 店铺简介
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "店铺简介")
    private String shopIntro;

    /**
     * 工商信息id
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "工商信息id")
    private ShopCompany shopCompany;
}
