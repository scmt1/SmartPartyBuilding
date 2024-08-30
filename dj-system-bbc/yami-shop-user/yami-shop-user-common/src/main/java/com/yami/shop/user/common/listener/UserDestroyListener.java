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
import com.yami.shop.bean.enums.ScoreLogType;
import com.yami.shop.bean.event.UserDestroyEvent;
import com.yami.shop.bean.event.UserRegisterLogEvent;
import com.yami.shop.bean.model.UserAddr;
import com.yami.shop.bean.model.UserCollection;
import com.yami.shop.bean.model.UserCollectionShop;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.param.ScoreConfigParam;
import com.yami.shop.common.config.Constant;
import com.yami.shop.security.common.dao.AppConnectMapper;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.model.AppConnect;
import com.yami.shop.security.common.service.AppConnectService;
import com.yami.shop.service.*;
import com.yami.shop.user.common.enums.GrowthLogSourceEnum;
import com.yami.shop.user.common.model.*;
import com.yami.shop.user.common.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author chiley
 * @date 2022/9/8 11:11
 */
@Slf4j
@Component("userDestroyListener")
@AllArgsConstructor
public class UserDestroyListener {

    private UserAddrService userAddrService;
    private UserGrowthLogService userGrowthLogService;
    private UserScoreDetailService userScoreDetailService;
    private UserScoreLockService userScoreLockService;
    private UserScoreLogService userScoreLogService;
    private UserTagUserService userTagUserService;
    private AppConnectService appConnectService;
    private TokenStore tokenStore;

    private final AppConnectMapper appConnectMapper;

    /**
     * 用户注销时,删除相关数据
     */
    @EventListener(UserDestroyEvent.class)
    public void userDestroyListener(UserDestroyEvent event) {
        String userId = event.getUserId();
        userAddrService.remove(new LambdaQueryWrapper<UserAddr>().eq(UserAddr::getUserId, userId));
        userGrowthLogService.remove(new LambdaQueryWrapper<UserGrowthLog>().eq(UserGrowthLog::getUserId, userId));
        userScoreDetailService.remove(new LambdaQueryWrapper<UserScoreDetail>().eq(UserScoreDetail::getUserId, userId));
        userScoreLockService.remove(new LambdaQueryWrapper<UserScoreLock>().eq(UserScoreLock::getUserId, userId));
        userScoreLogService.remove(new LambdaQueryWrapper<UserScoreLog>().eq(UserScoreLog::getUserId, userId));
        userTagUserService.remove(new LambdaQueryWrapper<UserTagUser>().eq(UserTagUser::getUserId, userId));
        appConnectService.remove(new LambdaQueryWrapper<AppConnect>().eq(AppConnect::getUserId, userId));
        tokenStore.deleteAllToken(SysTypeEnum.ORDINARY.value().toString(), userId);
        // 解除社交账号的绑定
        appConnectMapper.unBindUserByUserId(userId);

    }
}
