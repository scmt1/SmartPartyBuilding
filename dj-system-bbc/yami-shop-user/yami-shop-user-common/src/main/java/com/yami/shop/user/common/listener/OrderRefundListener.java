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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.dto.OrderRefundDto;
import com.yami.shop.bean.enums.ScoreLogType;
import com.yami.shop.bean.event.OrderRefundSuccessEvent;
import com.yami.shop.bean.event.RefundGrowthEvent;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.param.UserUpdateParam;
import com.yami.shop.service.OrderService;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.enums.GrowthLogSourceEnum;
import com.yami.shop.user.common.model.UserGrowthLog;
import com.yami.shop.user.common.model.UserScoreDetail;
import com.yami.shop.user.common.model.UserScoreLog;
import com.yami.shop.user.common.service.UserGrowthLogService;
import com.yami.shop.user.common.service.UserLevelService;
import com.yami.shop.user.common.service.UserScoreDetailService;
import com.yami.shop.user.common.service.UserScoreLogService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 退款订单监听
 * @author lhd
 */
@Component("commentOrderRefundListener")
@AllArgsConstructor
public class OrderRefundListener {

    private final UserExtensionService userExtensionService;
    private final UserScoreLogService userScoreLogService;
    private final UserScoreDetailService userScoreDetailService;
    private final OrderService orderService;
    private final UserGrowthLogService userGrowthLogService;
    private final UserLevelService userLevelService;


    /**
     * 退还用户此笔退款订单使用的积分
     */
    @EventListener(OrderRefundSuccessEvent.class)
    public void userOrderRefundSuccessEvent(OrderRefundSuccessEvent event) {
        OrderRefundDto orderRefundDto = event.getOrderRefundDto();
        //获取退款的所有订单项
        if (CollectionUtils.isEmpty(orderRefundDto.getOrderItems())) {
            return;
        }
        Order order = orderService.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNumber, orderRefundDto.getOrderNumber()));
        UserExtension userExtension = userExtensionService.getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId,order.getUserId()));
        List<UserScoreLog> scoreLogs = new ArrayList<>();
        List<UserScoreDetail> updateScoreDetails = new ArrayList<>();
        //退还用户此笔退款订单使用的积分,如果有确认收货还需扣减掉获取的积分。
        //先判断当时抵现的积分是否过期，有的话和需要退还的积分进行取小值退还
        int addScore = 0;
        List<UserScoreDetail> scoreDetails = userScoreDetailService.list(new LambdaUpdateWrapper<UserScoreDetail>()
                .eq(UserScoreDetail::getBizId, order.getOrderNumber()).eq(UserScoreDetail::getStatus, 0));
        if(CollectionUtils.isNotEmpty(scoreDetails)) {
            //要修改的用户积分，状态为-1表示还没过期的可以返还的用户积分
            for (UserScoreDetail scoreDetail : scoreDetails) {
                addScore += scoreDetail.getUsableScore();
            }
        }
        List<OrderItem> orderItems = orderRefundDto.getOrderItems();
        long totalScore = 0;
        long totalGainScore = 0;
        for (OrderItem orderItem : orderItems) {
            totalScore += orderItem.getUseScore();
            //确认收货时赠送的积分
            totalGainScore += orderItem.getGainScore() == null?0:orderItem.getGainScore();
        }
        //根据订单需要退还的抵现积分进行取小值
        totalScore = Math.min(addScore,totalScore);
        if(totalScore > 0) {
            //添加积分日志
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setUserId(order.getUserId());
            userScoreLog.setScore(totalScore);
            userScoreLog.setBizId(order.getOrderNumber());
            userScoreLog.setSource(ScoreLogType.SCORE_CASH.value());
            userScoreLog.setCreateTime(new Date());
            userScoreLog.setIoType(1);
            scoreLogs.add(userScoreLog);
            userExtension.setScore(userExtension.getScore() + totalScore);

            //积分明细修改、添加明细
            for (UserScoreDetail scoreDetail : scoreDetails) {
                if(totalScore >= scoreDetail.getUsableScore()) {
                    scoreDetail.setStatus(1);
                    updateScoreDetails.add(scoreDetail);
                }else{
                    scoreDetail.setUsableScore(scoreDetail.getUsableScore() - totalScore);
                    updateScoreDetails.add(scoreDetail);

                    UserScoreDetail addDetail = new UserScoreDetail();
                    addDetail.setCreateTime(scoreDetail.getCreateTime());
                    addDetail.setStatus(1);
                    addDetail.setUserId(scoreDetail.getUserId());
                    addDetail.setUsableScore(totalScore);
                    userScoreDetailService.saveUserScoreDetail(addDetail);
                    break;
                }
                totalScore -= scoreDetail.getUsableScore();
                if(totalScore <= 0){
                    break;
                }
            }
        }
        //确认收货后的退款,还需将用户完成此笔订单获取的积分扣除
        if(totalGainScore > 0){
            userExtension.setScore(userExtension.getScore() - totalGainScore);
            receiptRefundScore(order,scoreLogs,updateScoreDetails,totalGainScore);
        }
        //如果不为空则批量保存积分日志
        if(CollectionUtils.isNotEmpty(scoreLogs)){
            userScoreLogService.saveBatch(scoreLogs);
        }
        if(CollectionUtils.isNotEmpty(updateScoreDetails)) {
            userScoreDetailService.updateBatchById(updateScoreDetails);
        }
        userExtensionService.updateById(userExtension);
    }

    /**
     * 退还用户此笔退款订单获得的成长值
     */
    @EventListener(RefundGrowthEvent.class)
    public void refundGrowth(RefundGrowthEvent event) {
        String orderNumber = event.getOrderNumber();
        UserGrowthLog growthLog = userGrowthLogService.getOne(Wrappers.lambdaQuery(UserGrowthLog.class)
                .eq(UserGrowthLog::getBizId, orderNumber)
                .eq(UserGrowthLog::getSource, GrowthLogSourceEnum.ORDER_SUCCESS.value())
        );
        // 如果没有找到这条增加成长值的记录，就不用退成长值了
        if (Objects.isNull(growthLog)) {
            return;
        }
        // 需要减少成长值数值
        Integer growth = growthLog.getChangeGrowth();
        if (Objects.equals(growth, 0)) {
            return;
        }
        String userId = growthLog.getUserId();
        UserUpdateParam param = new UserUpdateParam();
        param.setGrowthValue(-growth);
        param.setBizId(orderNumber);
        param.setGrowthSource(GrowthLogSourceEnum.ORDER_FAIL.value());
        param.setUserIds(Collections.singletonList(userId));

        userLevelService.batchUpdateGrowth(param);
    }

    private void receiptRefundScore(Order order, List<UserScoreLog> scoreLogs,List<UserScoreDetail> updateScoreDetails, Long totalGainScore) {
        List<UserScoreDetail> usableUserScore = userScoreDetailService.list(new LambdaUpdateWrapper<UserScoreDetail>()
                .eq(UserScoreDetail::getStatus, 1)
                .eq(UserScoreDetail::getUserId,order.getUserId())
                .orderByAsc(UserScoreDetail::getCreateTime));
        long sumScore = usableUserScore.stream().mapToLong(UserScoreDetail::getUsableScore).sum();
//        if(totalGainScore > sumScore){
//            // 用户积分不足
//            throw new YamiShopBindException("yami.user.score.enough");
//        }
        //添加积分日志
        UserScoreLog log = new UserScoreLog();
        log.setUserId(order.getUserId());
        log.setScore(totalGainScore);
        log.setBizId(order.getOrderNumber());
        log.setSource(ScoreLogType.SHOP.value());
        log.setCreateTime(new Date());
        log.setIoType(0);
        scoreLogs.add(log);

        // 修改积分明细，如果当前明细不够扣除在进行下一条
        // 如果够添加一条积分明细记录
        for (UserScoreDetail scoreDetail : usableUserScore) {
            if(scoreDetail.getUsableScore() <= totalGainScore){
                scoreDetail.setBizId(order.getOrderNumber());
                scoreDetail.setStatus(0);
                updateScoreDetails.add(scoreDetail);
                totalGainScore -= scoreDetail.getUsableScore();
            }else{
                UserScoreDetail addDetail = new UserScoreDetail();
                addDetail.setBizId(order.getOrderNumber());
                addDetail.setStatus(0);
                addDetail.setCreateTime(scoreDetail.getCreateTime());
                addDetail.setUsableScore(totalGainScore);
                addDetail.setUserId(scoreDetail.getUserId());
                userScoreDetailService.save(addDetail);

                scoreDetail.setUsableScore(scoreDetail.getUsableScore() - totalGainScore);
                updateScoreDetails.add(scoreDetail);
                break;
            }
        }
    }
}
