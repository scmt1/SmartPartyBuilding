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
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@TableName("tz_form")
public class Form implements Serializable {
    private static final long serialVersionUID = -3468251351681518798L;
    /**
     * 主键
     */
    @TableId
    @ApiModelProperty("报表id")
    private Long formId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("报表名称")
    private String formName;

    @ApiModelProperty("推荐报表介绍内容")
    private String content;

    @ApiModelProperty("报表类型 1:店铺报表")
    private Integer fromType;

    @ApiModelProperty("时间周期 1:天 2:周 3:月")
    private Integer timeType;

    @ApiModelProperty("时间格式 1:自定义时间 2:指定时间范围")
    private Integer timeFormat;

    @ApiModelProperty("报表选择指标")
    private String formItemIds;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty("时间范围")
    private Integer timeRamge;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("序号")
    private Integer seq;

    @TableField(exist = false)
    @ApiModelProperty("推荐报表 true：推荐报表  false：店铺报表")
    private Boolean recommendForm;

    @TableField(exist = false)
    @ApiModelProperty("报表项列表")
    private List<FormItem> formItemList;


}
