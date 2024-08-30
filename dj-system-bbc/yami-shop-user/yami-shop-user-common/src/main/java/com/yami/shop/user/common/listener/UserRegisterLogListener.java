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

import com.yami.shop.bean.enums.ScoreLogType;
import com.yami.shop.bean.event.UserRegisterLogEvent;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.param.ScoreConfigParam;
import com.yami.shop.common.config.Constant;
import com.yami.shop.service.SysConfigService;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.enums.GrowthLogSourceEnum;
import com.yami.shop.user.common.model.UserBalanceLog;
import com.yami.shop.user.common.model.UserGrowthLog;
import com.yami.shop.user.common.model.UserScoreDetail;
import com.yami.shop.user.common.model.UserScoreLog;
import com.yami.shop.user.common.service.UserBalanceLogService;
import com.yami.shop.user.common.service.UserGrowthLogService;
import com.yami.shop.user.common.service.UserScoreDetailService;
import com.yami.shop.user.common.service.UserScoreLogService;
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
 * @author: cl
 * @date: 2021-04-21 11:09:06
 */
@Slf4j
@Component("userRegisterLogListener")
@AllArgsConstructor
public class UserRegisterLogListener {

    private final UserScoreDetailService userScoreDetailService;
    private final UserScoreLogService userScoreLogService;
    private final UserBalanceLogService userBalanceLogService;
    private final UserGrowthLogService userGrowthLogService;
    private final SysConfigService sysConfigService;
    private final UserExtensionService userExtensionService;


    /**
     * 用户注册时，初始化 积分 余额 成长值 用户等级 添加日志
     */
    @EventListener(UserRegisterLogEvent.class)
    public void userRegisterLogListener(UserRegisterLogEvent event) {
        List<UserExtension> userExtensions = event.getUserExtensions();
        if (CollectionUtils.isEmpty(userExtensions)) {
            return;
        }
        // 积分日志
        List<UserScoreLog> scoreLogs = new ArrayList<>();
        // 积分明细
        List<UserScoreDetail> scoreDetails = new ArrayList<>();
        // 用户余额
        List<UserBalanceLog> userBalanceLogs = new ArrayList<>();
        // 成长值
        List<UserGrowthLog> growthLogs = new ArrayList<>();
        Date now = new Date();
        for (UserExtension userExtension : userExtensions) {
            String userId = userExtension.getUserId();
            // 注册增送积分
            ScoreConfigParam scoreParam = sysConfigService.getSysConfigObject(Constant.SCORE_CONFIG, ScoreConfigParam.class);
            // 更新积分
            userExtension.setScore(scoreParam.getRegisterScore() + userExtension.getScore());
            setRegisterScoreLog(scoreLogs, scoreDetails, userExtension, scoreParam);
            // 积分log
            Long score = Objects.isNull(scoreParam.getRegisterScore()) ? userExtension.getScore() : userExtension.getScore() - scoreParam.getRegisterScore();
            setSystemScoreLog(scoreLogs, scoreDetails, now, userId, score);
            // 余额
            Double balance = userExtension.getBalance();
            setBalanceLog(userBalanceLogs, now, userId, balance);
            // 成长值
            Integer growth = userExtension.getGrowth();
            setGrowthLog(growthLogs, now, userId, growth);
        }

        userExtensionService.updateBatchById(userExtensions);

        if (CollectionUtils.isNotEmpty(scoreLogs)) {
            userScoreLogService.saveBatch(scoreLogs);
        }
        if (CollectionUtils.isNotEmpty(scoreDetails)) {
            userScoreDetailService.saveBatch(scoreDetails);
        }
        if (CollectionUtils.isNotEmpty(userBalanceLogs)) {
            userBalanceLogService.saveBatch(userBalanceLogs);
        }
        if (CollectionUtils.isNotEmpty(growthLogs)) {
            userGrowthLogService.saveBatch(growthLogs);
        }
    }

    private void setGrowthLog(List<UserGrowthLog> growthLogs, Date now, String userId, Integer growth) {
        if (Objects.nonNull(growth) && growth > 0) {
            UserGrowthLog growthLog = new UserGrowthLog();
            growthLog.setUserId(userId);
            growthLog.setSource(GrowthLogSourceEnum.SYSTEM.value());
            growthLog.setChangeGrowth(growth);
            growthLog.setCreateTime(now);
            growthLog.setRemarks("系统修改用户成长值");
            growthLogs.add(growthLog);
        }
    }

    private void setBalanceLog(List<UserBalanceLog> userBalanceLogs, Date now, String userId, Double balance) {
        if (Objects.nonNull(balance) && balance > 0) {
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setUserId(userId);
            userBalanceLog.setCreateTime(now);
            userBalanceLog.setChangeBalance(balance);
            userBalanceLog.setIoType(1);
            // 平台修改余额
            userBalanceLog.setType(5);
            userBalanceLogs.add(userBalanceLog);
        }
    }

    private void setSystemScoreLog(List<UserScoreLog> scoreLogs, List<UserScoreDetail> scoreDetails, Date now, String userId, Long score) {
        if (score > 0) {
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setUserId(userId);
            userScoreLog.setScore(score);
            // 用户批量导入注册设置积分
            userScoreLog.setSource(ScoreLogType.SYSTEM.value());
            userScoreLog.setCreateTime(now);
            userScoreLog.setIoType(1);
            scoreLogs.add(userScoreLog);
            // 积分明细
            UserScoreDetail addDetail = new UserScoreDetail();
            addDetail.setStatus(1);
            addDetail.setUserId(userId);
            addDetail.setCreateTime(now);
            addDetail.setUsableScore(score);
            scoreDetails.add(addDetail);
        }
    }

    private void setRegisterScoreLog(List<UserScoreLog> scoreLogs, List<UserScoreDetail> scoreDetails, UserExtension userExtension, ScoreConfigParam scoreParam) {
        if(scoreParam.getRegisterScore() != null) {
            //添加积分明细
            UserScoreDetail addDetail = new UserScoreDetail();
            addDetail.setCreateTime(new Date());
            addDetail.setStatus(1);
            addDetail.setUserId(userExtension.getUserId());
            addDetail.setUsableScore(scoreParam.getRegisterScore());
            scoreDetails.add(addDetail);

            //添加积分日志
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setUserId(userExtension.getUserId());
            userScoreLog.setScore(scoreParam.getRegisterScore());
            userScoreLog.setSource(ScoreLogType.REGISTER.value());
            userScoreLog.setCreateTime(new Date());
            userScoreLog.setIoType(1);
            scoreLogs.add(userScoreLog);
        }
    }

}
