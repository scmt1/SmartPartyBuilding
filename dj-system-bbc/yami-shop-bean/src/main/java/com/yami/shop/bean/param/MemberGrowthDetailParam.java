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

/**
 * @author Yami
 */
@Data
public class MemberGrowthDetailParam {

    @ApiModelProperty("等级所在人数")
    private Integer memberNum;

    @ApiModelProperty("会员等级")
    private Integer level;

    @ApiModelProperty("占比")
    private Double rate;

}
