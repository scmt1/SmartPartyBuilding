/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo.flow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhd
 */
@Data
public class MemberTrendRespVO {

    @ApiModelProperty("时间，格式例如：20200721")
    private Long currentDay;

    @ApiModelProperty("注册会员数量")
    private Integer memberNum;

    @ApiModelProperty("注册会员数在统计时间类的所有注册会员人数的占比")
    private Double memberNumRate;
}
