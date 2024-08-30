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
import com.yami.shop.bean.event.UpdateUserScoreEvent;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.order.ConfirmOrderOrder;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.model.UserScoreDetail;
import com.yami.shop.user.common.model.UserScoreLog;
import com.yami.shop.user.common.service.UserScoreDetailService;
import com.yami.shop.user.common.service.UserScoreLogService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *  用户领取积分接口
 *
 * @author LHD
 * @date 2019/12/17
 **/
@Component("updateUserScoreListener")
@AllArgsConstructor
public class UpdateUserScoreListener {

    private final UserScoreLogService scoreLogService;
    private final UserExtensionService userExtensionService;
    private final UserScoreDetailService userScoreDetailService;
    /**
     *  根据领取方式的不同添加用户积分及日志
     */
    @EventListener(UpdateUserScoreEvent.class)
    @Order(ConfirmOrderOrder.DEFAULT)
    public void receiveScoreListener(UpdateUserScoreEvent event) {
        UserExtension extension = userExtensionService.getOne(
                new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, event.getUserId()));
        Integer consumeType = event.getConsumeType();
        if(event.getScore() == null){
            return;
        }
        Long score = event.getScore();
        UserScoreLog scoreLog = new UserScoreLog();
        scoreLog.setSource(consumeType);
        scoreLog.setCreateTime(new Date());
        scoreLog.setIoType(event.getIoType());
        //添加一条日志和修改用户积分
        scoreLog.setUserId(extension.getUserId());
        scoreLog.setScore(score);
        scoreLogService.save(scoreLog);
        if(event.getIoType() == 1){
            extension.setScore(extension.getScore() + score);
            // 添加积分明细
            UserScoreDetail addDetail = new UserScoreDetail();
            addDetail.setCreateTime(new Date());
            addDetail.setStatus(1);
            addDetail.setUserId(extension.getUserId());
            addDetail.setUsableScore(score);
            userScoreDetailService.saveUserScoreDetail(addDetail);
        }else{
            extension.setScore(extension.getScore() - score);
        }
        extension.setSignDay(event.getSignDay());
        userExtensionService.updateById(extension);
    }

}
