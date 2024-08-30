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


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.bo.PayInfoResultBO;
import com.yami.shop.bean.model.PayInfo;
import com.yami.shop.bean.param.UserUpdateParam;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.user.common.model.UserBalance;
import com.yami.shop.user.common.model.UserBalanceLog;

import java.util.List;

/**
 * 余额充值级别表
 *
 * @author YXF
 * @date 2020-09-08 10:42:39
 */
public interface UserBalanceService extends IService<UserBalance> {

    /**
     * 保存余额充值模板
     * @param userBalance 余额充值模板
     */
    void saveBalance(UserBalance userBalance);

    /**
     * 更新余额充值模板
     * @param userBalance 余额充值模板
     */
    void updateBalanceById(UserBalance userBalance);

    /**
     * 获取余额充值模板信息
     * @param balanceId 余额充值模板id
     * @return 余额充值模板信息
     */
    UserBalance getBalanceInfo(Long balanceId);

    /**
     * 清除缓存
     */
    void removeCacheByBalanceId();

    /**
     * 获取余额充值模板列表
     * @return 余额充值模板列表
     */
    List<UserBalance> getBalanceList();

    /**
     * 余额充值回调
     * @param payInfoResultBO
     * @param payInfo
     * @return
     */
    ServerResponseEntity<String> noticeRecharge(PayInfoResultBO payInfoResultBO, PayInfo payInfo);

    /**
     * 余额充值支付成功处理
     * @param payNo 支付单号
     * @param userBalanceLog
     * @param payInfoResultBO
     */
    void paySuccess(String payNo, UserBalanceLog userBalanceLog, PayInfoResultBO payInfoResultBO);

    /**
     *批量给用户充值余额
     * @param param 用户余额更新数据
     * @return 是否充值成功
     */
    Boolean batchUpdateUserBalance(UserUpdateParam param);

    /**
     * 分页获取用户余额充值记录列表
     * @param page 分页信息
     * @param userId 用户id
     * @return 用户余额充值记录列表
     */
    IPage<UserBalanceLog> getPageByUserId(PageParam<UserBalanceLog> page, String userId);

}
