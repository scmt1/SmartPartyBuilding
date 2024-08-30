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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.service.UserService;
import com.yami.shop.user.common.dto.LevelDetailDto;
import com.yami.shop.user.common.dto.LevelDto;
import com.yami.shop.user.common.service.UserLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 会员等级
 *
 * @author LHD
 * @date 2019-12-19 10:27:46
 */
@RestController
@AllArgsConstructor
@RequestMapping("/p/score/scoreLevel" )
@Api(tags = "会员等级接口")
public class UserLevelController {

    private final UserLevelService levelService;
    private final UserService userService;
    private final UserExtensionService userExtensionService;

    /**
     * 获取会员等级信息页信息
     */
    @GetMapping("/page" )
    @ApiOperation(value = "会员等级信息", notes = "会员等级信息")
    @ApiImplicitParam(name = "levelType", value = "会员等级类型0为根据会员自身情况 1为付费", required = true, dataType = "int")
    public ServerResponseEntity<LevelDto> getScoreLogPage(Integer levelType) {
        String userId = SecurityUtils.getUser().getUserId();
        User user = userService.getById(userId);
        UserExtension extension = userExtensionService.getOne(
                new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, userId));
        if(levelType == 0){
            levelType = extension.getLevelType() == null ? 0:extension.getLevelType();
        }else{
            levelType = 1;
        }
        List<LevelDetailDto> userLevels = levelService.selectLevelAndRights(levelType);
        LevelDetailDto nowLevel = new LevelDetailDto();
        LevelDetailDto nextLevel = new LevelDetailDto();
        //通过用户积分获取当前等级和下一等级
        for (LevelDetailDto userLevel : userLevels) {
            if (extension.getLevel() >= userLevel.getLevel()) {
                nowLevel = userLevel;
            } else {
                nextLevel = userLevel;
                break;
            }
        }
        LevelDto userLevelDto = new LevelDto();
        userLevelDto.setNickName(user.getNickName());
        userLevelDto.setEndTime(user.getVipEndTime());
        userLevelDto.setScore(extension.getScore());
        userLevelDto.setGrowth(extension.getGrowth());
        userLevelDto.setNeedGrowth(nowLevel.getNeedGrowth());
        userLevelDto.setUserLevel(nowLevel);
        userLevelDto.setLevelType(nowLevel.getLevelType());
        userLevelDto.setLevelName(nowLevel.getLevelName());
        userLevelDto.setNextGrowth(nextLevel.getNeedGrowth() == null ?nextLevel.getNeedGrowth():nextLevel.getNeedGrowth());
        userLevelDto.setNextLevelName(nextLevel.getLevelName() == null ?nowLevel.getLevelName():nextLevel.getLevelName());
        userLevelDto.setUserLevels(userLevels);
        return ServerResponseEntity.success(userLevelDto);
    }


    /**
     * 返回金额最低的会员价格和年限
     */
    @GetMapping("/minAmountLevel" )
    @ApiOperation(value = "返回金额最低的会员价格和年限", notes = "返回金额最低的会员价格和年限")
    public ServerResponseEntity<LevelDetailDto> getScoreLogPage() {
        List<LevelDetailDto> userLevels = levelService.selectLevelAndRights(1);
        //返回 先以金额升序 期数降序
        List<LevelDetailDto> levelDetailDtos = userLevels.stream().sorted(
                Comparator.comparing(LevelDetailDto::getNeedAmount).thenComparing(LevelDetailDto::getTermType, Comparator.reverseOrder())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(levelDetailDtos)){
            return ServerResponseEntity.success();
        }
        return ServerResponseEntity.success(levelDetailDtos.get(0));
    }

}
