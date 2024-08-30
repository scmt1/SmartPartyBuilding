/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.platform.controller;


import cn.hutool.core.util.StrUtil;
import com.yami.shop.bean.model.Category;
import com.yami.shop.bean.param.CategoryScoreConfigParam;
import com.yami.shop.bean.param.ScoreConfigParam;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.CategoryService;
import com.yami.shop.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 积分基本配置信息
 * @author lhd
 */
@Api(tags = "积分基本配置信息")
@RestController
@RequestMapping("/user/scoreConfig")
public class ScoreConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/info/{key}")
    @PreAuthorize("@pms.hasPermission('user:scoreSonfig:info')")
    @ApiOperation(value = "获取配置信息", notes = "获取配置信息")
    @ApiImplicitParam(name = "key", value = "参数名")
    public ServerResponseEntity<ScoreConfigParam> info(@PathVariable("key") String key){
        ScoreConfigParam scoreParamDb = sysConfigService.getSysConfigObject(key,ScoreConfigParam.class);
        if(Objects.isNull(scoreParamDb)){
            ScoreConfigParam scoreParam = new ScoreConfigParam();
            addCategoryConfig(scoreParam);
            return ServerResponseEntity.success(scoreParam);
        }
        if(CollectionUtils.isEmpty(scoreParamDb.getCategoryConfigs())){
            addCategoryConfig(scoreParamDb);
        }

        if(scoreParamDb.getSignInScoreString() != null) {
            List<Integer> signScore = new ArrayList<>();
            String[] scores = scoreParamDb.getSignInScoreString().split(StrUtil.COMMA);
            for (String score : scores) {
                signScore.add(Integer.valueOf(score.trim()));
            }
            scoreParamDb.setSignInScore(signScore);
        }
        return ServerResponseEntity.success(scoreParamDb);
    }

    private void addCategoryConfig(ScoreConfigParam scoreParam) {
        List<CategoryScoreConfigParam> params = new ArrayList<>();
        List<Category> categories = categoryService.listByShopIdAndParentIdAndGrade(Constant.PLATFORM_SHOP_ID, Constant.CATEGORY_ID, 1);
        for (Category category : categories) {
            CategoryScoreConfigParam param = new CategoryScoreConfigParam();
            param.setCategoryId(category.getCategoryId());
            param.setCategoryName(category.getCategoryName());
            param.setCategoryNameEn(category.getCategoryNameEn());
            param.setGetScoreLimit(1.0);
            param.setUseScoreLimit(1.0);
            params.add(param);
        }
        scoreParam.setCategoryConfigs(params);
    }

    @SysLog("保存配置")
    @PostMapping
    @ApiOperation(value = "保存配置", notes = "保存配置")
    @PreAuthorize("@pms.hasPermission('user:scoreSonfig:save')")
    public ServerResponseEntity<Void> save(@RequestBody @Valid ScoreConfigParam scoreConfigParam){
        sysConfigService.saveOrUpdateSysConfigService(scoreConfigParam,Constant.SCORE_CONFIG);
        return ServerResponseEntity.success();
    }

}
