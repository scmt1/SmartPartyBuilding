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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.yami.shop.bean.dto.UserScoreLockDTO;
import com.yami.shop.bean.enums.OrderType;
import com.yami.shop.bean.enums.ScoreLogType;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.util.Json;
import com.yami.shop.dao.UserExtensionMapper;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.dao.UserScoreDetailMapper;
import com.yami.shop.user.common.dao.UserScoreLockMapper;
import com.yami.shop.user.common.model.UserScoreDetail;
import com.yami.shop.user.common.model.UserScoreLock;
import com.yami.shop.user.common.model.UserScoreLog;
import com.yami.shop.user.common.service.UserScoreDetailService;
import com.yami.shop.user.common.service.UserScoreLockService;
import com.yami.shop.user.common.service.UserScoreLogService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 积分锁定信息
 *
 * @author lhd
 * @date 2022-05-06 17:27:53
 */
@Service
public class UserScoreLockServiceImpl extends ServiceImpl<UserScoreLockMapper, UserScoreLock> implements UserScoreLockService {

    @Autowired
    private UserScoreLockMapper userScoreLockMapper;
    @Autowired
    private UserScoreDetailService userScoreGetLogService;
    @Autowired
    private UserScoreDetailMapper userScoreDetailMapper;
    @Autowired
    private UserScoreLogService userScoreLogService;
    @Autowired
    private UserExtensionService userExtensionService;
    @Autowired
    private UserExtensionMapper userExtensionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lock(List<UserScoreLockDTO> userScoreLocks, String userId) {
        Integer orderType = userScoreLocks.get(0).getOrderType();
        // 2.开始锁定积分
        List<UserScoreLock> userScoreLockLogs = Lists.newArrayList();
        Set<String> orderNumbers = new HashSet<>();
        long useScore = 0;
        useScore = addUserScoreLockLog(userScoreLocks, userId, userScoreLockLogs, orderNumbers, useScore);
        if (!userScoreLockLogs.isEmpty()){
            // 批量保存锁定记录
            saveBatch(userScoreLockLogs);
            // 扣减用户积分
            int updateStatus = userExtensionMapper.lockScoreBySubmitOrder(userId, useScore);
            if (updateStatus < 1) {
                throw new YamiShopBindException("yami.user.score.no.enough");
            }
            UserScoreLog userScoreLog = new UserScoreLog();
            Date date = new Date();
            userScoreLog.setCreateTime(date);
            userScoreLog.setUserId(userId);
            if (Objects.equals(orderType, OrderType.SCORE.value()) || Objects.equals(orderType, OrderType.ORDINARY.value())) {
                userScoreLog.setSource(ScoreLogType.SCORE_CASH.value());
                userScoreLog.setBizId(userScoreLocks.get(0).getOrderNumber());
            } else {
                userScoreLog.setSource(ScoreLogType.SCORE_CASH.value());
                userScoreLog.setBizId(null);
            }
            userScoreLog.setScore(useScore);
            userScoreLog.setIoType(0);
            userScoreLogService.save(userScoreLog);

//            // 发送消息一个小时后解锁积分(包括哪些订单)
//            List<String> orderNumberList = new ArrayList<>(orderNumbers);
//            UserScoreBO userScoreBo = new UserScoreBO();
//            userScoreBo.setUserId(userId);
//            userScoreBo.setOrderNumbers(orderNumberList);
//            SendStatus sendStatus = userScoreTemplate.syncSend(RocketMqConstant.SCORE_UNLOCK_TOPIC, new GenericMessage<>(userScoreBo), RocketMqConstant.TIMEOUT, RocketMqConstant.CANCEL_ORDER_DELAY_LEVEL + 1).getSendStatus();
//            if (!Objects.equals(sendStatus, SendStatus.SEND_OK)) {
//                // 消息发不出去就抛异常，发的出去无所谓
//                throw new YamiShopBindException(ResponseEnum.EXCEPTION);
//            }
        }
    }

    @Override
    public void unlock(Order order) {
        UserExtension userExtension = userExtensionService.getOne(
                new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, order.getUserId()));
        if(Objects.isNull(userExtension)){
            return;
        }
        //修改积分明细
        List<UserScoreDetail> scoreDetails = userScoreDetailMapper.selectList(new LambdaUpdateWrapper<UserScoreDetail>()
                .eq(UserScoreDetail::getBizId, order.getOrderNumber()).eq(UserScoreDetail::getStatus, 0));
        if(CollectionUtils.isEmpty(scoreDetails)){
            return;
        }
        List<Long> userScoreLockIds = new ArrayList<>();
        Long useScore = 0L;
        List<UserScoreLock> userScoreLocks = userScoreLockMapper.selectList(new LambdaQueryWrapper<UserScoreLock>()
                .eq(UserScoreLock::getOrderNumber,order.getOrderNumber()));
        List<Long> userScoreGetIds = new ArrayList<>();
        for (UserScoreLock userScoreLock : userScoreLocks) {
            List<Long> tempUserScoreGetIds = Json.parseArray(userScoreLock.getScoreGetLogIds(), Long[].class);
            userScoreGetIds.addAll(tempUserScoreGetIds);
            userScoreLockIds.add(userScoreLock.getId());
            useScore = useScore + userScoreLock.getScore();
        }
        // todo 批量将用户的积分变成未使用状态，是否要把已经拆开的积分合并？
        userScoreDetailMapper.batchUpdateUserScoreGetStatus(1, userScoreGetIds);
        userExtensionMapper.updateScoreByUserId(order.getUserId(), useScore);
        UserScoreLog userScoreLog = new UserScoreLog();
        Date date = new Date();
        userScoreLog.setCreateTime(date);
        userScoreLog.setUserId(order.getUserId());
        if (Objects.equals(order.getOrderType(), OrderType.SCORE.value())) {
            userScoreLog.setSource(ScoreLogType.SCORE_CASH.value());
            userScoreLog.setBizId(order.getOrderNumber());
        } else {
            userScoreLog.setSource(ScoreLogType.SCORE_CASH.value());
            userScoreLog.setBizId(null);
        }
        userScoreLog.setScore(useScore);
        userScoreLog.setIoType(1);
        userScoreLogService.save(userScoreLog);
        // 将锁定状态标记为已解锁
        int updateStatus = userScoreLockMapper.unLockByIds(-1, userScoreLockIds);
        if (updateStatus == 0) {
            throw new YamiShopBindException(ResponseEnum.EXCEPTION);
        }
    }

    /**
     * 添加用户积分锁定日志
     * @param userScoreLocks 锁定积分日志
     * @param userId 用户id
     * @param userScoreLockLogs 用户积分锁定日志
     * @param orderNumbers 订单id列表
     * @param useScore 用户积分
     * @return 用户积分
     */
    private long addUserScoreLockLog(List<UserScoreLockDTO> userScoreLocks, String userId, List<UserScoreLock> userScoreLockLogs, Set<String> orderNumbers, Long useScore) {
        List<UserScoreDetail> updateScoreDetails = new ArrayList<>();
        for (UserScoreLockDTO userScoreLockParam : userScoreLocks) {
            if (Objects.isNull(userScoreLockParam.getScore())) {
                break;
            }
            List<UserScoreDetail> temp = userScoreGetLogService.listByCreateTime(userId, 1);
            long sumTotalUsableScore = temp.stream().mapToLong(UserScoreDetail::getUsableScore).sum();
            if (sumTotalUsableScore < userScoreLockParam.getScore()) {
                throw new YamiShopBindException("yami.user.score.enough");
            }
            if (temp.isEmpty()) {
                break;
            }
            Date date = new Date();
            // 按积分创建时间优先扣减
            Long usableScore = userScoreLockParam.getScore();
            List<UserScoreDetail> userScoreGetLogList;
            long sumUsableScore;
            int i = 1;
            List<Long> scoreGetLogIds = new ArrayList<>();
            do {
                userScoreGetLogList = userScoreGetLogService.listByCreateTimeAndPage(userId, 1, 0, i * 10);
                sumUsableScore = userScoreGetLogList.stream().mapToLong(UserScoreDetail::getUsableScore).sum();
                if (usableScore > sumUsableScore) {
                    i = i + 1;
                }
            } while (usableScore > sumUsableScore);

            for (UserScoreDetail userScoreGetLog : userScoreGetLogList) {
                if (usableScore >= userScoreGetLog.getUsableScore()) {
                    userScoreGetLog.setUpdateTime(date);
                    userScoreGetLog.setStatus(0);
                    userScoreGetLog.setBizId(userScoreLockParam.getOrderNumber());
                    updateScoreDetails.add(userScoreGetLog);
                    usableScore = usableScore - userScoreGetLog.getUsableScore();
                    scoreGetLogIds.add(userScoreGetLog.getUserScoreDetailId());
                } else {
                    // 如果当条拥有的积分行大于此次使用的积分
                    // 1）新增一条已使用的积分
                    UserScoreDetail addDetail = new UserScoreDetail();
                    addDetail.setCreateTime(userScoreGetLog.getCreateTime());
                    addDetail.setUpdateTime(userScoreGetLog.getUpdateTime());
                    addDetail.setStatus(0);
                    addDetail.setUserId(userScoreGetLog.getUserId());
                    addDetail.setBizId(userScoreLockParam.getOrderNumber());
                    addDetail.setUsableScore(usableScore);
                    userScoreGetLogService.save(addDetail);
                    scoreGetLogIds.add(addDetail.getUserScoreDetailId());

                    // 2）将这条的可用积分 - 本次使用的积分
                    userScoreGetLog.setUsableScore(userScoreGetLog.getUsableScore() - usableScore);
                    userScoreGetLog.setUpdateTime(date);
                    updateScoreDetails.add(userScoreGetLog);
                    scoreGetLogIds.add(userScoreGetLog.getUserScoreDetailId());
//                    // 如果当条拥有的积分行大于此次使用的积分
//                    // 1）新增一条正常状态积分 - 此次使用的积分
//                    UserScoreDetail userScoreGetLog2 = new UserScoreDetail();
//                    userScoreGetLog2.setCreateTime(userScoreGetLog.getCreateTime());
//                    userScoreGetLog2.setUpdateTime(date);
//                    userScoreGetLog2.setStatus(userScoreGetLog.getStatus());
//                    userScoreGetLog2.setUserId(userScoreGetLog.getUserId());
//                    userScoreGetLog2.setBizId(userScoreLockParam.getOrderNumber());
//                    userScoreGetLog2.setUsableScore(userScoreGetLog.getUsableScore() - usableScore);
//                    userScoreGetLogService.save(userScoreGetLog2);
//                    scoreGetLogIds.add(userScoreGetLog2.getUserScoreDetailId());

                    // 2）将这条直接已使用并变成此次使用的积分
//                    userScoreGetLog.setUpdateTime(date);
//                    userScoreGetLog.setUsableScore(usableScore);
//                    userScoreGetLog.setStatus(0);
//                    updateScoreDetails.add(userScoreGetLog);
//                    scoreGetLogIds.add(userScoreGetLog.getUserScoreDetailId());
                    break;
                }
                if (usableScore <= 0) {
                    break;
                }
            }

            if (!scoreGetLogIds.isEmpty()) {
                UserScoreLock userScoreLock = new UserScoreLock();
                userScoreLock.setUserId(userId);
                userScoreLock.setScore(userScoreLockParam.getScore());
                userScoreLock.setOrderNumber(userScoreLockParam.getOrderNumber());
                userScoreLock.setScoreGetLogIds(Json.toJsonString(scoreGetLogIds));
                useScore += userScoreLock.getScore();
                userScoreLock.setStatus(0);
                userScoreLockLogs.add(userScoreLock);
                orderNumbers.add(userScoreLockParam.getOrderNumber());
            }
        }
        userScoreGetLogService.updateBatchById(updateScoreDetails);
        return useScore;
    }

}
