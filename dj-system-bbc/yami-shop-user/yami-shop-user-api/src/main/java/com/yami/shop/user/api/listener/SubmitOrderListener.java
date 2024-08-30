/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.api.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.app.dto.ShopCartOrderMergerDto;
import com.yami.shop.bean.enums.ScoreLogType;
import com.yami.shop.bean.event.SubmitScoreOrderEvent;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.order.SubmitOrderOrder;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.model.UserScoreDetail;
import com.yami.shop.user.common.model.UserScoreLog;
import com.yami.shop.user.common.service.UserScoreDetailService;
import com.yami.shop.user.common.service.UserScoreLogService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 确认订单信息时的用户积分操作
 *
 * @author LGH
 */
@Component("userSubmitOrderListener")
@AllArgsConstructor
public class SubmitOrderListener {

    private final UserExtensionService userExtensionService;
    private final UserScoreDetailService userScoreDetailService;
    private final UserScoreLogService userScoreLogService;

    /**
     * 计算订单金额
     */
    @EventListener(SubmitScoreOrderEvent.class)
    @Order(SubmitOrderOrder.USER)
    public void userSubmitOrderListener(SubmitScoreOrderEvent event) {

        ShopCartOrderMergerDto mergerOrder = event.getMergerOrder();
        if(mergerOrder.getIsScorePay() == 0 || mergerOrder.getTotalUsableScore() <= 0){
            return;
        }
        Date now = new Date();
        String userId = SecurityUtils.getUser().getUserId();
        UserExtension userExtension = userExtensionService.getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, userId));
        //查询积分详细表数据
        List<UserScoreDetail> scoreDetailList = userScoreDetailService.list(new LambdaQueryWrapper<UserScoreDetail>()
                .eq(UserScoreDetail::getUserId, userExtension.getUserId()).eq(UserScoreDetail::getStatus,1).orderByAsc(UserScoreDetail::getCreateTime));
        if(CollectionUtils.isEmpty(scoreDetailList)){
            // 积分不足无法提交订单
            throw new YamiShopBindException("yami.user.score.no.enough");
        }
        long score = scoreDetailList.stream().mapToLong(UserScoreDetail::getUsableScore).sum();
        if(userExtension.getScore() < mergerOrder.getTotalUsableScore() || score < mergerOrder.getTotalUsableScore()){
            // 积分不足无法提交订单
            throw new YamiShopBindException("yami.user.score.no.enough");
        }

        //添加积分日志
        List<com.yami.shop.bean.model.Order> orders = event.getOrders();
        List<UserScoreLog> logList = new ArrayList<>();
        List<UserScoreDetail> updateScoreDetails = new ArrayList<>();
        for (com.yami.shop.bean.model.Order order : orders) {
            long updateScore = order.getScore();
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setUserId(userId);
            userScoreLog.setBizId(order.getOrderNumber());
            userScoreLog.setScore(order.getScore());
            userScoreLog.setSource(ScoreLogType.SCORE_CASH.value());
            userScoreLog.setCreateTime(now);
            userScoreLog.setIoType(0);
            logList.add(userScoreLog);
            // 修改积分明细，如果当前明细不够扣除在进行下一条
            // 如果够添加一条积分明细记录
            for (UserScoreDetail scoreDetail : scoreDetailList) {
                if(scoreDetail.getUsableScore() <= updateScore){
                    scoreDetail.setStatus(0);
                    scoreDetail.setBizId(order.getOrderNumber());
                    updateScoreDetails.add(scoreDetail);

                    updateScore -= scoreDetail.getUsableScore();
                }else{
                    UserScoreDetail addDetail = new UserScoreDetail();
                    addDetail.setCreateTime(scoreDetail.getCreateTime());
                    addDetail.setStatus(0);
                    addDetail.setUserId(scoreDetail.getUserId());
                    addDetail.setBizId(order.getOrderNumber());
                    addDetail.setUsableScore(updateScore);
                    userScoreDetailService.save(addDetail);

                    scoreDetail.setUsableScore(scoreDetail.getUsableScore() - updateScore);
                    updateScoreDetails.add(scoreDetail);
                    break;
                }
                if(updateScore <= 0){
                    break;
                }
            }
        }
        userScoreLogService.saveBatch(logList);
        userScoreDetailService.updateBatchById(updateScoreDetails);
        //保存用户积分
        userExtension.setScore(userExtension.getScore() - mergerOrder.getTotalUsableScore());
        userExtensionService.updateById(userExtension);
    }

}
