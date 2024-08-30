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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.user.common.dao.UserRightsMapper;
import com.yami.shop.user.common.model.UserLevelRights;
import com.yami.shop.user.common.model.UserRights;
import com.yami.shop.user.common.service.UserLevelRightsService;
import com.yami.shop.user.common.service.UserRightsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 会员等级
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserRightsServiceImpl extends ServiceImpl<UserRightsMapper, UserRights> implements UserRightsService {
    private UserRightsMapper userRightsMapper;
    private UserLevelRightsService userLevelRightsService;

    @Override
    public Boolean setStatua(Long rightsId, Integer status) {
        return userRightsMapper.setStatua(rightsId,status);
    }

    @Override
    public List<Long> getUserRightsIdListByLevelId(Long id ,Integer levelType) {
        return userRightsMapper.getUserRightsIdListByLevelId(id,levelType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeRights(Long rightsId) {
        boolean isSuccess = this.removeById(rightsId);
        userLevelRightsService.remove(new LambdaQueryWrapper<UserLevelRights>().eq(UserLevelRights::getRightsId,rightsId));
        return isSuccess;
    }
}
