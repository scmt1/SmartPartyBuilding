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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.user.common.dao.UserLevelRightsMapper;
import com.yami.shop.user.common.model.UserLevelRights;
import com.yami.shop.user.common.service.UserLevelRightsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 会员等级
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserLevelRightsServiceImpl extends ServiceImpl<UserLevelRightsMapper, UserLevelRights> implements UserLevelRightsService {
    private UserLevelRightsMapper userLevelRightsMapper;


    @Override
    public void insertBatchRights(Long[] rightsList, Long id) {
        userLevelRightsMapper.insertBatchRights(rightsList,id);
    }

    @Override
    public void delBatchRights(Long[] rightsList, Long id) {
        userLevelRightsMapper.delBatchRights(rightsList,id);
    }
}
