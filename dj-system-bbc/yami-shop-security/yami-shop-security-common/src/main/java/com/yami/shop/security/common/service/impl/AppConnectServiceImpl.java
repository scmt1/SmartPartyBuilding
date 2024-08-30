/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.event.LevelUpEvent;
import com.yami.shop.bean.event.UserRegisterLogEvent;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.param.UserExcelParam;
import com.yami.shop.bean.param.UserRegisterExcelParam;
import com.yami.shop.common.annotation.RedisLock;
import com.yami.shop.common.enums.StatusEnum;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.util.IPHelper;
import com.yami.shop.dao.UserExtensionMapper;
import com.yami.shop.dao.UserMapper;
import com.yami.shop.security.common.dao.AppConnectMapper;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.model.AppConnect;
import com.yami.shop.security.common.service.AppConnectService;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.service.UserService;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 *
 * @author lgh on 2018/09/07.
 */
@Service
@AllArgsConstructor
public class AppConnectServiceImpl extends ServiceImpl<AppConnectMapper, AppConnect> implements AppConnectService {

    private final AppConnectMapper appConnectMapper;
    private final ApplicationContext applicationContext;

    private final UserMapper userMapper;
    private final UserService userService;
    private final UserExtensionMapper userExtensionMapper;
    private final UserExtensionService userExtensionService;
    private final PasswordEncoder passwordEncoder;
    private final MapperFacade mapperFacade;
    private final PasswordManager passwordManager;

    /**
     * YamiUserServiceImpl#insertUserIfNecessary 将会清除该缓存信息
     * @param bizUserId
     * @return
     */
    @Override
    public AppConnect getByBizUserId(String bizUserId, Integer appId) {
        return appConnectMapper.getByBizUserId(bizUserId, appId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registerAndBindUser(String mobile, String password, String tempUid) {
        // 新建用户
        User user = new User();
        user.setModifyTime(new Date());
        user.setUserRegtime(new Date());
        user.setUserRegip(IPHelper.getIpAddr());
        user.setStatus(1);
        user.setUserMobile(mobile);
        if (StrUtil.isNotBlank(password)) {
            String decryptPassword = passwordManager.decryptPassword(password);
            user.setLoginPassword(passwordEncoder.encode(decryptPassword));
        }
        // 昵称默认位u + 手机尾号后4位
        user.setNickName("u" + mobile.substring(7));
        String userId = IdUtil.simpleUUID();
        user.setUserId(userId);
        user.setLevel(1);
        user.setLevelType(0);
        userMapper.insert(user);

        UserExtension userExtension = new UserExtension();
        userExtension.setBalance(0.0);
        userExtension.setTotalBalance(0.0);
        userExtension.setGrowth(0);
        userExtension.setLevel(1);
        userExtension.setLevelType(0);
        userExtension.setScore(0L);
        userExtension.setUpdateTime(new Date());
        userExtension.setVersion(0);
        userExtension.setSignDay(0);
        userExtension.setUserId(userId);
        userExtensionMapper.insert(userExtension);
        //用户注册成功后发送等级提升事件
        applicationContext.publishEvent(new LevelUpEvent(userExtension,1,0));
        if (StrUtil.isNotBlank(tempUid)) {
            appConnectMapper.bindUserIdByTempUid(user.getUserId(), tempUid);
        }
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer batchRegisterUser(UserExcelParam params, List<UserRegisterExcelParam> userList, Set<String> repeatPhoneSet, Set<String> repeatMailSet) {
        // 成功注册用户条数
        int registerNum = 0;
        if (CollectionUtils.isEmpty(userList)) {
            return registerNum;
        }
        List<String> phones = userList.stream().map(UserRegisterExcelParam::getPhone).collect(Collectors.toList());
        List<String> mails = userList.stream().map(UserRegisterExcelParam::getUserMail).collect(Collectors.toList());
        // 查询已经存在的手机号用户
        List<User> users = userMapper.listByUserPhones(phones);
        // 查询已经存在的邮箱用户
        List<User> userListByMail =userMapper.listByUserMails(mails);
        if (CollectionUtils.isNotEmpty(users)) {
            List<String> mobiles = users.stream().map(User::getUserMobile).collect(Collectors.toList());
            userList = userList.stream().filter(item -> !mobiles.contains(item.getPhone())).collect(Collectors.toList());
            repeatPhoneSet.addAll(mobiles);
        }
        if (CollectionUtils.isNotEmpty(userListByMail) && CollectionUtils.isNotEmpty(userList)) {
            List<String> userMails = userListByMail.stream().map(User::getUserMail).collect(Collectors.toList());
            userList = userList.stream().filter(item -> !userMails.contains(item.getUserMail())).collect(Collectors.toList());
            repeatMailSet.addAll(userMails);
        }
        if (CollectionUtils.isEmpty(userList)) {
            return registerNum;
        }
        registerNum = userList.size();
        Date now = new Date();
        // 用户
        List<User> userSave = new ArrayList<>();
        // 用户扩展表
        List<UserExtension> userExtensionsSave = new ArrayList<>();
        for (UserRegisterExcelParam param : userList) {
            // 用户
            String userId = IdUtil.simpleUUID();
            if(param.getBalance() > UserRegisterExcelParam.MaxBalance){
                //余额超出最大范围，最多不得超过999999999.99
                params.getErrorRowInfos().add(I18nMessage.getMessage("yami.balance.out.error"));
                params.setSuccessNum(params.getSuccessNum() -1);
                params.setErrorNum(params.getErrorNum() + 1);
                registerNum --;
                continue;
                //throw new YamiShopBindException("yami.balance.out.error);
            }
            if(param.getScore() > UserRegisterExcelParam.MaxScore){
                //积分超出最大范围，最多不得超过100000000
                //throw new YamiShopBindException("yami.score.out.error");
                params.getErrorRowInfos().add(I18nMessage.getMessage("yami.score.out.error"));
                params.setSuccessNum(params.getSuccessNum() -1);
                params.setErrorNum(params.getErrorNum() + 1);
                registerNum --;
                continue;
            }
            User user = mapperFacade.map(param, User.class);
            user.setUserId(userId);
            user.setUserMobile(param.getPhone());
            user.setLoginPassword(passwordEncoder.encode(param.getPassword()));
            user.setUserName(param.getPhone());
            user.setModifyTime(now);
            user.setUserRegtime(now);
            user.setStatus(1);
            userSave.add(user);
            // 用户扩展信息
            UserExtension userExtension = new UserExtension();
            userExtension.setUserId(userId);
            userExtension.setLevel(user.getLevel());
            userExtension.setLevelType(user.getLevelType());
            userExtension.setGrowth(param.getGrowth());
            userExtension.setScore(param.getScore());
            userExtension.setBalance(param.getBalance());
            userExtension.setTotalBalance(param.getBalance());
            userExtension.setVersion(0);
            userExtension.setUpdateTime(now);
            userExtensionsSave.add(userExtension);
        }
        userService.saveBatch(userSave);
        userExtensionService.saveBatch(userExtensionsSave);
        //用户注册成功后发送日志事件 积分log 成长值log
        applicationContext.publishEvent(new UserRegisterLogEvent(userExtensionsSave));
        return registerNum;
    }

    @Override
    public AppConnect getByTempUid(String tempUid) {
        return appConnectMapper.getByTempUid(tempUid);
    }

    @Override
    public void unBindUser(String bizUserId, Integer socialType) {
        appConnectMapper.unBindUser(bizUserId, socialType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(lockName = "insertUser", key = "#appConnect.appId + ':' + #appConnect.bizUserId")
    @Caching(evict = {
            @CacheEvict(cacheNames = "yami_user", key = "#appConnect.appId + ':' + #appConnect.bizUserId"),
            @CacheEvict(cacheNames = "AppConnect", key = "#appConnect.appId + ':' + #appConnect.bizUserId")
    })
    public void insertUserIfNecessary(AppConnect appConnect) {
        // 进入锁后再重新判断一遍用户是否创建
        AppConnect dbAppConnect = appConnectMapper.getByBizUserId(appConnect.getBizUserId(), appConnect.getAppId());
        if(dbAppConnect != null) {
            return;
        }

        String bizUnionId = appConnect.getBizUnionid();
        String userId = null;
        User user;

        if (StrUtil.isNotBlank(bizUnionId)) {
            userId = appConnectMapper.getUserIdByUnionId(bizUnionId);
        }
        if (StrUtil.isBlank(userId)) {
            userId = IdUtil.simpleUUID();
            Date now = new Date();
            user = new User();
            user.setUserId(userId);
            user.setModifyTime(now);
            user.setUserRegtime(now);
            user.setStatus(1);
            user.setNickName(appConnect.getNickName());
            user.setPic(appConnect.getImageUrl());
            user.setUserMobile(appConnect.getBizUserId());
            userMapper.insert(user);


            UserExtension userExtension = new UserExtension();
            userExtension.setBalance(0.0);
            userExtension.setTotalBalance(0.0);
            userExtension.setGrowth(0);
            userExtension.setLevel(1);
            userExtension.setLevelType(0);
            userExtension.setScore(0L);
            userExtension.setUpdateTime(new Date());
            userExtension.setVersion(0);
            userExtension.setSignDay(0);
            userExtension.setUserId(userId);
            userExtensionMapper.insert(userExtension);
            //用户注册成功后发送等级提升事件
            applicationContext.publishEvent(new LevelUpEvent(userExtension,1,0));
        } else {
            user = userMapper.selectById(userId);
        }
        appConnect.setUserId(user.getUserId());
        appConnectMapper.insert(appConnect);
    }
}
