/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.event.CheckIsMainShopEvent;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.User;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.service.UserService;
import com.yami.shop.user.common.model.UserLevel;
import com.yami.shop.user.common.model.UserLevelRights;
import com.yami.shop.user.common.model.UserRights;
import com.yami.shop.user.common.service.UserLevelRightsService;
import com.yami.shop.user.common.service.UserLevelService;
import com.yami.shop.user.common.service.UserRightsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 是否自营包邮
 *
 * @author yami
 */
@Slf4j
@Component("defaultCheckIsMainShopListener")
@AllArgsConstructor
public class CheckIsMainShopListener {

    private final UserService userService;
    private final UserLevelService userLevelService;
    private final UserRightsService userRightsService;
    private final UserLevelRightsService userLevelRightsService;

    @EventListener(CheckIsMainShopEvent.class)
    public void checkIsMainShopLister(CheckIsMainShopEvent event) {
        Order orderDb = event.getOrder();
        User user = userService.getUserByUserId(orderDb.getUserId());
        UserLevel userLevel = userLevelService.getOne(new LambdaQueryWrapper<UserLevel>().eq(UserLevel::getLevel, user.getLevel()).eq(UserLevel::getLevelType, user.getLevelType()));
        //todo 自营店包邮是否固定id是2？
        UserRights userRights = userRightsService.getById(2);
        UserLevelRights userLevelRights = userLevelRightsService.getOne(new LambdaQueryWrapper<UserLevelRights>().eq(UserLevelRights::getLevelId, userLevel.getId()).eq(UserLevelRights::getRightsId, userRights.getRightsId()));
        if (Objects.nonNull(userLevelRights)) {
            throw new YamiShopBindException("自营店包邮不能修改运费");
        }
    }

}
