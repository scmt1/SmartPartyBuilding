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
 * 自提点
 *
 * @author LGH
 * @date 2020-04-23 15:18:29
 */
@Data
@TableName("tz_station")
public class Station implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("自提点id")
    private Long stationId;

    @ApiModelProperty("关联店铺")
    private Long shopId;

    @ApiModelProperty("自提点名称")
    private String stationName;

    @ApiModelProperty("自提点图片")
    private String pic;

    @ApiModelProperty("电话区号")
    private String phonePrefix;

    @ApiModelProperty("手机/电话号码")
    private String phone;

    @ApiModelProperty("自提点状态 0:关闭 1:营业 2:强制关闭 3:审核中 4:审核失败")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("省id")
    private Long provinceId;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市id")
    private Long cityId;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区id")
    private Long areaId;

    @ApiModelProperty("区")
    private String area;

    @ApiModelProperty("地址")
    private String addr;

    @ApiModelProperty("经度")
    private Double lng;

    @ApiModelProperty("纬度")
    private Double lat;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("时间日期数据")
    private String timeDate;

    @TableField(exist = false)
    @ApiModelProperty("店铺名称")
    private String shopName;

    @Data
    public static class TimeDateModeVO {

        @ApiModelProperty("营业时间 shopTime[营业开始时间，营业结束时间]")
        private String[] shopTime;

        @ApiModelProperty("时段间隔 1:天 2:上下午晚上（12:00和18:00为分界点）  3:小时  4:半小时")
        private Integer interval;

        @ApiModelProperty("自提开始时间")
        private String stationStartTime;

        @ApiModelProperty("自提结束时间")
        private String stationEndTime;
    }

}
