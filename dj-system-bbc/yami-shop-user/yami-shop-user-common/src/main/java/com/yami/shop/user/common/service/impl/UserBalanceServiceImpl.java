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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.yami.shop.bean.bo.PayInfoResultBO;
import com.yami.shop.bean.enums.PayStatus;
import com.yami.shop.bean.event.BatchBindCouponEvent;
import com.yami.shop.bean.model.PayInfo;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.param.UserUpdateParam;
import com.yami.shop.bean.pay.RefundInfoDto;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.Arith;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.dao.UserExtensionMapper;
import com.yami.shop.service.PayInfoService;
import com.yami.shop.service.RefundInfoService;
import com.yami.shop.user.common.dao.UserBalanceLogMapper;
import com.yami.shop.user.common.dao.UserBalanceMapper;
import com.yami.shop.user.common.model.Coupon;
import com.yami.shop.user.common.model.UserBalance;
import com.yami.shop.user.common.model.UserBalanceLog;
import com.yami.shop.user.common.service.UserBalanceCouponService;
import com.yami.shop.user.common.service.UserBalanceLogService;
import com.yami.shop.user.common.service.UserBalanceService;
import com.yami.shop.user.common.service.UserLevelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 余额充值级别表
 *
 * @author YXF
 * @date 2020-09-08 10:42:39
 */
@Service
@Slf4j
@AllArgsConstructor
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class UserBalanceServiceImpl extends ServiceImpl<UserBalanceMapper, UserBalance> implements UserBalanceService {

    private final UserBalanceMapper userBalanceMapper;
    private final UserExtensionMapper userExtensionMapper;
    private final UserBalanceLogMapper userBalanceLogMapper;
    private final UserBalanceLogService userBalanceLogService;
    private final UserBalanceCouponService userBalanceCouponService;
    private final ApplicationContext applicationContext;
    private final UserLevelService userLevelService;
    private final Snowflake snowflake;
    private final RefundInfoService refundInfoService;
    private final PayInfoService payInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "UserBalanceList", key = "'balanceList'")
    public void saveBalance(UserBalance userBalance) {
        userBalanceMapper.insert(userBalance);
        if (CollUtil.isEmpty(userBalance.getCouponList())) {
            return;
        }
        userBalanceCouponService.insertBatch(userBalance.getBalanceId(), userBalance.getCouponList());
    }

    @Override
    public void updateBalanceById(UserBalance userBalance) {
        userBalanceMapper.updateById(userBalance);
        UserBalance userBalanceDb = userBalanceMapper.getBalanceInfo(userBalance.getBalanceId(), false);
        Map<Long, Coupon> mapByCouponId = userBalanceDb.getCouponList().stream().collect(Collectors.toMap(Coupon::getCouponId, c -> c));
        List<Coupon> addCoupon = Lists.newArrayList();
        if (CollUtil.isEmpty(userBalance.getCouponList()) && CollUtil.isNotEmpty(userBalanceDb.getCouponList())) {
            // 之前赠送优惠券，更新为不赠送优惠券
            userBalanceCouponService.removeByBalanceId(userBalance.getBalanceId());
        } else if (CollUtil.isEmpty(userBalanceDb.getCouponList()) && CollUtil.isNotEmpty(userBalance.getCouponList())) {
            // 之前不赠送优惠券，更新为赠送优惠券
            userBalanceCouponService.insertBatch(userBalance.getBalanceId(), userBalance.getCouponList());
        } else {
            //筛选需要删除的优惠券.
            Set<Long> couponIdList = mapByCouponId.keySet();
            List<Coupon> updateList = Lists.newArrayList();
            for (Coupon coupon : userBalance.getCouponList()) {
                if (!couponIdList.contains(coupon.getCouponId())) {
                    addCoupon.add(coupon);
                    continue;
                }
                Coupon couponDb = mapByCouponId.get(coupon.getCouponId());
                if (Objects.equals(coupon.getCouponNum(), couponDb.getCouponNum())) {
                    couponIdList.remove(coupon.getCouponId());
                    continue;
                }
                updateList.add(coupon);
                couponIdList.remove(coupon.getCouponId());
            }
            if (CollUtil.isNotEmpty(couponIdList)) {
                userBalanceCouponService.removeByBalaceIdAndCouponId(userBalance.getBalanceId(), couponIdList);
            }
            if (CollUtil.isNotEmpty(addCoupon)) {
                userBalanceCouponService.insertBatch(userBalance.getBalanceId(), addCoupon);
            }
            if (CollUtil.isNotEmpty(updateList)) {
                userBalanceCouponService.updateBatchByCoupons(userBalance.getBalanceId(), updateList);
            }
        }
    }

    @Override
    public UserBalance getBalanceInfo(Long balanceId) {
        UserBalance balanceInfo = userBalanceMapper.getBalanceInfo(balanceId, true);
        List<Coupon> couponList = balanceInfo.getCouponList();
        Iterator<Coupon> iterator = couponList.iterator();
        while (iterator.hasNext()) {
            Coupon coupon = iterator.next();
            if (Objects.isNull(coupon.getCouponId())) {
                iterator.remove();
            }
        }
        return balanceInfo;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "UserBalanceList", key = "'balanceList'")
    })
    public void removeCacheByBalanceId() {
    }

    @Override
    @Cacheable(cacheNames = "UserBalanceList", key = "'balanceList'")
    public List<UserBalance> getBalanceList() {
        return userBalanceMapper.getBalanceList();
    }

    @Override
    public ServerResponseEntity<String> noticeRecharge(PayInfoResultBO payInfoResultBO, PayInfo payInfo) {
        Long balanceLogId = Long.valueOf(payInfo.getOrderNumbers());
        UserBalanceLog userBalanceLog = userBalanceLogService.getById(balanceLogId);
        if (Objects.isNull(userBalanceLog)) {
            log.info("余额充值订单信息异常,支付单号:" + payInfo.getPayNo());
            return ServerResponseEntity.success(payInfoResultBO.getSuccessString());
        }
        if (Objects.equals(userBalanceLog.getIsPayed(), 1)) {
            RefundInfoDto refundInfo = new RefundInfoDto();
            refundInfo.setRefundSn(String.valueOf(snowflake.nextId()));
            refundInfo.setRefundAmount(payInfo.getPayAmount());
            refundInfo.setPayNo(payInfo.getPayNo());
            refundInfo.setRefundOrderNumbers(Collections.singletonList(String.valueOf(balanceLogId)));
            refundInfo.setOnlyRefund(1);
            refundInfoService.doRefund(refundInfo);
            // 标记为退款
            payInfoService.markerRefund(payInfo.getPayNo());
            return ServerResponseEntity.success(payInfoResultBO.getSuccessString());
        }
        // 支付成功
        UserBalanceService userBalanceService = (UserBalanceService) AopContext.currentProxy();
        userBalanceService.paySuccess(payInfo.getPayNo(), userBalanceLog, payInfoResultBO);
        return ServerResponseEntity.success(payInfoResultBO.getSuccessString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(String payNo, UserBalanceLog userBalanceLog, PayInfoResultBO payInfoResultBO) {
        UserExtension userExtension = userExtensionMapper.selectOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, userBalanceLog.getUserId()));
        if (Objects.isNull(userExtension)) {
            log.info("余额充值的用户信息异常,用户id：" + userBalanceLog.getUserId() + ",支付单号：" + payNo);
            return;
        }
        PayInfo newPayInfo = new PayInfo();
        newPayInfo.setPayNo(payNo);
        newPayInfo.setBizPayNo(payInfoResultBO.getBizPayNo());
        newPayInfo.setCallbackContent(payInfoResultBO.getCallbackContent());
        newPayInfo.setCallbackTime(new Date());
        newPayInfo.setPayStatus(PayStatus.PAYED.value());
        payInfoService.update(newPayInfo, new LambdaUpdateWrapper<PayInfo>().eq(PayInfo::getPayNo, newPayInfo.getPayNo()));
        //保存用户充值的金额及相关数据
        saveUserBalanceData(userBalanceLog, userExtension);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchUpdateUserBalance(UserUpdateParam param) {
        Double balanceValue = param.getBalanceValue();

        List<String> userIds = param.getUserIds();
        if (Objects.equals(0.0, balanceValue) || CollectionUtils.isEmpty(userIds)) {
            return false;
        }
        Date now = new Date();
        // 批量给用户充值
        List<UserExtension> extensions = userExtensionMapper.listByUserIds(userIds);
        List<UserExtension> updateExtensions = new ArrayList<>();
        List<UserBalanceLog> userBalanceLogs = new ArrayList<>();
        for (UserExtension extension : extensions) {
            if (Objects.isNull(extension)) {
                continue;
            }
            UserExtension update = new UserExtension();
            update.setUserExtensionId(extension.getUserExtensionId());
            // 用户改变的余额数值
            Double changeBalance = Math.abs(balanceValue);
            Double totalBalance = Objects.nonNull(extension.getTotalBalance()) ? extension.getTotalBalance() : 0.0;
            double userBalance = Arith.add(totalBalance, balanceValue);
            if (userBalance < 0) {
                //修改后的金额 < 0 ，那么减少的金额就是用户原本的余额
                changeBalance = totalBalance;
            }
            // 修改后的金额大于最大值， 则变化金额 = 最大值 - 用户余额
            else if (userBalance > Constant.MAX_USER_BALANCE) {
                changeBalance = Constant.MAX_USER_BALANCE - totalBalance;
            }
            update.setChangeBalance(balanceValue);
            update.setUpdateTime(now);
            updateExtensions.add(update);

            // 添加日志
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setUserId(extension.getUserId());
            userBalanceLog.setCreateTime(now);
            userBalanceLog.setChangeBalance(changeBalance);
            userBalanceLog.setIoType(balanceValue > 0 ? 1 : 0);
            userBalanceLog.setType(5);
            userBalanceLogs.add(userBalanceLog);
        }
        if (CollectionUtils.isEmpty(updateExtensions)) {
            return false;
        }
        userExtensionMapper.updateBatchUserBalanceById(updateExtensions);
        userBalanceLogService.saveBatch(userBalanceLogs);
        return true;
    }

    @Override
    public IPage<UserBalanceLog> getPageByUserId(PageParam<UserBalanceLog> page, String userId) {
        return userBalanceLogMapper.getPageByUserId(page, userId);
    }


    private void saveUserBalanceData(UserBalanceLog userBalanceLog, UserExtension userExtension) {
        if (-1L == userBalanceLog.getBalanceId()) {
            Double totalBanlance = Objects.isNull(userExtension.getTotalBalance()) ? 0 : userExtension.getTotalBalance();
            userBalanceLog.setIsPayed(1);
            userBalanceLogMapper.updateById(userBalanceLog);
            userExtension.setTotalBalance(Arith.add(userBalanceLog.getChangeBalance(), totalBanlance));
            userExtension.setBalance(Arith.add(userExtension.getBalance(), userBalanceLog.getChangeBalance()));
            //赠送成长值 (若提升等级会对user信息进行更改，所以成长值最后计算，且先保存之前操作的用户信息)
            userLevelService.addGrowthAndScore(0, 0L, userExtension.getUserId(), "-1", userExtension, 2);
        } else {
            //充值余额记录
            UserBalance userBalance = getBalanceInfo(userBalanceLog.getBalanceId());
            Double totalBanlance = Objects.isNull(userExtension.getTotalBalance()) ? 0 : userExtension.getTotalBalance();
            userBalanceLog.setIsPayed(1);
            userBalanceLogMapper.updateById(userBalanceLog);
            userExtension.setTotalBalance(Arith.add(userBalanceLog.getChangeBalance(), totalBanlance));
            userExtension.setBalance(Arith.add(userExtension.getBalance(), userBalanceLog.getChangeBalance()));
            Double presAmount = userBalance.getPresAmount();
            if (presAmount > 0) {
                //余额充值赠送记录
                UserBalanceLog presLog = new UserBalanceLog();
                presLog.setChangeBalance(presAmount);
                presLog.setCreateTime(new Date());
                presLog.setIoType(1);
                presLog.setType(2);
                presLog.setUserId(userExtension.getUserId());
                userBalanceLogMapper.insert(presLog);
                userExtension.setTotalBalance(Arith.add(userExtension.getTotalBalance(), presAmount));
                userExtension.setBalance(Arith.add(userExtension.getBalance(), presAmount));
            }
            //赠送优惠券
            if (CollUtil.isNotEmpty(userBalance.getCouponList())) {
                List<Long> couponIds = Lists.newArrayList();
                for (Coupon coupon : userBalance.getCouponList()) {
                    for (int i = 0; i < coupon.getCouponNum(); i++) {
                        couponIds.add(coupon.getCouponId());
                    }
                }
                applicationContext.publishEvent(new BatchBindCouponEvent(couponIds, userExtension.getUserId(), 0L));
            }

            //赠送成长值 (若提升等级会对user信息进行更改，所以成长值最后计算，且先保存之前操作的用户信息)
            Integer growth = userBalance.getPresGrowth();
            Long score = userBalance.getPresScore();
            if (growth > 0 || score > 0) {
                userLevelService.addGrowthAndScore(growth, score, userExtension.getUserId(), userBalance.getBalanceId().toString(), userExtension, 2);
            } else {
                userExtensionMapper.updateById(userExtension);
            }
        }
    }
}
