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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author lth
 * @Date 2021/9/2 16:09
 */
@Data
public class ShopExportParam {

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty("商家名称")
    private String merchantName;

    @ApiModelProperty(value = "店铺状态")
    private Integer shopStatus;

    @ApiModelProperty(value = "店铺联系电话")
    private String tel;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "店铺所在省份")
    private String province;

    @ApiModelProperty(value = "店铺所在城市")
    private String city;

    @ApiModelProperty(value = "店铺所在区域")
    private String area;

    @ApiModelProperty(value = "店铺详细地址")
    private String shopAddress;

    @ApiModelProperty(value = "店铺简介")
    private String intro;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "0普通店铺 1优选好店")
    private Integer type;

    @ApiModelProperty(value = "签约起始时间")
    private Date contractStartTime;

    @ApiModelProperty(value = "签约终止时间")
    private Date contractEndTime;

    @ApiModelProperty(value = "经营范围")
    private String businessScope;

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

    @ApiModelProperty(value = "成立日期")
    private Date foundTime;

    @ApiModelProperty(value = "营业起始日期")
    private Date startTime;

    @ApiModelProperty(value = "营业终止日期")
    private Date endTime;
}
