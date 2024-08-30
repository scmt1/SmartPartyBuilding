/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.platform.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.param.ScoreExpireParam;
import com.yami.shop.common.config.Constant;
import com.yami.shop.dao.UserMapper;
import com.yami.shop.service.SysConfigService;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.dto.LevelDetailDto;
import com.yami.shop.user.common.service.UserLevelService;
import com.yami.shop.user.common.service.UserScoreDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author yami
 */
@Slf4j
@Component
@AllArgsConstructor
public class ScoreTask {

    private final UserMapper userMapper;
    private final UserExtensionService userExtensionService;
    private final UserLevelService levelService;
    private final UserScoreDetailService userScoreDetailService;
    private final SysConfigService sysConfigService;

    /**
     * 将会员设为已过期状态
     */
    @XxlJob("vipUserClear")
    public void vipUserClear() {
        log.info("将会员设为已过期状态》》》》》》》》》》》》》》》》》》》》》");
        List<User> users = userMapper.selectMemberByEndTime(DateUtil.endOfDay(DateUtil.yesterday()));
        if (CollectionUtil.isEmpty(users)) {
            log.info("将会员设为已过期状态》》》》》》》》》》》》》》》》》》》》》");
            return;
        }
        List<String> userIds = new ArrayList<>();
        users.forEach(user -> userIds.add(user.getUserId()));
        List<UserExtension> userExtensions = userExtensionService.list(new LambdaQueryWrapper<UserExtension>().in(UserExtension::getUserId, userIds));
        List<LevelDetailDto> userLevels = levelService.selectLevelAndRights(0);
        List<User> updateUsers = new ArrayList<>();
        //通过用户积分获取当前等级和下一等级
        for (UserExtension userExtension : userExtensions) {
            userExtension.setLevelType(0);
            User user = new User();
            user.setUserId(userExtension.getUserId());
            user.setLevelType(0);
            for (LevelDetailDto userLevel : userLevels) {
                if (userExtension.getGrowth() >= userLevel.getNeedGrowth()) {
                    userExtension.setLevel(userLevel.getLevel());
                    user.setLevel(userLevel.getLevel());
                }
            }
            updateUsers.add(user);
        }
        userExtensionService.updateMemberByTime(updateUsers,userExtensions);
        log.info("将会员设为已过期状态》》》》》》》》》》》》》》》》》》》》》");
    }
    /**
     * 积分过期定时任务
     */
    @XxlJob("scoreExpireTask")
    public void scoreExpireTask() {
        // 0.计算过期时间
        ScoreExpireParam scoreExpireParam = sysConfigService.getSysConfigObject(Constant.SCORE_EXPIRE,ScoreExpireParam.class);
        if(Objects.isNull(scoreExpireParam) || !scoreExpireParam.getScoreExpireSwitch()){
            return;
        }
        int year = scoreExpireParam.getExpireYear() - 1;
        DateTime dateTime = DateUtil.offset(DateUtil.beginOfDay(new Date()), DateField.YEAR, -year);
        // 1.修改用户过期积分
        userScoreDetailService.updateExpireScoreDetail(dateTime);
    }

}
