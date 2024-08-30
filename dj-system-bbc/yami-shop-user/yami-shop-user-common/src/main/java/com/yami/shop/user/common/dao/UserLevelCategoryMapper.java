/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.user.common.model.UserLevelCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 等级分类关联
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
public interface UserLevelCategoryMapper extends BaseMapper<UserLevelCategory> {

    /**
     * 根据等级id，获取等级关联的分类id列表
     * @param id 等级id
     * @return 分类id列表
     */
    List<Long> getCategoryIdByLevelId(@Param("id") Long id);

    /**
     * 批量保存等级分类关联信息
     * @param categorys 分类id列表
     * @param id 等级id
     */
    void insertBatch(@Param("categorys") Long[] categorys, @Param("id") Long id);

    /**
     * 批量删除等级分类关联信息
     * @param categorys 分类id列表
     * @param id 等级id
     */
    void delBatch(@Param("categorys") Long[] categorys, @Param("id") Long id);
}
