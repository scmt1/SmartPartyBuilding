/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 自提点
 *
 * @author LGH
 * @date 2020-04-23 15:18:29
 */
@Data
public class StationDto{
    @ApiModelProperty(value = "自提点id")
    private Long stationId;

    @ApiModelProperty(value = "关联店铺")
    private Long shopId;

    @ApiModelProperty(value = "自提点名称")
    private String stationName;

    @ApiModelProperty(value = "自提点图片")
    private String pic;

    @ApiModelProperty(value = "电话区号")
    private String phonePrefix;

    @ApiModelProperty(value = "手机/电话号码")
    private String phone;

    @ApiModelProperty(value = "0:关闭 1:营业 2:强制关闭 3:审核中 4:审核失败")
    private Integer status;

    @ApiModelProperty(value = "省id")
    private Long provinceId;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市id")
    private Long cityId;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区id")
    private Long areaId;

    @ApiModelProperty(value = "区")
    private String area;

    @ApiModelProperty(value = "地址")
    private String addr;

    @ApiModelProperty(value = "经度")
    private Double lng;

    @ApiModelProperty(value = "纬度")
    private Double lat;

    @ApiModelProperty(value = "时间日期数据")
    private String timeDate;

    @ApiModelProperty(value = "销售数据")
    private List<StationSalesDto> stationSalesDtoList;

}
