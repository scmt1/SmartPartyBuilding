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
import com.yami.shop.bean.enums.ScoreLogType;
import com.yami.shop.bean.event.LevelUpEvent;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.param.ScoreConfigParam;
import com.yami.shop.common.config.Constant;
import com.yami.shop.service.SysConfigService;
import com.yami.shop.user.common.model.UserLevel;
import com.yami.shop.user.common.model.UserScoreDetail;
import com.yami.shop.user.common.model.UserScoreLog;
import com.yami.shop.user.common.service.UserLevelService;
import com.yami.shop.user.common.service.UserScoreDetailService;
import com.yami.shop.user.common.service.UserScoreLogService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

/**
 * 用户等级提升操作
 *
 * @author lhd
 */
@Component("levelUpListener")
@AllArgsConstructor
public class LevelUpListener {

    private final UserLevelService userLevelService;
    private final SysConfigService sysConfigService;
    private final UserScoreDetailService userScoreDetailService;
    private final UserScoreLogService userScoreLogService;


    /**
     * 用户等级提升操作
     */
    @EventListener(LevelUpEvent.class)
    public void levelUpListener(LevelUpEvent event) {
        UserLevel userLevel = userLevelService.getOne(
                new LambdaQueryWrapper<UserLevel>().eq(UserLevel::getLevel, 1).eq(UserLevel::getLevelType, 0));
        ArrayList<UserLevel> userLevels = new ArrayList<>();
        ScoreConfigParam scoreParam = sysConfigService.getSysConfigObject(Constant.SCORE_CONFIG, ScoreConfigParam.class);
        userLevels.add(userLevel);
        UserExtension userExtension = event.getUserExtension();
        Integer beforeLevel = userExtension.getLevel();
        if(scoreParam != null && scoreParam.getRegisterScore() != null) {
            userExtension.setScore(scoreParam.getRegisterScore());
            //添加积分明细
            UserScoreDetail addDetail = new UserScoreDetail();
            addDetail.setCreateTime(new Date());
            addDetail.setStatus(1);
            addDetail.setUserId(userExtension.getUserId());
            addDetail.setUsableScore(scoreParam.getRegisterScore());
            userScoreDetailService.saveUserScoreDetail(addDetail);


            //添加积分日志
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setUserId(userExtension.getUserId());
            userScoreLog.setScore(scoreParam.getRegisterScore());
            userScoreLog.setSource(ScoreLogType.REGISTER.value());
            userScoreLog.setCreateTime(new Date());
            userScoreLog.setIoType(1);
            userScoreLogService.save(userScoreLog);
        }
        userLevelService.levelUp(userLevels,null,userExtension,null,beforeLevel);
    }


}
