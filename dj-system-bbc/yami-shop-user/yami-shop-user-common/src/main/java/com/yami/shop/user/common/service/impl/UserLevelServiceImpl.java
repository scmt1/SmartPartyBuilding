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
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.bo.PayInfoResultBO;
import com.yami.shop.bean.enums.PayStatus;
import com.yami.shop.bean.enums.ScoreLogType;
import com.yami.shop.bean.event.BatchBindCouponEvent;
import com.yami.shop.bean.model.PayInfo;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.param.UserUpdateParam;
import com.yami.shop.bean.pay.RefundInfoDto;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.enums.UserLevelType;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.i18n.LanguageEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dao.UserExtensionMapper;
import com.yami.shop.service.NotifyTemplateService;
import com.yami.shop.service.PayInfoService;
import com.yami.shop.service.RefundInfoService;
import com.yami.shop.service.UserService;
import com.yami.shop.user.common.dao.UserLevelMapper;
import com.yami.shop.user.common.dto.LevelDetailDto;
import com.yami.shop.user.common.dto.UserLevelDto;
import com.yami.shop.user.common.enums.GrowthLogSourceEnum;
import com.yami.shop.user.common.enums.UserRightsInfo;
import com.yami.shop.user.common.model.*;
import com.yami.shop.user.common.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 会员等级
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Service
@AllArgsConstructor
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class UserLevelServiceImpl extends ServiceImpl<UserLevelMapper, UserLevel> implements UserLevelService {

    private final UserLevelMapper userLevelMapper;
    private final UserLevelLogService userLevelLogService;
    private final NotifyTemplateService notifyTemplateService;
    private final UserScoreDetailService userScoreDetailService;
    private final UserScoreLogService userScoreLogService;
    private final UserGrowthLogService userGrowthLogService;
    private final UserService userService;
    private final UserExtensionMapper userExtensionMapper;
    private final ApplicationContext applicationContext;
    private final UserLevelCategoryService userLevelCategoryService;
    private final UserLevelCouponService userLevelCouponService;
    private final UserRightsService userRightsService;
    private final UserLevelRightsService userLevelRightsService;
    private final Snowflake snowflake;
    private final RefundInfoService refundInfoService;
    private final PayInfoService payInfoService;

    @Override
    @Cacheable(cacheNames = "UserLevelsByLevelType", key = "#userLevelType")
    public List<UserLevelDto> listUserLevelsByUserLevelType(Integer userLevelType) {
        return userLevelMapper.getList(userLevelType);
    }

    @Override
    @CacheEvict(cacheNames = "UserLevelsByLevelType", key = "#userLevelType")
    public void removeUserLevelsByUserLevelTypeCache(Integer userLevelType) {}

    @Override
    public ServerResponseEntity<String> noticeBuyVip(PayInfoResultBO payInfoResultBO, PayInfo payInfo) {
        Long userLevelLogId = Long.valueOf(payInfo.getOrderNumbers());
        UserLevelLog userLevelLog = userLevelLogService.getById(userLevelLogId);
        if (Objects.isNull(userLevelLog)) {
            log.info("购买会员订单信息异常,支付单号:" + payInfo.getPayNo());
            return ServerResponseEntity.success(payInfoResultBO.getSuccessString());
        }
        if (Objects.equals(userLevelLog.getIsPayed(), 1)) {
            RefundInfoDto refundInfo = new RefundInfoDto();
            refundInfo.setRefundSn(String.valueOf(snowflake.nextId()));
            refundInfo.setPayNo(payInfo.getPayNo());
            refundInfo.setRefundAmount(payInfo.getPayAmount());
            refundInfo.setRefundOrderNumbers(Collections.singletonList(String.valueOf(userLevelLogId)));
            refundInfo.setOnlyRefund(1);
            refundInfoService.doRefund(refundInfo);
            payInfoService.markerRefund(payInfoResultBO.getPayNo());
            return ServerResponseEntity.success(payInfoResultBO.getSuccessString());
        }

        // 支付成功
        UserLevelService userLevelService = (UserLevelService) AopContext.currentProxy();
        userLevelService.paySuccess(payInfoResultBO.getPayNo(), userLevelLog, payInfoResultBO);
        return ServerResponseEntity.success(payInfoResultBO.getSuccessString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(String payNo, UserLevelLog userLevelLog, PayInfoResultBO payInfoResultBO) {
        UserLevel userLevel = userLevelMapper.selectLevelAndCoupons(userLevelLog.getLevel());
        if (userLevel == null) {
            log.info("订单异常,会员等级找不到!" + userLevelLog.getPayNo());
            return;
        }
        PayInfo newPayInfo = new PayInfo();
        newPayInfo.setPayNo(payNo);
        newPayInfo.setBizPayNo(payInfoResultBO.getBizPayNo());
        newPayInfo.setCallbackContent(payInfoResultBO.getCallbackContent());
        newPayInfo.setCallbackTime(new Date());
        newPayInfo.setPayStatus(PayStatus.PAYED.value());
        payInfoService.update(newPayInfo, new LambdaUpdateWrapper<PayInfo>().eq(PayInfo::getPayNo, newPayInfo.getPayNo()));
        //修改用户会员信息
        User user = userService.getById(userLevelLog.getUserId());
        Integer beforeLevel = user.getLevel();
        UserExtension userExtension = userExtensionMapper.selectOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, userLevelLog.getUserId()));
        //修改用户等级
        updateUserLevel(user, userLevel, userExtension);
        //发放奖励
        ArrayList<UserLevel> userLevels = new ArrayList<>();
        userLevels.add(userLevel);
        levelUp(userLevels, userLevelLog, userExtension, null, beforeLevel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "UserLevelsByLevelType", key = "#userLevel.levelType")
    public void updateUserLevelList(UserLevel userLevel) {
        UserLevel dbUserLevel = new UserLevel();
        int sameNameCount = 0;
        //根据成长值，更新已有用户的等级
        if (Objects.nonNull(userLevel.getId())) {
            dbUserLevel = userLevelMapper.selectOne(new LambdaQueryWrapper<UserLevel>()
                    .eq(UserLevel::getId, userLevel.getId())
                    .eq(UserLevel::getLevelType, userLevel.getLevelType()));

            sameNameCount = count(new LambdaQueryWrapper<UserLevel>().eq(UserLevel::getLevelName, userLevel.getLevelName()).eq(UserLevel::getLevelType, userLevel.getLevelType()).ne(UserLevel::getId, userLevel.getId()));
            } else {
            int count = count(new LambdaQueryWrapper<UserLevel>().eq(UserLevel::getLevelType, userLevel.getLevelType()));
            count++;
            if (userLevel.getLevel() != count) {
                userLevel.setLevel(count);
            }
            sameNameCount = count(new LambdaQueryWrapper<UserLevel>().eq(UserLevel::getLevelName, userLevel.getLevelName()).eq(UserLevel::getLevelType, userLevel.getLevelType()));
        }
        //相同会员类型的等级名称不能重复
        if(sameNameCount > 0){
            throw new YamiShopBindException("yami.user.level.update.repeatLevelName.error");
        }
        // 若是普通会员，新增或修改时成长值有变化，状态设置为未更新用户等级'
        if (Objects.equals(userLevel.getLevelType(), 0) && !Objects.equals(dbUserLevel.getNeedGrowth(), userLevel.getNeedGrowth())) {
            userLevel.setStatus(-1);
        } else {
            userLevel.setStatus(1);
        }
        this.saveOrUpdate(userLevel);
        // 插入/更新等级分类数据
        this.delOrAddCategory(userLevel, dbUserLevel);
        //插入/更新等级优惠券数据
        this.delOrAddCoupon(userLevel);
        //插入/更新等级用户权益数据
        this.delOrAddRights(userLevel);
    }

    @Override
    @CacheEvict(cacheNames = "UserLevelsByLevelType", key = "#userLevel.levelType")
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserLevel(UserLevel userLevel) {
        if (Objects.equals(userLevel.getLevel(), 1)) {
            // 此等级不能删除，必须要保留最少一个等级
            throw new YamiShopBindException("yami.user.level.delete.error");
        }
        UserLevel dbUserLevel = userLevelMapper.selectById(userLevel.getId());
        //删除等级关联权益
        userLevelRightsService.remove(new LambdaQueryWrapper<UserLevelRights>().eq(UserLevelRights::getLevelId, userLevel.getId()));
        //删除等级关联的优惠券
        userLevelCouponService.remove(new LambdaQueryWrapper<UserLevelCoupon>().eq(UserLevelCoupon::getLevelId, userLevel.getId()));
        //删除等级关联的分类
        userLevelCategoryService.remove(new LambdaQueryWrapper<UserLevelCategory>().eq(UserLevelCategory::getLevelId, userLevel.getId()));
        userService.setUserLevelBylevelId(dbUserLevel.getLevel(), dbUserLevel.getLevelType());
        this.removeById(userLevel.getId());
        return true;
    }

    @Override
    public UserLevel getUserLevelById(Long id) {
        UserLevel userLevel = userLevelMapper.selectById(id);
        if (Objects.equals(userLevel.getDiscountType(), 1)) {
            List<Long> categoryIds = userLevelCategoryService.getCategoryIdByLevelId(userLevel.getId());
            userLevel.setCategorys(categoryIds);
        }
        userLevel.setUserRightsIds(userRightsService.getUserRightsIdListByLevelId(userLevel.getId(), 1));
        userLevel.setCouponList(userLevelCouponService.getCouponListByLevelId(userLevel.getId()));
        return userLevel;
    }

    @Override
    public List<LevelDetailDto> selectLevelAndRights(Integer levelType) {
        return userLevelMapper.selectLevelAndRights(levelType);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchUpdateGrowth(UserUpdateParam param) {
        Integer growthValue = param.getGrowthValue();
        List<String> userIds = param.getUserIds();
        if (Objects.isNull(growthValue) || CollectionUtils.isEmpty(userIds)) {
            return false;
        }
        if (Objects.equals(0, growthValue)) {
            return false;
        }
        for (String userId : userIds) {
            UserExtension userExtension = userExtensionMapper.getByUserId(userId);
            // 修改成长值
            addGrowthAndScore(growthValue, param.getGrowthSource(), 0L, param.getBizId(), userId, userExtension, param.getIsSendReward());
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchUpdateScore(UserUpdateParam param) {
        List<String> userIds = param.getUserIds();
        Long scoreValue = param.getScoreValue();
        if (Objects.isNull(scoreValue) || CollectionUtils.isEmpty(userIds)) {
            return false;
        }
        if (Objects.equals(0L, scoreValue)) {
            return false;
        }
        for (String userId : userIds) {
            UserExtension userExtension = userExtensionMapper.getByUserId(userId);
            // 系统直接修改积分
            addGrowthAndScore(0,  null, scoreValue, param.getBizId(), userId, userExtension, param.getIsSendReward());
        }
        return true;
    }

    /**
     * @param growthValue   增加/减少的成长值
     * @param growthSource  成长值来源
     * @param score         增加/减少的积分
     * @param bizId         业务id
     * @param userId        用户id
     * @param userExtension
     * @param isSendReward  是否发放奖励，是1 否0
     */
    private void addGrowthAndScore(Integer growthValue, Integer growthSource, Long score, String bizId, String userId, UserExtension userExtension, Integer isSendReward) {
        User user = userService.getById(userId);
        if (Objects.isNull(user.getLevel())) {
            user.setLevel(Constant.USER_LEVEL_INIT);
        }
        Integer growth = (int) growthValue;

        //判断是否升级
        //修改用户信息
        //用户＋增加成长值
        int nowGrowth = userExtension.getGrowth() == null ? growth : userExtension.getGrowth() + growth;
        long nowScore = userExtension.getScore() == null ? score : userExtension.getScore() + score;
        //用户积分最低为0
        if (nowScore < 0) {
            // 如果是减少的积分数，则判断出最多可以改变的积分数量,如果用户原本就是0就不能改变积分
            score = userExtension.getScore() == null ? 0 : -userExtension.getScore();
            nowScore = 0;
        }
        //用户成长值最低为0
        if (nowGrowth < 0) {
            // 如果是减少的成长值，则判断出最多可以改变的成长值数量,如果用户原本就是0就不能改变成长值
            growth = userExtension.getGrowth() == null ? 0 : -userExtension.getGrowth();
            nowGrowth = 0;
        }

        addScoreAndGrowth(user.getUserId(), growth, score, growthSource, bizId);
        userExtension.setGrowth(nowGrowth);
        userExtension.setScore(nowScore);
        userExtension.setUpdateTime(new Date());
        //如果用户是付费会员则不提升等级,也不会降低等级，只修改成长值
        //判断用户是否提升过等级,如果是修改用户等级并添加用户等级提升日志
        // 是否降级
        boolean isReduceLevel = false;
        List<UserLevel> userLevels = new ArrayList<>();
        if (growth > 0) {
            userLevels = userLevelMapper.selectListAndCoupons(nowGrowth, userExtension.getLevel());
        } else {
            // 查看比当前成长值小的一个等级是否和当前等级相同，如果相同就不改变，如果比当前等级小，就降低等级
            userLevels = userLevelMapper.selectListAndCouponsLtNowGrowth(nowGrowth, userExtension.getLevel());
            isReduceLevel = CollectionUtils.isNotEmpty(userLevels) && userLevels.get(0).getLevel() < userExtension.getLevel();
            userLevels = isReduceLevel ? userLevels : null;
        }
        if (user.getLevelType() == 1 || CollectionUtils.isEmpty(userLevels)) {
            // 如果是付费会员或者没有升降级，则只更新成长值等信息
            userExtensionMapper.updateById(userExtension);
        } else {
            // 新等级
            Integer level = userLevels.get(0).getLevel();
            // 旧等级
            Integer beforeLevel = user.getLevel();
            //修改用户等级
            user.setLevel(level);
            userService.updateById(user);
            // 获取用户提升过的最大等级
            Integer historyLevel = userLevelLogService.getMaxLevelByUserId(user.getUserId());
            //等级提升发放奖品及添加等级提升日志
            levelUp(userLevels, null, userExtension, historyLevel, beforeLevel);
        }

    }

    private void addScoreAndGrowth(String userId, Integer growth, Long score, Integer growthSource, String bizId) {
        if (growth != 0) {
            //添加成长值日志
            UserGrowthLog userGrowthLog = new UserGrowthLog();
            userGrowthLog.setChangeGrowth(growth);
            userGrowthLog.setSource(growthSource);
            userGrowthLog.setUserId(userId);
            if (Objects.nonNull(bizId)) {
                userGrowthLog.setBizId(bizId);
            }
            userGrowthLog.setCreateTime(new Date());
            if (Objects.nonNull(growthSource)) {
                GrowthLogSourceEnum growthLogSourceEnum = GrowthLogSourceEnum.instance(growthSource);
                if (Objects.isNull(growthLogSourceEnum)) {
                    userGrowthLog.setRemarks("系统修改用户成长值");
                } else {
                    userGrowthLog.setRemarks(Objects.equals(I18nMessage.getLang(), LanguageEnum.LANGUAGE_ZH_CN.getLang()) ? growthLogSourceEnum.getCn() : growthLogSourceEnum.getEn());
                }
            } else {
                userGrowthLog.setRemarks("系统修改用户成长值");
            }
            userGrowthLogService.save(userGrowthLog);
        }
        if (score == 0) {
            return;
        }
        // 添加积分日志
        UserScoreLog userScoreLog = new UserScoreLog();
        userScoreLog.setScore(Math.abs(score));
        userScoreLog.setUserId(userId);
        userScoreLog.setSource(ScoreLogType.SYSTEM.value());
        userScoreLog.setCreateTime(new Date());
        userScoreLog.setIoType(score > 0 ? 1 : 0);
        userScoreLogService.save(userScoreLog);
        // 修改积分明细
        if (score < 0) {
            score = Math.abs(score);
            //查询积分详细表数据
            List<UserScoreDetail> scoreDetailList = userScoreDetailService.list(new LambdaQueryWrapper<UserScoreDetail>()
                    .eq(UserScoreDetail::getUserId, userId).eq(UserScoreDetail::getStatus, 1).orderByAsc(UserScoreDetail::getCreateTime));
            List<UserScoreDetail> updateScoreDetails = new ArrayList<>();
            // 如果是负的则表示为减少积分
            // 修改积分明细，如果当前明细不够扣除在进行下一条
            // 如果够添加一条积分明细记录
            for (UserScoreDetail scoreDetail : scoreDetailList) {
                if (scoreDetail.getUsableScore() <= score) {
                    scoreDetail.setStatus(0);
                    updateScoreDetails.add(scoreDetail);
                    score -= scoreDetail.getUsableScore();
                } else {
                    UserScoreDetail addDetail = new UserScoreDetail();
                    addDetail.setCreateTime(scoreDetail.getCreateTime());
                    addDetail.setStatus(0);
                    addDetail.setUserId(scoreDetail.getUserId());
                    addDetail.setUsableScore(score);
                    userScoreDetailService.save(addDetail);

                    scoreDetail.setUsableScore(scoreDetail.getUsableScore() - score);
                    updateScoreDetails.add(scoreDetail);
                    break;
                }
                if (score <= 0) {
                    break;
                }
            }
            userScoreDetailService.updateBatchById(updateScoreDetails);
        } else {
            // 添加积分明细
            UserScoreDetail addDetail = new UserScoreDetail();
            addDetail.setStatus(1);
            addDetail.setUserId(userId);
            addDetail.setUsableScore(score);
            addDetail.setCreateTime(new Date());
            userScoreDetailService.saveUserScoreDetail(addDetail);
        }
    }

    @Override
    @Cacheable(cacheNames = "getUserLevelName", key = "#userExtension.userId")
    public String getUserLevelName(UserExtension userExtension) {
        List<UserLevel> userLevels = list(new LambdaQueryWrapper<UserLevel>()
                .le(UserLevel::getNeedGrowth, userExtension.getGrowth()).orderByDesc(UserLevel::getNeedGrowth));
        String levelName;
        if (userExtension.getLevel() == null) {
            levelName = userLevels.get(0).getLevelName();
        } else {
            levelName = String.valueOf(userExtension.getLevel());
        }
        return levelName;
    }

    @Override
    @CacheEvict(cacheNames = "UserLevelsByLevelType", key = "#userLevel.levelType")
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserLevel(UserLevel userLevel) {
        List<UserLevelDto> userLevelDtoList = this.listUserLevelsByUserLevelType(userLevel.getLevelType());
        Boolean isUpdate = false;
        Integer level = 0;
        Integer minNeedGrowth = 0;
        for (UserLevelDto userLevelDto : userLevelDtoList) {
            //成长值处于会员等级变化的区间，更新用户等级
            if (isUpdate) {
                userService.setUserLevelByGrowth(level, minNeedGrowth - 1, userLevelDto.getNeedGrowth(), userLevel.getLevelType());
                isUpdate = false;
            }
            if (Objects.equals(userLevelDto.getStatus(), -1) && Objects.equals(userLevelDto.getLevel(), userLevelDtoList.size())) {
                userService.setUserLevelByGrowth(userLevelDto.getLevel(), userLevelDto.getNeedGrowth() - 1, null, userLevel.getLevelType());
            } else if (Objects.equals(userLevelDto.getStatus(), -1)) {
                isUpdate = true;
            }
            level = userLevelDto.getLevel();
            minNeedGrowth = userLevelDto.getNeedGrowth();
        }
        userLevelMapper.setStatusByLevelType();
        // 批量将普通会员的等级与成长值不匹配的筛选出来，更新等级
        batchUpdateUserLevel(UserLevelType.ORDINARY.value());
        return true;
    }

    /**
     * 修改会员等级
     * @param levelType 会员类型
     */
    private void batchUpdateUserLevel(Integer levelType) {
        // 获取等级与普通会员成长值不匹配的用户
        List<UserExtension> userExtensionList = userExtensionMapper.getGrowthLevelMismatchUserByLevelType(levelType);
        List<UserLevel> levelDbList = list(new LambdaQueryWrapper<UserLevel>().eq(UserLevel::getLevelType, levelType)
                .orderByAsc(UserLevel::getNeedGrowth));
        List<String> userIds = userExtensionList.stream().map(UserExtension::getUserId).collect(Collectors.toList());
        if (CollUtil.isEmpty(userExtensionList)) {
            return;
        }
        List<UserLevel> userLevels;
        Integer historyLevel;
        Integer afterLevel;
        for (UserExtension userExtension : userExtensionList) {
            userLevels = new ArrayList<>();
            historyLevel = userExtension.getLevel();
            afterLevel = userExtension.getLevel();
            for (UserLevel userLevel : levelDbList) {
                if(userExtension.getGrowth() >= userLevel.getNeedGrowth()){
                    userLevels.add(userLevel);
                    afterLevel = userLevel.getLevel();
                }
            }
            // TODO 会有性能问题，暂待优化
//            //等级提升发放奖品及添加等级提升日志
//            levelUp(userLevels, null, userExtension, historyLevel, beforeLevel);
            // 消息推送-升级提醒，只有升级时进行提醒
            if (afterLevel > historyLevel) {
                User userDb = new User();
                userDb.setUserId(userExtension.getUserId());
                userDb.setUserMobile(userExtension.getUserMobile());
                notifyTemplateService.sendNotifyOfLevelUp(userLevels.get(userLevels.size() - 1).getLevelName(), userDb);
            }
        }

        // 批量修改用户等级
        userExtensionMapper.batchUpdateLevelByUserIds(userIds, levelType);
        // 将用户等级从用户扩展表同步到用户表里面
        userExtensionMapper.updateUserLevelByUserExtensionAndUserIds(userIds);
    }

    /**
     * 修改用户等级中的商品分类
     *
     * @param userLevel
     * @param dbUserLevel
     */
    private void delOrAddCategory(UserLevel userLevel, UserLevel dbUserLevel) {
        List<Long> dbCategoryIds = userLevelCategoryService.getCategoryIdByLevelId(userLevel.getId());
        List<Long> categoryList = userLevel.getCategorys();
        //如果分类数据发生改变，执行方法进行增删操作
        Map<String, List<Long>> map = this.compare(categoryList, dbCategoryIds);
        List<Long> delCategory = map.get("del");
        if (delCategory.size() > 0) {
            userLevelCategoryService.delBatch(delCategory.toArray(new Long[delCategory.size()]), userLevel.getId());
        }
        List<Long> addCategory = map.get("add");
        if (addCategory.size() > 0) {
            userLevelCategoryService.insertBatch(addCategory.toArray(new Long[addCategory.size()]), userLevel.getId());
        }
    }

    /**
     * 修改用户等级中的赠送优惠券
     *
     * @param userLevel
     */
    private void delOrAddCoupon(UserLevel userLevel) {
        List<UserLevelCoupon> userLevelCouponList = userLevelCouponService.list(new LambdaQueryWrapper<UserLevelCoupon>().eq(UserLevelCoupon::getLevelId, userLevel.getId()));
        List<Long> dbCouponIds = new ArrayList<>();
        List<Long> couponIds = new ArrayList<>();
        if (!Objects.isNull(userLevelCouponList)) {
            for (UserLevelCoupon userLevelCoupon : userLevelCouponList) {
                dbCouponIds.add(userLevelCoupon.getCouponId());
            }
        }
        if (!Objects.isNull(userLevel.getCouponList())) {
            for (Coupon coupon : userLevel.getCouponList()) {
                couponIds.add(coupon.getCouponId());
            }
        }
        Map<String, List<Long>> map = this.compare(couponIds, dbCouponIds);
        List<Long> delCoupon = map.get("del");
        if (delCoupon.size() > 0) {
            userLevelCouponService.delBatchCoupon(delCoupon.toArray(new Long[delCoupon.size()]), userLevel.getId());
        }
        List<Long> addCoupon = map.get("add");
        if (addCoupon.size() > 0) {
            userLevelCouponService.insertBatchCoupon(addCoupon.toArray(new Long[addCoupon.size()]), userLevel.getId());
        }

    }

    /**
     * 修改用户等级中的权益
     *
     * @param userLevel
     */
    private void delOrAddRights(UserLevel userLevel) {
        List<Long> userRightsList = userRightsService.getUserRightsIdListByLevelId(userLevel.getId(), null);
        List<Long> dbRightsIds = new ArrayList<>();
        List<Long> rightsIds = new ArrayList<>();
        if (!Objects.isNull(userRightsList)) {
            dbRightsIds.addAll(userRightsList);
        }
        if (!Objects.isNull(userLevel.getUserRightsIds())) {
            rightsIds.addAll(userLevel.getUserRightsIds());
            //折扣
            if (!Objects.equals(userLevel.getDiscount(), Constant.MAX_LEVEL_DISCOUNT)) {
                rightsIds.add(UserRightsInfo.DISCOUNT.value());
            }
            //包邮
            if (Objects.equals(userLevel.getIsFreeFee(), 1)) {
                rightsIds.add(UserRightsInfo.IS_FREE_FEE.value());
            }
            //赠送积分
            if (!Objects.equals(userLevel.getPresScore(), 0)) {
                rightsIds.add(UserRightsInfo.SCORE.value());
            }
            //优惠券
            if (userLevel.getCouponList().size() > 0) {
                rightsIds.add(UserRightsInfo.COUPON.value());
            }
            //积分倍率
            if (!Objects.equals(userLevel.getRateScore(), Constant.MIN_LEVEL_RATE_SCORE)) {
                rightsIds.add(UserRightsInfo.RATE_SCORE.value());
            }
        }
        Map<String, List<Long>> map = this.compare(rightsIds, dbRightsIds);
        List<Long> delCoupon = map.get("del");
        if (delCoupon.size() > 0) {
            userLevelRightsService.delBatchRights(delCoupon.toArray(new Long[delCoupon.size()]), userLevel.getId());
        }
        List<Long> addCoupon = map.get("add");
        if (addCoupon.size() > 0) {
            userLevelRightsService.insertBatchRights(addCoupon.toArray(new Long[addCoupon.size()]), userLevel.getId());
        }
    }


    /**
     * 得到需要新增或删除的数据
     *
     * @param ids
     * @param dbIds
     */
    private Map<String, List<Long>> compare(List<Long> ids, List<Long> dbIds) {
        if (Objects.isNull(ids)) {
            ids = new ArrayList<>();
        }
        List<Long> del = new ArrayList<>();
        List<Long> add = new ArrayList<>();
        del.addAll(dbIds);
        add.addAll(ids);
        add.removeAll(dbIds);
        del.removeAll(ids);
        Map<String, List<Long>> map = new HashMap<>(add.size() + del.size());
        //需要新增的数据
        map.put("add", add);
        //需要删除的数据
        map.put("del", del);
        return map;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGrowthAndScore(double growthPrice, Long score, String userId, String bizId, UserExtension userExtension, Integer type) {
        User user = userService.getById(userId);
        if (Objects.isNull(user)) {
            return;
        }
        if (user.getLevel() == null) {
            user.setLevel(Constant.USER_LEVEL_INIT);
        }
        Integer growth = (int) growthPrice;

        // 添加积分、成长值记录
        addScoreAndGrowth(bizId, userId, growth, score, type);
        // 判断是否升级
        // 修改用户信息
        // 用户＋增加成长值
        Integer nowGrowth = userExtension.getGrowth() == null ? growth : userExtension.getGrowth() + growth;
        userExtension.setScore(userExtension.getScore() == null ? score : userExtension.getScore() + score);
        userExtension.setGrowth(nowGrowth);
        userExtension.setUpdateTime(new Date());
        // 如果用户是付费会员则不提升等级,只修改成长值
        // 判断用户是否提升过等级,如果是修改用户等级并添加用户等级提升日志
        List<UserLevel> userLevels = userLevelMapper.selectListAndCoupons(nowGrowth, userExtension.getLevel());
        if (user.getLevelType() == 1 || CollectionUtils.isEmpty(userLevels)) {
            // TODO 添加积分明细
            userExtensionMapper.updateBalanceByVersion(userExtension);
        } else {
            Integer level = userLevels.get(0).getLevel();
            Integer beforeLevel = user.getLevel();
            //修改用户等级
            user.setLevel(level);
            userService.updateById(user);
            //判断用户是否提升过等级
            Integer historyLevel = userLevelLogService.getMaxLevelByUserId(user.getUserId());
            //等级提升发放奖品及添加等级提升日志
            levelUp(userLevels, null, userExtension, historyLevel, beforeLevel);
        }
    }

    /**
     * 添加积分明细、积分日志、成长值日志
     *
     * @param bizId  type = 1:订单号 type = 2:充值id
     * @param userId 用户id
     * @param growth 成长值
     * @param score  积分
     * @param type   type = 1:订单 type = 2:余额
     */
    private void addScoreAndGrowth(String bizId, String userId, Integer growth, Long score, Integer type) {
        if (score > 0) {
            //添加积分明细
            UserScoreDetail addDetail = new UserScoreDetail();
            addDetail.setCreateTime(new Date());
            addDetail.setStatus(1);
            addDetail.setUserId(userId);
            addDetail.setBizId(bizId);
            addDetail.setUsableScore(score);
            userScoreDetailService.saveUserScoreDetail(addDetail);

            //添加积分日志
            UserScoreLog userScoreLog = new UserScoreLog();
            userScoreLog.setUserId(userId);
            userScoreLog.setScore(score);
            userScoreLog.setBizId(bizId);
            if (type.equals(1)) {
                userScoreLog.setSource(ScoreLogType.SHOP.value());
            } else {
                userScoreLog.setSource(ScoreLogType.BALANCE.value());
            }
            userScoreLog.setCreateTime(new Date());
            userScoreLog.setIoType(1);
            userScoreLogService.save(userScoreLog);
        }
        if (growth > 0) {
            //添加成长值日志
            UserGrowthLog userGrowthLog = new UserGrowthLog();
            userGrowthLog.setChangeGrowth(growth);
            if (Objects.equals(type, 1)) {
                userGrowthLog.setSource(GrowthLogSourceEnum.ORDER_SUCCESS.value());
            } else {
                userGrowthLog.setSource(GrowthLogSourceEnum.BALANCE.value());
            }
            userGrowthLog.setUserId(userId);
            userGrowthLog.setCreateTime(new Date());
            userGrowthLog.setBizId(bizId);
            if (type.equals(1)) {
                userGrowthLog.setRemarks("订单确认收货获取的成长值");
            } else {
                userGrowthLog.setRemarks("用户充值余额获取的成长值");
            }
            userGrowthLogService.save(userGrowthLog);
        }
    }


    /**
     * 用户购买会员后提升会员等级
     *
     * @param user          用户信息
     * @param userLevel     用户等级
     * @param userExtension 用户扩展信息
     */
    private void updateUserLevel(User user, UserLevel userLevel, UserExtension userExtension) {
        Integer level = userLevel.getLevel();
        DateTime endTime;
        Map<Integer, Integer> dateMap = new HashMap<>(12);
        dateMap.put(1, 1);
        dateMap.put(2, 7);
        dateMap.put(3, 31);
        dateMap.put(4, 93);
        dateMap.put(5, 366);
        //修改用户等级
        if (user.getVipEndTime() == null || user.getLevel() < level || user.getLevel() > level) {
            endTime = DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, 24 * userLevel.getTerm() * dateMap.get(userLevel.getTermType()));
        } else {
            endTime = DateUtil.offset(user.getVipEndTime(), DateField.HOUR_OF_DAY, 24 * userLevel.getTerm() * dateMap.get(userLevel.getTermType()));
        }
        user.setVipEndTime(endTime);
        user.setLevel(level);
        user.setLevelType(1);
        userService.updateById(user);
        //修改用户等级积分详细表
        userExtension.setLevel(level);
        userExtension.setLevelType(1);
        userExtension.setUpdateTime(new Date());
        // 使用乐观锁进行更新
        if (userExtensionMapper.updateById(userExtension) != 1) {
            log.error("更新用户等级失败！");
            // 服务器繁忙,更新用户等级失败!
            throw new YamiShopBindException("yami.network.busy");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void levelUp(List<UserLevel> userLevels, UserLevelLog userLevelLog, UserExtension user, Integer historyLevel, Integer beforeLevel) {
        List<UserScoreLog> userScoreLogs = new ArrayList<>();
        List<UserScoreDetail> userScoreDetails = new ArrayList<>();
        //用户可能升多级，批量插入升级日志
        List<UserLevelLog> levelLogs = new ArrayList<>();
        List<Long> couponIds = new ArrayList<>();
        User userDetail = userService.getById(user.getUserId());
        Integer maxLevel = 0;
        for (UserLevel level : userLevels) {
            //修改用户扩展表等级
            boolean isLevelUp = historyLevel != null && level.getLevel() <= historyLevel;
            if (level.getLevel() > maxLevel) {
                maxLevel = level.getLevel();
            }
            user.setLevelType(level.getLevelType());
            //如果历史提升过此等级并且为普通会员就退出循环
            if (level.getLevelType() == 0 && isLevelUp) {
                continue;
            }
            //升级奖励计算(积分，优惠券，商品)
            //1.积分
            if (level.getPresScore() != null && level.getPresScore() > 0) {
                UserScoreLog userScoreLog = new UserScoreLog();
                Long score = level.getPresScore();
                userScoreLog.setUserId(user.getUserId());
                userScoreLog.setScore(score);
                userScoreLog.setSource(ScoreLogType.LEVEL_UP.value());
                userScoreLog.setCreateTime(DateUtil.date());
                userScoreLog.setIoType(1);
                userScoreLogs.add(userScoreLog);

                //添加积分明细
                UserScoreDetail addDetail = new UserScoreDetail();
                addDetail.setCreateTime(new Date());
                addDetail.setStatus(1);
                addDetail.setUserId(user.getUserId());
                addDetail.setUsableScore(score);
                userScoreDetails.add(addDetail);

                user.setScore(user.getScore() == null ? score : user.getScore() + score);
            }
            //2.优惠券
            if (CollectionUtils.isNotEmpty(level.getCouponIds())) {
                couponIds.addAll(level.getCouponIds());
            }
            //3.等级日志,如果不为空则为会员购买提升的等级就直接退出，修改一行
            if (userLevelLog != null) {
                userLevelLog.setIsPayed(1);
                userLevelLog.setState(1);
                userLevelLog.setLevelType(1);
                userLevelLogService.updateById(userLevelLog);
                break;
            }
            UserLevelLog levelLog = new UserLevelLog();
            levelLog.setUserId(user.getUserId());
            levelLog.setAddrOrderId(null);
            levelLog.setLevel(level.getLevel());
            levelLog.setCreateTime(DateUtil.date());
            levelLog.setState(1);
            levelLog.setLevelType(0);
            levelLog.setIsPayed(1);
            levelLogs.add(levelLog);
        }
        user.setLevel(maxLevel);
        // 消息推送-升级提醒，只有升级时进行提醒
        if (user.getLevel() > beforeLevel) {
            notifyTemplateService.sendNotifyOfLevelUp(userLevels.get(0).getLevelName(), userDetail);
        }
        //保存积分日志、积分明细日志
        if (CollectionUtils.isNotEmpty(userScoreLogs)) {
            userScoreLogService.saveBatch(userScoreLogs);
            userScoreDetailService.saveBatchUserScoreDetail(userScoreDetails, user.getUserId());
        }
        //修改扩展表信息
        userExtensionMapper.updateById(user);
        if (CollectionUtils.isNotEmpty(couponIds)) {
            applicationContext.publishEvent(new BatchBindCouponEvent(couponIds, user.getUserId(), 0L));
        }
        //批量保存等级日志
        if (CollectionUtils.isNotEmpty(levelLogs)) {
            userLevelLogService.saveBatch(levelLogs);
        }
    }


}
