/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author FrozenWatermelon
 */
@Data
public class GroupActivitySearchVO {

    @ApiModelProperty("店铺类型1自营店 2普通店")
    private Long groupActivityId;

    @ApiModelProperty(value = "成团人数")
    private Integer groupNumber;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty("活动创建时间")
    private Date createTime;
}
