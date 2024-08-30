/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.param.UserManagerParam;
import com.yami.shop.bean.param.UserManagerReqParam;
import com.yami.shop.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author yami
 */
@Slf4j
public class AnalysisSalseTask implements Callable<List<UserManagerParam>> {

    private UserService userService;
    private UserManagerReqParam user;
    private Page<User> pages;

    public AnalysisSalseTask(UserService userService, UserManagerReqParam user, Page<User> pages) {
        this.userService = userService;
        this.user = user;
        this.pages = pages;
    }

    @Override
    public List<UserManagerParam> call() throws Exception {
        IPage<UserManagerParam> userPage =  userService.getUserInfoPage(pages,user);
        return userPage.getRecords();
    }
}
