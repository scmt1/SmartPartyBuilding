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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yami
 */
@Data
@TableName("tz_delivery")
public class Delivery implements Serializable {

    @TableId
    @ApiModelProperty("物流id")
    private Long dvyId;

    @ApiModelProperty("物流公司名称")
    private String dvyName;

    @ApiModelProperty("物流公司编号（快递鸟）")
    private String dvyNo;

    @ApiModelProperty("物流公司编号（快递100）")
    private String dvyNoHd;

    @ApiModelProperty("物流公司编号(阿里)")
    private String aliNo;

    @ApiModelProperty("公司主页")
    private String companyHomeUrl;

    @ApiModelProperty("建立时间")
    private Date recTime;

    @ApiModelProperty("修改时间")
    private Date modifyTime;

    @ApiModelProperty("物流查询接口")
    private String queryUrl;

    @ApiModelProperty("状态 1：正常 -1：删除")
    private Integer status;
}
