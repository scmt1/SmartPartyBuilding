/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.user.common.model.UserLevelRights;

/**
 * 会员等级
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
public interface UserLevelRightsService extends IService<UserLevelRights> {

    /**
     * 批量插入等级权益信息
     * @param rightsList 权益列表
     * @param id 等级id
     */
    void insertBatchRights(Long[] rightsList, Long id);

    /**
     * 批量删除等级权益信息
     * @param rightsList 权益列表
     * @param id 等级id
     */
    void delBatchRights(Long[] rightsList, Long id);
}
