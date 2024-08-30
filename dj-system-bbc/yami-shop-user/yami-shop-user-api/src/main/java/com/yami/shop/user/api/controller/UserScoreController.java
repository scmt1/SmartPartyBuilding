/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.api.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.ScoreLogType;
import com.yami.shop.bean.event.UpdateUserScoreEvent;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.param.ScoreConfigParam;
import com.yami.shop.bean.param.ScoreExpireParam;
import com.yami.shop.common.bean.SysConfig;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.SysConfigService;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.dto.ScoreDataDto;
import com.yami.shop.user.common.model.UserLevel;
import com.yami.shop.user.common.model.UserScoreDetail;
import com.yami.shop.user.common.model.UserScoreLog;
import com.yami.shop.user.common.service.UserLevelService;
import com.yami.shop.user.common.service.UserScoreDetailService;
import com.yami.shop.user.common.service.UserScoreLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 积分中心接口
 *
 * @author LHD
 * @date 2019-3-18 10:27:46
 */
@RestController
@RequestMapping("/p/score")
@Api(tags = "积分中心接口")
@AllArgsConstructor
public class UserScoreController {

    private final UserScoreLogService scoreLogService;
    private final UserScoreDetailService userScoreDetailService;
    private final ApplicationContext applicationContext;
    private final SysConfigService sysConfigService;
    private final UserExtensionService userExtensionService;
    private final UserLevelService userLevelService;


    @GetMapping("/updateUserScore")
    @ApiOperation(value = "积分签到", notes = "积分签到")
    public ServerResponseEntity<String> updateUserScore() {
        String userId = SecurityUtils.getUser().getUserId();
        UserExtension extension = userExtensionService.getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, userId));
        ScoreConfigParam scoreParam = sysConfigService.getSysConfigObject(Constant.SCORE_CONFIG, ScoreConfigParam.class);
        //签到，计算连续签到日期
        if (isSignIn(userId, DateUtil.date())) {
            // 今天已经签到过了，请明天再试!
            return ServerResponseEntity.success(I18nMessage.getMessage("yami.user.receive.score.check"));
        }
        //根据逗号分隔
        String[] signInScore = scoreParam.getSignInScoreString().split(StrUtil.COMMA);
        int count = Math.min(extension.getSignDay(), signInScore.length - 1);
        long score = Long.parseLong(signInScore[count]);
        int signDay;
        // 查询昨天有没有签到,没有则初始化为1
        if (isSignIn(userId, DateUtil.offsetDay(DateUtil.date(), -1))) {
            signDay = extension.getSignDay() + 1;
        } else {
            signDay = 1;
        }
        String remarks = "签到第" + count + "天获取的积分";
        applicationContext.publishEvent(new UpdateUserScoreEvent(ScoreLogType.SIGN_IN.value(), score, 1,
                null, remarks, userId,signDay));
        // 领取积分成功
        return ServerResponseEntity.success(I18nMessage.getMessage("yami.user.receive.score"));
    }


    @GetMapping("/scoreInfo")
    @ApiOperation(value = "查看积分中心信息", notes = "查看积分中心信息")
    public ServerResponseEntity<ScoreDataDto> scoreInfo() {
        ScoreDataDto scoreDataDto = new ScoreDataDto();
        String userId = SecurityUtils.getUser().getUserId();
        ScoreConfigParam scoreParam = sysConfigService.getSysConfigObject(Constant.SCORE_CONFIG, ScoreConfigParam.class);
        // 0.计算过期时间
        ScoreExpireParam scoreExpireParam = sysConfigService.getSysConfigObject(Constant.SCORE_EXPIRE, ScoreExpireParam.class);
        if (Objects.isNull(scoreParam)) {
            return ServerResponseEntity.success();
        }
        Integer year = Objects.isNull(scoreExpireParam) ? 0 : scoreExpireParam.getExpireYear();
        ArrayList<Integer> signInScores = new ArrayList<>();
        for (String s : scoreParam.getSignInScoreString().trim().split(StrUtil.COMMA)) {
            Integer signInScore = Integer.valueOf(s);
            signInScores.add(signInScore);
        }
        UserExtension userExtension = userExtensionService.getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, userId));
        if (userExtension == null) {
            return ServerResponseEntity.success();
        }
        // 查询连续签到天数不大于0并且没有连续签到初始化为0
        boolean isContinuousSign = !isSignIn(userId,DateUtil.offsetDay(DateUtil.date(),-1)) && !isSignIn(userId,DateUtil.date())
                && (Objects.isNull(userExtension.getSignDay()) || userExtension.getSignDay() != 0);
        if(isContinuousSign) {
            userExtension.setSignDay(0);
            userExtensionService.updateById(userExtension);
        }
        UserScoreDetail userScoreDetail = userScoreDetailService.getOne(new LambdaQueryWrapper<UserScoreDetail>().eq(UserScoreDetail::getUserId, userId)
                .eq(UserScoreDetail::getStatus, -1).orderByDesc(UserScoreDetail::getExpireTime).last("limit 1"));
        scoreDataDto.setIsRegister(1);
        scoreDataDto.setExpireScore(Objects.nonNull(userScoreDetail) ? userScoreDetail.getUsableScore() : 0L);
        scoreDataDto.setExpireYear(year);
        scoreDataDto.setScoreExpireSwitch(scoreExpireParam.getScoreExpireSwitch());
        scoreDataDto.setGrowth(userExtension.getGrowth());
        scoreDataDto.setScore(userExtension.getScore());
        scoreDataDto.setScoreList(signInScores);
        scoreDataDto.setRegisterScore(scoreParam.getRegisterScore());
        scoreDataDto.setShopScore(scoreParam.getShopGetScore());
        scoreDataDto.setIsSignIn(isSignIn(userId, DateUtil.date()) ? 1 : 0);
        scoreDataDto.setLevelType(userExtension.getLevelType());
        UserLevel userLevel = userLevelService.getOne(new LambdaQueryWrapper<UserLevel>()
                .eq(UserLevel::getLevel, userExtension.getLevel())
                .eq(UserLevel::getLevelType, userExtension.getLevelType())
        );
        scoreDataDto.setLevelName(userLevel.getLevelName());
        //计算签到天数
        int count = userExtension.getSignDay() <= 0 ? 1 : userExtension.getSignDay();
        // 如果不为第一天签到或者大于等于第七天的签到，+1
        if (userExtension.getSignDay() != 0 && !isSignIn(userId, DateUtil.date())) {
            count++;
        }
        scoreDataDto.setSignInCount(count);
        return ServerResponseEntity.success(scoreDataDto);
    }

    /**
     * 是否已经签到
     *
     * @param userId
     * @param date
     * @return
     */
    private boolean isSignIn(String userId, Date date) {
        List<UserScoreLog> scoreList = scoreLogService.list(new LambdaQueryWrapper<UserScoreLog>().eq(UserScoreLog::getSource, ScoreLogType.SIGN_IN.value())
                .ge(UserScoreLog::getCreateTime, DateUtil.beginOfDay(date))
                .le(UserScoreLog::getCreateTime, DateUtil.endOfDay(date))
                .eq(UserScoreLog::getUserId, userId));
        return CollectionUtils.isNotEmpty(scoreList);
    }

    /**
     * 分页查询积分明细
     *
     * @param page 分页对象
     * @return 分页数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "查询积分明细", notes = "查询积分明细")
    public ServerResponseEntity<IPage<UserScoreLog>> getScoreLogPage(PageParam<UserScoreLog> page) {
        String userId = SecurityUtils.getUser().getUserId();
        return ServerResponseEntity.success(scoreLogService.getPageByUserId(page, userId));
    }


    /**
     * 等级页展示
     *
     * @return 等级页展示
     */
    @GetMapping("/getLevelShow")
    @ApiOperation(value = "等级页展示", notes = "等级页展示")
    public ServerResponseEntity<String> getLevelShow() {
        String config = sysConfigService.getSysConfigObject(Constant.LEVEL_SHOW, String.class);
        if (Objects.isNull(config)) {
            return ServerResponseEntity.success();
        }
        return ServerResponseEntity.success(config);
    }

    /**
     * 积分常见问题
     *
     * @return 积分常见问题
     */
    @GetMapping("/getScoreQuestion")
    @ApiOperation(value = "积分常见问题", notes = "积分常见问题")
    public ServerResponseEntity<SysConfig> getScoreQuestion() {
        SysConfig config = sysConfigService.getOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getParamKey, Constant.SCORE_QUESTION));
        if (Objects.isNull(config)) {
            return ServerResponseEntity.success(new SysConfig());
        }
        return ServerResponseEntity.success(config);
    }

}
