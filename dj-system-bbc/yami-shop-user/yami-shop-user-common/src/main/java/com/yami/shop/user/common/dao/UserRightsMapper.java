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
import com.yami.shop.user.common.model.UserRights;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员等级
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
public interface UserRightsMapper extends BaseMapper<UserRights> {

    /**
     * 更新权益状态信息
     * @param rightsId 权益id
     * @param status 状态
     * @return 是否成功更新
     */
    Boolean setStatua(@Param("rightsId") Long rightsId, @Param("status")Integer status);

    /**
     * 根据等级id，获取关联的权益列表
     * @param id 等级id
     * @param levelType 等级类型
     * @return 权益列表
     */
    List<Long> getUserRightsIdListByLevelId(@Param("id")Long id,@Param("levelType") Integer levelType);
}
