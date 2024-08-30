/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.param.CustomerMemberParam;
import com.yami.shop.bean.param.CustomerMemberTrendParam;
import com.yami.shop.bean.param.CustomerPayParam;
import com.yami.shop.bean.param.CustomerReqParam;
import com.yami.shop.common.util.*;
import com.yami.shop.dao.UserExtensionMapper;
import com.yami.shop.dao.UserMapper;
import com.yami.shop.user.common.dao.UserBalanceLogMapper;
import com.yami.shop.user.common.dao.UserLevelLogMapper;
import com.yami.shop.user.common.model.UserLevelLog;
import com.yami.shop.user.common.service.UserLevelLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 *
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Service
public class UserLevelLogServiceImpl extends ServiceImpl<UserLevelLogMapper, UserLevelLog> implements UserLevelLogService {

    @Autowired
    private UserLevelLogMapper userLevelLogMapper;
    @Autowired
    private UserExtensionMapper userExtensionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserBalanceLogMapper userBalanceLogMapper;

    @Value("${yami.expose.operation.auth:}")
    private Boolean permission;

    @Override
    public Page<UserLevelLog> getPage(Page<UserLevelLog> page, UserLevelLog userLevelLog) {
        return userLevelLogMapper.getPage(page,userLevelLog);
    }

    @Override
    public Integer getMaxLevelByUserId(String userId) {
        return userLevelLogMapper.getMaxLevelByUserId(userId);
    }

    @Override
    public CustomerMemberParam countMemberNum(CustomerReqParam param) {
        List<DateParam> everyDays = DateUtils.findEveryDays(param.getStartTime(), param.getEndTime());
        CustomerMemberParam resParam = new CustomerMemberParam();
        List<CustomerMemberTrendParam> resList = new ArrayList<>();
        if (everyDays.size() == 1 ) {
            Date startTime = everyDays.get(0).getStartTime();
            Date beforeDate = DateUtils.getBeforeDate(startTime);
            everyDays = DateUtils.findEveryDays(beforeDate, param.getEndTime());
        }
        Integer userNum = null;
        // 新增会员总数
        int newUserNum = 0;
        // 升级会员数
        int growthMemberNum = 0;
        // 储值会员数
        int rechargeNum = 0;
        List<CustomerPayParam> userParams = userMapper.countUserByMemberParam(DateUtil.offsetDay(param.getStartTime(),-1),param.getEndTime(), null);
        List<CustomerPayParam> levelParams = userLevelLogMapper.countGrowthMemberByTime(param);
        List<CustomerPayParam> balanceParams = userBalanceLogMapper.countUserRechargeNumByTime(param);
        Map<Date, Integer> userMap = userParams.stream().collect(toMap(CustomerPayParam::getCreateDate, CustomerPayParam::getNumber));
        Map<Date, Integer> levelMap = levelParams.stream().collect(toMap(CustomerPayParam::getCreateDate, CustomerPayParam::getNumber));
        Map<Date, Integer> balanceMap = balanceParams.stream().collect(toMap(CustomerPayParam::getCreateDate, CustomerPayParam::getNumber));
        Integer userCount = userMapper.selectCount(new LambdaQueryWrapper<User>().lt(User::getUserRegtime, param.getStartTime()));
        for (DateParam everyDay : everyDays) {
            CustomerMemberTrendParam res = new CustomerMemberTrendParam();
//            param.setDateTime(null);
//            param.setStartTime(null);
//            param.setEndTime(null);
//            param.setDateTime(everyDay.getStartTime());
            res.setCurrentDay(DateUtils.dateToStrYmd(everyDay.getEndTime()));
            //累计会员
            userCount += userMap.getOrDefault(everyDay.getStartTime(),0);
            res.setAccumulate(userCount);
            res.setNewAddMember(userMap.getOrDefault(everyDay.getStartTime(),0));
            newUserNum += res.getNewAddMember();
//            res.setAccumulate(userExtensionMapper.countMemberByParam(param));
//            if (Objects.isNull(userNum)){
//                param.setDateTime(DateUtil.offsetDay(everyDay.getStartTime(),-1));
//                userNum = userExtensionMapper.countMemberByParam(param);
//                param.setDateTime(everyDay.getStartTime());
//                res.setNewAddMember(res.getAccumulate() - userNum);
//                newUserNum += res.getNewAddMember();
//            }else {
//                int num = res.getAccumulate() - userNum;
//                if (num < 0){
//                    res.setNewAddMember(0);
//                }else {
//                    newUserNum += num;
//                    res.setNewAddMember(num);
//                }
//            }
//            userNum = res.getAccumulate();
            // 升级会员数
            param.setDateTime(null);
            param.setStartTime(everyDay.getStartTime());
            param.setEndTime(everyDay.getEndTime());
            res.setGrowthMember(levelMap.getOrDefault(everyDay.getStartTime(),0));
            growthMemberNum += res.getGrowthMember();
            // 储值会员数
            res.setStoredValue(balanceMap.getOrDefault(everyDay.getStartTime(),0));
            rechargeNum += res.getStoredValue();
            resList.add(res);
        }
        resList.sort(Comparator.comparing(CustomerMemberTrendParam::getCurrentDay));
        CustomerMemberTrendParam trendParam = resList.get(resList.size() - 1);
        CustomerMemberTrendParam first = resList.get(0);
        resParam.setAccumulate(trendParam.getAccumulate());
        resParam.setAccumulateRate(divRate(resParam.getAccumulate()-first.getAccumulate(),first.getAccumulate(),4));
        // 新增会员数
        resParam.setNewAddMember(newUserNum);
        DateParam dateParam = DateUtils.getPreDateByRangeTime(param.getStartTime(), param.getEndTime());
        param.setStartTime(dateParam.getStartTime());
        param.setEndTime(dateParam.getEndTime());
        Integer preNewAddMember = userExtensionMapper.countMemberByParam(param);
        resParam.setNewAddMemberRate(divRate(newUserNum-preNewAddMember,preNewAddMember,4));
        // 升级会员
        resParam.setGrowthMember(growthMemberNum);
        Integer preGrowthMember = userLevelLogMapper.countGrowthMember(param);
        resParam.setGrowthMemberRate(divRate(growthMemberNum-preGrowthMember,preGrowthMember,4));
        // 储值会员数
        resParam.setStoredValue(rechargeNum);
        int preStoredNum = userBalanceLogMapper.countUserRechargeNumByDateRange(param);
        resParam.setStoredValueRate(divRate(rechargeNum-preStoredNum,preStoredNum,4));
        resParam.setList(resList);
        return resParam;
    }

    @Override
    public UserLevelLog getMaxCrtTimeByUserId(String userId) {
        return userLevelLogMapper.getMaxCrtTimeByUserId(userId);
    }

    @Override
    public IPage<UserLevelLog> pageBuyLevelLog(PageParam<UserLevelLog> page, UserLevelLog userLevelLog) {
        IPage<UserLevelLog> userLevelLogPage = userLevelLogMapper.pageBuyLevelLog(page, userLevelLog);
        if (BooleanUtil.isFalse(permission)) {
            for (UserLevelLog record : userLevelLogPage.getRecords()) {
                if (StrUtil.isNotBlank(record.getNickName()) && PrincipalUtil.isMobile(record.getNickName())) {
                    record.setNickName(PhoneUtil.hideBetween(record.getNickName()).toString());
                }
                if (StrUtil.isNotBlank(record.getUserName()) && PrincipalUtil.isMobile(record.getUserName())) {
                    record.setUserName(PhoneUtil.hideBetween(record.getUserName()).toString());
                }
            }
        }
        return userLevelLogPage;
    }

    private Double divRate(Integer a, Integer b,Integer scale) {
        if (Objects.isNull(b) || b == 0 || Objects.isNull(a)) {
            return 0.0;
        }
        return Arith.div(a,b,scale);
    }
}
