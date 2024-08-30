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

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yami
 */
@Data
@TableName("tz_user_addr_order")
public class UserAddrOrder implements Serializable {

    @TableId
    @ApiModelProperty("ID")
    private Long addrOrderId;

    @ApiModelProperty("地址ID")
    private Long addrId;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("收货人")
    private String receiver;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区")
    private String area;

    @ApiModelProperty("地址")
    private String addr;

    @ApiModelProperty("邮编")
    private String postCode;

    @ApiModelProperty("省ID")
    private Long provinceId;

    @ApiModelProperty("城市ID")
    private Long cityId;

    @ApiModelProperty("区域ID")
    private Long areaId;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("建立时间")
    private Date createTime;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("经度")
    @TableField(exist = false)
    private double lng;

    @ApiModelProperty("纬度")
    @TableField(exist = false)
    private double lat;

}
