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
import com.yami.shop.user.common.model.UserBalance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 余额充值级别表
 *
 * @author YXF
 * @date 2020-09-08 10:42:39
 */
public interface UserBalanceMapper extends BaseMapper<UserBalance> {

    /**
     * 查询余额信息
     * @param balanceId 余额id
     * @param launch  true:只查询投放状态的优惠券 false: 全部
     * @return
     */
    UserBalance getBalanceInfo(@Param("balanceId") Long balanceId,@Param("launch") Boolean launch);

    /**
     * 获取余额充值模板列表
     * @return 余额充值模板列表
     */
    List<UserBalance> getBalanceList();
}
