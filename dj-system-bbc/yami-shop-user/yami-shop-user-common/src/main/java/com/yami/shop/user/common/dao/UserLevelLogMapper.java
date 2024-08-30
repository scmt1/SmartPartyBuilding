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
import com.yami.shop.bean.param.CustomerPayParam;
import com.yami.shop.bean.param.CustomerReqParam;
import com.yami.shop.bean.param.MemberGrowthDetailParam;
import com.yami.shop.bean.param.MemberGrowthReqParam;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.model.UserLevelLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户等级记录
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
public interface UserLevelLogMapper extends BaseMapper<UserLevelLog> {

    /**
     * 分页获取用户等级记录
     *
     * @param page         分页信息
     * @param userLevelLog 筛选信息
     * @return 用户等级记录列表
     */
    Page<UserLevelLog> getPage(Page<UserLevelLog> page, @Param("userLevelLog") UserLevelLog userLevelLog);

    /**
     * 获取用户最高的等级（普通会员）
     *
     * @param userId 用户id
     * @return 用户等级
     */
    Integer getMaxLevelByUserId(@Param("userId") String userId);

    /**
     * 统计升级会员数
     *
     * @param param 筛选数据
     * @return 升级会员数
     */
    Integer countGrowthMember(@Param("param") CustomerReqParam param);

    /**
     * 统计每个等级中的会员数
     *
     * @param param 筛选数据
     * @return 等级及等级中的会员数量
     */
    List<MemberGrowthDetailParam> countGrowthMemberByParam(@Param("param") MemberGrowthReqParam param);

    /**
     * 根据用户数量，筛选用户
     *
     * @param isPayed   是否支付
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param minNum    最小用户数
     * @param maxNum    最大用户数
     * @return 用户id列表
     */
    List<String> listUserIdByRechargeNum(@Param("isPayed") Integer isPayed,
                                         @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate,
                                         @Param("minNum") Long minNum,
                                         @Param("maxNum") Long maxNum);

    /**
     * 根据开始结束时间及支付状态，筛选用户
     *
     * @param isPayed   是否支付
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 用户id列表
     */
    List<String> listUserIdByEarliestRechargeTime(@Param("isPayed") Integer isPayed,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate);

    /**
     * 获取用户最新的购买等级记录信息
     *
     * @param userId 用户id
     * @return 等级记录信息
     */
    UserLevelLog getMaxCrtTimeByUserId(@Param("userId") String userId);

    /**
     * 获取付费会员购买记录
     *
     * @param page
     * @param userLevelLog
     * @return
     */
    IPage<UserLevelLog> pageBuyLevelLog(@Param("page") PageParam<UserLevelLog> page, @Param("userLevelLog") UserLevelLog userLevelLog);

    /**
     * 根据时间获取升级会员信息
     *
     * @param param 时间
     * @return 会员数量
     */
    List<CustomerPayParam> countGrowthMemberByTime(@Param("param") CustomerReqParam param);

    /**
     * 获取支付会员数
     *
     * @param param
     * @return
     */
    Integer countPayUserByTime(@Param("param") CustomerReqParam param);
}
