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
public class ScoreExpireParam {

    @ApiModelProperty("过期年限")
    private Integer expireYear;

    @ApiModelProperty("积分过期开关")
    private Boolean scoreExpireSwitch;


}
