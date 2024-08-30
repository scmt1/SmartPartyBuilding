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

import java.util.List;

/**
 * @author Yami
 */
@Data
public class CustomerRetainedListParam {

    @ApiModelProperty("年月份 例如：20190701")
    private Long currentDay;

    @ApiModelProperty("后几个月的留存率")
    private List<CycleRetained> cycleRetainedList;

    @ApiModelProperty("暂时没用字段")
    private List<CycleRetained> cycleRetainedListIterator;

    @ApiModelProperty("留存人数")
    private Integer newCustomerCount;
    /**
     * 留存集合大小
     */
    @ApiModelProperty("留存集合大小")
    private Integer cycleRetainedListSize = 0;

}
