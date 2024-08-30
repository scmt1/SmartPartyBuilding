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
 * 成长配置信息
 */
/**
 * @author Yami
 */
@Data
public class GrowthParamConfig {

    @ApiModelProperty("成长值获取开关")
    private Boolean shopGrowthSwitch;

    @ApiModelProperty("购买商品每消费{buyPrice}元, 获取一点成长值")
    private Double buyPrice;

    @ApiModelProperty("每完成一笔订单获取{buyOrder}成长值")
    private Double buyOrder;

}
