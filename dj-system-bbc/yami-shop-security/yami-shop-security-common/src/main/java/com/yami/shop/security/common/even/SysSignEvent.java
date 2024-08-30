/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.even;

import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/**
 * 签名访问事件
 * @author LHD
 */
@Data
@AllArgsConstructor
public class SysSignEvent {

    /**
     * 积分日志类型
     */
    private UserInfoInTokenBO userInfoInTokenBO;

    /**
     * 积分数额
     */
    private HttpServletRequest req;

    /**
     * 积分数额
     */
    private String accessId;


}
