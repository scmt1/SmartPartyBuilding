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
import com.yami.shop.user.common.dao.UserLevelCategoryMapper;
import com.yami.shop.user.common.model.UserLevelCategory;
import com.yami.shop.user.common.service.UserLevelCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Service
@AllArgsConstructor
public class UserLevelCategoryServiceImpl extends ServiceImpl<UserLevelCategoryMapper, UserLevelCategory> implements UserLevelCategoryService {

    private final UserLevelCategoryMapper userLevelCategoryMapper;

    @Override
    public List<Long> getCategoryIdByLevelId(Long id) {
        List<Long> categoryIdByLevelId = userLevelCategoryMapper.getCategoryIdByLevelId(id);
        return categoryIdByLevelId;
    }

    @Override
    public void insertBatch(Long[] categorys ,Long id) {
        userLevelCategoryMapper.insertBatch(categorys, id);
    }

    @Override
    public void delBatch(Long[] categorys, Long id) {
        userLevelCategoryMapper.delBatch(categorys, id);
    }
}
