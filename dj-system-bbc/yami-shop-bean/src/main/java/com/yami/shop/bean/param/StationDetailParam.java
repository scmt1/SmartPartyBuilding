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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 自提点
 *
 * @author LGH
 * @date 2020-04-23 15:18:29
 */
@Data
public class StationDetailParam implements Serializable{
    /**
     * 自提点id
     */
    @ApiModelProperty(value = "自提点id")
    private Long stationId;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String userName;
    /**
     * 自提点名称
     */
    @ApiModelProperty(value = "自提点名称")
    private String stationName;
    /**
     * 自提点图片
     */
    @ApiModelProperty(value = "自提点图片")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String images;

    /**
     * 是否常用客服中心 1是 0不是
     */
    @ApiModelProperty(value = "是否常用客服中心 1是 0不是")
    private Integer defaultStation = 0;
    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String province;
    /**
     * 省id

     */
    @ApiModelProperty(value = "省id")
    private Long provinceId;
    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;
    /**
     * 市id
     */
    @ApiModelProperty(value = "市id")
    private Long cityId;
    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String area;
    /**
     * 区id
     */
    @ApiModelProperty(value = "区id")
    private Long areaId;
    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String addr;
    /**
     * 电话区号
     */
    @ApiModelProperty(value = "电话区号")
    private String phonePrefix;
    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String mobile;
    /**
     * 店铺所在纬度
     */
    @ApiModelProperty(value = "店铺所在纬度")
    private Double lat;
    /**
     * 店铺所在经度
     */
    @ApiModelProperty(value = "店铺所在经度")
    private Double lng;
    /**
     * 距离
     */
    @ApiModelProperty(value = "距离")
    private double distance;
    /**
     * 是否默认自提点 1 默认  0 非默认
     */
    @ApiModelProperty(value = "是否默认自提点 1 默认  0 非默认")
    private String commonAddr;

    /**
     * 用户自提点id
     */
    @ApiModelProperty(value = "用户自提点id")
    private Long id;

    /**
     * 时间日期数据
     */
    private String timeDate;

    /**
     * 营业时间
     */
    @ApiModelProperty(value = "营业时间")
    private String business;
    /**
     * 时间日期数据
     */
    private List<TimeParam> timeParams;

    @Data
    public static class TimeDateModeVO {
        /**
         * 营业时间
         */
        private Long[] shopTime;
        /**
         * 时段间隔 1:天 2:上下午晚上（12:00和18:00为分界点）  3:小时  4:半小时
         */
        private Integer interval;
        /**
         * 自提开始时间
         */
        private String stationStartTime;
        /**
         * 自提结束时间
         */
        private String stationEndTime;
    }

}
