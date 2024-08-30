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
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户地址管理
 *
 * @author hzm
 * @date 2019-04-15 10:49:40
 */
@Data
@TableName("tz_user_addr")
@EqualsAndHashCode(callSuper = false)
public class UserAddr implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty("地址id")
    private Long addrId;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("收货人")
    private String receiver;

    @ApiModelProperty("省ID")
    private Long provinceId;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("城市ID")
    private Long cityId;

    @ApiModelProperty("区")
    private String area;

    @ApiModelProperty("区ID")
    private Long areaId;

    @ApiModelProperty("邮编")
    private String postCode;

    @ApiModelProperty("地址")
    private String addr;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("状态,1正常，0无效")
    private Integer status;

    @ApiModelProperty("是否默认地址 1是")
    private Integer commonAddr;

    @ApiModelProperty("建立时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer version;

    @ApiModelProperty("经度")
    private double lng;

    @ApiModelProperty("纬度")
    private double lat;

    @ApiModelProperty("更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
