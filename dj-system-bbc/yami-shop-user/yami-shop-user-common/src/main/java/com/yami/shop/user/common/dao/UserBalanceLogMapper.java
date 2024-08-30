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
import com.yami.shop.bean.param.CustomerPayParam;
import com.yami.shop.bean.param.CustomerReqParam;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.dto.UserBalanceLogDto;
import com.yami.shop.user.common.model.UserBalanceLog;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 余额充值记录
 *
 * @author YXF
 * @date 2020-09-09 17:38:30
 */
public interface UserBalanceLogMapper extends BaseMapper<UserBalanceLog> {

    /**
     * 分页获取余额日志列表
     *
     * @param page   分页信息
     * @param userId 用户id
     * @return 余额日志列表
     */
    PageParam<UserBalanceLogDto> getLogPage(PageParam<UserBalanceLogDto> page, @Param("userId") String userId);

    /**
     * 统计用户总充值金额
     *
     * @param userId 用户id
     * @return 用户总充值金额
     */
    Double countUserRechargeAmount(@Param("userId") String userId);

    /**
     * 统计用户余额充值次数
     *
     * @param userId 用户id
     * @return 用户余额充值次数
     */
    Integer countUserRechargeNum(@Param("userId") String userId);

    /**
     * 获取用户最新的一次余额充值时间
     *
     * @param userId 用户id
     * @return 用户最新的一次余额充值时间
     */
    Date getRecentRechargeTime(@Param("userId") String userId);

    /**
     * 平台查询某个用户的余额明细
     *
     * @param page   分页信息
     * @param userId 用户id
     * @return 用户的余额明细列表
     */
    IPage<UserBalanceLog> getPageByUserId(PageParam<UserBalanceLog> page, @Param("userId") String userId);

    /**
     * 根据充值金额，获取用户id列表
     *
     * @param isPayed   是否支付
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param minAmount 最小金额
     * @param maxAmount 最大金额
     * @return 用户id列表
     */
    List<String> listUserIdByRechargeAmount(@Param("isPayed") Integer isPayed,
                                            @Param("startDate") Date startDate,
                                            @Param("endDate") Date endDate,
                                            @Param("minAmount") BigDecimal minAmount,
                                            @Param("maxAmount") BigDecimal maxAmount);

    /**
     * 根据充值次数，获取用户id列表
     *
     * @param isPayed   是否支付
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param minNum    最小次数
     * @param maxNum    最大次数
     * @return 用户id列表
     */
    List<String> listUserIdByRechargeNum(@Param("isPayed") Integer isPayed,
                                         @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate,
                                         @Param("minNum") Long minNum,
                                         @Param("maxNum") Long maxNum);

    /**
     * 获取充值次数大于某个值的用户id列表
     *
     * @param isPayed   是否支付
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param minNum    最小次数
     * @return 用户id列表
     */
    List<String> listUserIdByRechargeNums(@Param("isPayed") Integer isPayed,
                                         @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate,
                                         @Param("minNum") Long minNum
                                         );

    /**
     * 时间段类，进行充值的会员人数，同一个人多次充值计为1
     *
     * @param param 参数
     * @return
     */
    int countUserRechargeNumByDateRange(@Param("param") CustomerReqParam param);

    /**
     * 时间段类，进行充值的会员人数，同一个人多次充值计为1
     *
     * @param param 参数
     * @return
     */
    List<CustomerPayParam> countUserRechargeNumByTime(@Param("param") CustomerReqParam param);

    /**
     * 获取用户最新充值余额的记录
     *
     * @param userId
     * @return
     */
	UserBalanceLog getMaxCrtTimeByUserId(@Param("userId") String userId);
}
