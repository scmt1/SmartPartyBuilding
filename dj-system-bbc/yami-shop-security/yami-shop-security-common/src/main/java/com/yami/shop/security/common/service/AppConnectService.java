/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.param.UserExcelParam;
import com.yami.shop.bean.param.UserRegisterExcelParam;
import com.yami.shop.security.common.model.AppConnect;

import java.util.List;
import java.util.Set;

/**
 *
 * @author lgh on 2018/09/07.
 */
public interface AppConnectService extends IService<AppConnect> {

    /**
     * 根据第三方userId和第三方系统id获取用户关联第三方信息
     * @param bizUserId 第三方userId
     * @param appId 第三方系统id
     * @return 用户关联第三方信息
     */
    AppConnect getByBizUserId(String bizUserId, Integer appId);

    /**
     * 用户注册并绑定第三方账号
     * @param mobile 手机号
     * @param password 密码
     * @param tempUid 用来找到openid进行关联
     * @return
     */
    User registerAndBindUser(String mobile, String password, String tempUid);

    /**
     * 批量注册用户
     * @param userList 用户列表
     * @param repeatPhoneSet 重复手机号信息
     * @param repeatMailSet 重复邮箱信息
     * @return 结果
     */
    Integer batchRegisterUser(UserExcelParam params, List<UserRegisterExcelParam> userList, Set<String> repeatPhoneSet, Set<String> repeatMailSet);

    /**
     * 获取根据尝试社交登录时，保存的临时的uid获取社交信息
     *
     * @param tempUid tempUid
     * @return 用户社交账号信息
     */
    AppConnect getByTempUid(String tempUid);

    /**
     * 解除用户绑定
     * @param bizUserId openid
     * @param socialType 社交账号类型
     */
    void unBindUser(String bizUserId, Integer socialType);

    void insertUserIfNecessary(AppConnect appConnect);
}
