/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 修改用户积分事件
 * @author LHD
 */
@Data
@AllArgsConstructor
public class UpdateUserScoreEvent {

    /**
     * 积分日志类型
     */
    private Integer consumeType;

    /**
     * 积分数额
     */
    private Long score;
    /**
     * 出入类型 0=支出 1=收入
     */
    private Integer ioType;

    /**
     * 订单号
    */
    private String orderNumber;

    /**
     * 描述
    */
    private String remark;

    private String userId;

    /**
     * 用户连续签到天数
     */
    private Integer signDay;
}
