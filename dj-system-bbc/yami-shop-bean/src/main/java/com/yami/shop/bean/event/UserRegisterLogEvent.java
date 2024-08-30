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

import com.yami.shop.bean.model.UserExtension;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 用户注册时，初始化 积分 余额 成长值 用户等级 添加日志
 * @author: cl
 * @date: 2021-04-21 09:06:29
 */
@Data
@AllArgsConstructor
public class UserRegisterLogEvent {

    /** 批量导入用户时用到 */
    private List<UserExtension> userExtensions;

}
