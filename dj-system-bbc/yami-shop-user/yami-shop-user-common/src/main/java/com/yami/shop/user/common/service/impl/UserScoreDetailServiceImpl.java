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

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.enums.ScoreLogType;
import com.yami.shop.bean.enums.UserScoreStatus;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.dao.UserScoreDetailMapper;
import com.yami.shop.user.common.model.UserScoreDetail;
import com.yami.shop.user.common.model.UserScoreLog;
import com.yami.shop.user.common.service.UserScoreDetailService;
import com.yami.shop.user.common.service.UserScoreLogService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 *
 * @author lhd
 * @date 2020-05-25 15:31:02
 */
@Service
@AllArgsConstructor
public class UserScoreDetailServiceImpl extends ServiceImpl<UserScoreDetailMapper, UserScoreDetail> implements UserScoreDetailService {

    private final UserScoreDetailMapper scoreDetailMapper;
    private final UserExtensionService userExtensionService;
    private final UserScoreLogService userScoreLogService;


    /**
     * 修改用户过期积分
     * @param dateTime 过期时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateExpireScoreDetail(DateTime dateTime) {
        // 1.查询要进行积分过期的用户(已超出过期时间，但状态还是正常、积分抵扣的记录) 积分状态 com.yami.shop.bean.enums.UserScoreStatus
        List<UserScoreDetail> userExpiredScoreDetails = scoreDetailMapper.listExpireScoreDetail(dateTime);
        if(CollectionUtils.isEmpty(userExpiredScoreDetails)){
            return;
        }
        // 2.修改用户积分信息
        Map<String, List<UserScoreDetail>> userScoreMap = userExpiredScoreDetails.stream()
                .filter(userScoreDetail -> Objects.equals(userScoreDetail.getStatus(), UserScoreStatus.NORMAL.value()))
                .collect(Collectors.groupingBy(UserScoreDetail::getUserId));
        List<UserScoreLog> logList = new ArrayList<>();
        List<UserExtension> userExtensions = new ArrayList<>();
        for (String userId : userScoreMap.keySet()) {
            List<UserScoreDetail> detailList = userScoreMap.get(userId);
            long expireScore = detailList.stream().mapToLong(UserScoreDetail::getUsableScore).sum();
            UserExtension userExtension = new UserExtension();
            userExtension.setUserId(userId);
            userExtension.setScore(expireScore);
            userExtensions.add(userExtension);

            // 添加积分日志
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setUserId(userId);
            userScoreLog.setScore(expireScore);
            userScoreLog.setSource(ScoreLogType.EXPIRE.value());
            userScoreLog.setCreateTime(new Date());
            userScoreLog.setIoType(0);
            logList.add(userScoreLog);
        }
        // 批量修改用户积分和添加积分日志
        if(CollectionUtils.isNotEmpty(userExtensions)){
            userExtensionService.updateBatchByUserId(userExtensions);
        }
        if(CollectionUtils.isNotEmpty(logList)){
            userScoreLogService.saveBatch(logList);
        }
        // 3.修改积分明细
        for (UserScoreDetail userScoreDetail : userExpiredScoreDetails) {
            // 正常状态 -》 过期
            if (Objects.equals(userScoreDetail.getStatus(), UserScoreStatus.NORMAL.value())) {
                userScoreDetail.setStatus(UserScoreStatus.EXPIRED.value());
                userScoreDetail.setExpireTime(new Date());
            }
            // 订单抵现 -》 订单抵现已结算
            else {
                userScoreDetail.setStatus(UserScoreStatus.ORDER_CREDIT_SETTLE.value());
            }
        }
        updateBatchById(userExpiredScoreDetails);
    }

//    /**
//     * 取消订单时修改用户积分和添加日志
//     * @param orderNumber 订单号
//     * @param userId 用户id
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateLogAndDetail(String orderNumber, String userId) {
//        UserExtension userExtension = userExtensionService.getOne(
//                new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, userId));
//        if(Objects.isNull(userExtension)){
//            return;
//        }
//        //修改积分明细
//        List<UserScoreDetail> scoreDetails = list(new LambdaUpdateWrapper<UserScoreDetail>()
//                .eq(UserScoreDetail::getBizId, orderNumber).eq(UserScoreDetail::getStatus, 0));
//        if(CollectionUtils.isEmpty(scoreDetails)){
//            return;
//        }
//        //要修改的用户积分，状态为-1表示还没过期的可以返还的用户积分
//        Long score = 0L;
//        for (UserScoreDetail scoreDetail : scoreDetails) {
//            score += scoreDetail.getUsableScore();
//            scoreDetail.setStatus(1);
//        }
//        updateBatchById(scoreDetails);
//        userExtension.setScore(userExtension.getScore() + score);
//        userExtensionService.updateById(userExtension);
//        //添加积分日志
//        UserScoreLog scoreLog = new UserScoreLog();
//        scoreLog.setUserId(userId);
//        scoreLog.setBizId(orderNumber);
//        scoreLog.setScore(score);
//        scoreLog.setSource(ScoreLogType.SCORE_CASH.value());
//        scoreLog.setCreateTime(DateUtil.date());
//        scoreLog.setIoType(1);
//        userScoreLogService.save(scoreLog);
//    }

    @Override
    public void saveUserScoreDetail(UserScoreDetail userScoreDetail) {
        UserExtension user = userExtensionService.getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, userScoreDetail.getUserId()));
        if (Objects.equals(userScoreDetail.getStatus(), 0)) {
            save(userScoreDetail);
            return;
        }
        if (user.getScore() < 0) {
            if (Math.abs(user.getScore()) >= userScoreDetail.getUsableScore()) {
                userScoreDetail.setUsableScore(0L);
                userScoreDetail.setStatus(0);
            } else {
                userScoreDetail.setUsableScore(userScoreDetail.getUsableScore() - Math.abs(user.getScore()));
            }
        }
        save(userScoreDetail);
    }

    @Override
    public void saveBatchUserScoreDetail(List<UserScoreDetail> userScoreDetails, String userId) {
        UserExtension user = userExtensionService.getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, userId));
        if (user.getScore() < 0) {
            for (UserScoreDetail userScoreDetail : userScoreDetails) {
                if (Math.abs(user.getScore()) >= userScoreDetail.getUsableScore()) {
                    userScoreDetail.setUsableScore(0L);
                    userScoreDetail.setStatus(0);
                } else {
                    userScoreDetail.setUsableScore(userScoreDetail.getUsableScore() - Math.abs(user.getScore()));
                    break;
                }
            }
        }
        saveBatch(userScoreDetails);
    }

    @Override
    public List<UserScoreDetail> listByUserIdAndExpireTimeAndStatus(String userId, Date expireTime, Integer status) {
        return scoreDetailMapper.listByUserIdAndExpireTimeAndStatus(userId, expireTime, status);
    }

    @Override
    public List<UserScoreDetail> listByCreateTime(String userId, Integer status) {
        return scoreDetailMapper.listByCreateTime(userId, status);
    }

    @Override
    public List<UserScoreDetail> listByCreateTimeAndPage(String userId, Integer status, Integer current, Integer size) {
        return scoreDetailMapper.listByCreateTimeAndPage(userId, status, current, size);
    }
}
