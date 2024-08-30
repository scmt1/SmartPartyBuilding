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
import com.yami.shop.bean.param.UserTagParam;
import com.yami.shop.user.common.dao.UserTagMapper;
import com.yami.shop.user.common.dao.UserTagUserMapper;
import com.yami.shop.user.common.model.UserTagUser;
import com.yami.shop.user.common.service.UserTagUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户和标签关联表
 *
 * @author LGH
 * @date 2020-09-10 08:44:22
 */
@Service
@AllArgsConstructor
public class UserTagUserServiceImpl extends ServiceImpl<UserTagUserMapper, UserTagUser> implements UserTagUserService {

    private final UserTagUserMapper userTagUserMapper;
    private final UserTagMapper userTagMapper;

    @Override
    public UserTagUser getByUserIdAndTagId(String userId, Long tagId) {
        return userTagUserMapper.getByUserIdAndTagId(userId,tagId);
    }

    @Override
    public Integer countByUserIdAndTagId(String userId, Long tagId) {
        Integer count = userTagUserMapper.countByUserIdAndTagId(userId,tagId);
        return Objects.isNull(count)? 0 : count;
    }

    @Override
    public List<UserTagParam> getUserTagsUserByUserId(String userId) {
        List<UserTagParam> userTagParams = userTagUserMapper.getUserTagsUserByUserId(userId);
        return userTagParams;
    }

    @Override
    public Boolean removeByUserIdAndTagId(String userId, Long userTagId) {
        boolean a = userTagUserMapper.removeByUserIdAndTagId(userId,userTagId) > 0;
        Map<Long, Integer> tagUser = new HashMap<>(16);
        tagUser.put(userTagId,-1);
        userTagMapper.updateBatch(tagUser);
        return a;
    }

}
