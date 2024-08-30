/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.adapter;

import com.yami.shop.security.common.bo.UserInfoInTokenBO;

import java.util.Map;

/**
 * 实现该接口之后，修改需要授权登陆的路径，不需要授权登陆的路径
 *
 * @author FrozenWatermelon
 * @date 2020/7/4
 */
public interface SignAuthAdapter {

    /**
     * 补充用户信息
     * @param dataMap 请求数据
     * @return
     */
    UserInfoInTokenBO loadUserInfoInToken(Map<String, Object> dataMap);

}
