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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yami.shop.user.common.model.UserTag;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 客户标签
 *
 * @author LGH
 * @date 2020-09-09 11:31:16
 */
public interface UserTagMapper extends BaseMapper<UserTag> {

    /**
     * 批量修改标签人数
     * @param tagUser 标签人数map
     */
    void updateBatch(@Param("tagUser") Map<Long, Integer> tagUser);

    /**
     * 根据参数查询标签列表
     * @param page 分页参数
     * @param tagType 标签类型
     * @param tagName 标签名称
     * @return 标签列表
     */
    IPage<UserTag> pageUserTagByParam(Page page, @Param("tagType") Integer tagType, @Param("tagName") String tagName);

}
