/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.bo.mq;

import lombok.Data;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/22
 */
@Data
public class UserScoreBO {

    private String userId;

    private List<String> orderNumbers;


}
