/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @date 2019/9/23 19:01
 */
@Data
@ApiModel("店铺审核列表参数")
public class AuditingInfoParam implements Serializable {

    @ApiModelProperty(value = "审核id)")
    private Long shopAuditingId;

    @ApiModelProperty(value = "审核状态(0 未审核 1已通过 -1未通过)")
    private Integer status;

    @ApiModelProperty(value = "店铺名字")
    private String shopName;

    @ApiModelProperty(value = "用户名字")
    private String userName;

    @ApiModelProperty(value = "店铺简介")
    private String intro;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "店铺状态")
    private Integer shopStatus;

    @ApiModelProperty(value = "店铺状态数组")
    private List<Integer> shopStatusList;

    @ApiModelProperty(value = "单位电话")
    private String mobile;

    @ApiModelProperty("0普通店铺 1优选好店")
    private Integer type;

    @ApiModelProperty("商家手机号")
    private String merchantMobile;

    @ApiModelProperty("选中的店铺id")
    private List<Long> shopIds;

}
