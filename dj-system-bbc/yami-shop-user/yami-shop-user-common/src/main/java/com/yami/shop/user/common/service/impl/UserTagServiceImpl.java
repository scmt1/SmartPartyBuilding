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

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.enums.OrderStatus;
import com.yami.shop.bean.enums.RecentDaysType;
import com.yami.shop.bean.enums.RemindType;
import com.yami.shop.bean.enums.SendType;
import com.yami.shop.bean.model.*;
import com.yami.shop.bean.param.UserUpdateParam;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.dao.NotifyTemplateTagMapper;
import com.yami.shop.dao.OrderMapper;
import com.yami.shop.dao.UserMapper;
import com.yami.shop.service.NotifyLogService;
import com.yami.shop.service.NotifyTemplateService;
import com.yami.shop.service.OrderService;
import com.yami.shop.service.UserService;
import com.yami.shop.user.common.dao.UserBalanceLogMapper;
import com.yami.shop.user.common.dao.UserLevelLogMapper;
import com.yami.shop.user.common.dao.UserTagMapper;
import com.yami.shop.user.common.dto.UserTagDto;
import com.yami.shop.user.common.model.UserTag;
import com.yami.shop.user.common.model.UserTagUser;
import com.yami.shop.user.common.service.UserTagService;
import com.yami.shop.user.common.service.UserTagUserService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 客户标签
 *
 * @author LGH
 * @date 2020-09-09 11:31:16
 */
@Service
@AllArgsConstructor
public class UserTagServiceImpl extends ServiceImpl<UserTagMapper, UserTag> implements UserTagService {

    private final UserTagUserService userTagUserService;

    private final UserService userService;

    private final OrderService orderService;
    private final NotifyTemplateTagMapper notifyTemplateTagMapper;
    private final NotifyTemplateService notifyTemplateService;
    private final NotifyLogService notifyLogService;

    private final UserBalanceLogMapper userBalanceLogMapper;

    private final OrderMapper orderMapper;

    private final UserLevelLogMapper userLevelLogMapper;
    private final UserTagMapper userTagMapper;
    private final UserMapper userMapper;

    @Override
    public boolean addUserTag(UserTagDto userTagDto) {
        UserTag userTag = new UserTag();
        userTag.setTagName(userTagDto.getTagName());
        userTag.setTagType(userTagDto.getTagType());
        userTag.setIsSysTurnOn(0);
        switch (userTagDto.getTagType()) {
            // 手动标签
            case 0:
                //todo
                break;
            // 条件标签
            case 1:
                fillInCustomOptionalCondition(userTagDto, userTag, new UpdateWrapper<>());
                break;
            default:
        }
        userTag.setUserNum(0L);
        userTag.setCreateTime(LocalDateTime.now());
        return save(userTag);
    }

    @Override
    public boolean updateUserTag(UserTagDto userTagDto) {
        UserTag userTag = getById(userTagDto.getUserTagId());
        if (Objects.isNull(userTag)) {
            throw new YamiShopBindException("yami.user.tag.not.exists");
        }
        userTag.setTagName(userTagDto.getTagName());
        userTag.setUpdateTime(LocalDateTime.now());
        UpdateWrapper<UserTag> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(UserTag::getUserTagId, userTagDto.getUserTagId());
        switch (userTag.getTagType()) {
            // 手动标签
            case 0:
                //todo
                break;
            // 条件标签
            case 1:
                fillInCustomOptionalCondition(userTagDto, userTag, updateWrapper);
                break;
            default:
        }
        return update(userTag, updateWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeUserTag(Long userTagId) {
        QueryWrapper<UserTagUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserTagUser::getUserTagId, userTagId);
        List<UserTagUser> userTagUsers = userTagUserService.list(queryWrapper);
        if (Objects.nonNull(userTagUsers) && !userTagUsers.isEmpty()) {
            userTagUserService.remove(queryWrapper);
        }
        notifyTemplateTagMapper.delete(Wrappers.lambdaQuery(NotifyTemplateTag.class).eq(NotifyTemplateTag::getUserTagId, userTagId));
        return removeById(userTagId);
    }

    @Override
    public void fillInCustomOptionalCondition(UserTagDto userTagDto, UserTag userTag, UpdateWrapper<UserTag> updateWrapper) {
        // 基础条件
        baseOptionalCondition(userTagDto, userTag, updateWrapper);
        // 交易条件
        transactionOptionalCondition(userTagDto, userTag, updateWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserTag refreshConditionTag(Long userTagId) {
        // 检查标签类型
        UserTag userTag = getById(userTagId);
        if (Objects.isNull(userTag)) {
            throw new YamiShopBindException("yami.user.tag.not.exists");
        }
        if (userTag.getTagType() != 1) {
            throw new YamiShopBindException("yami.user.tag.type.not.right");
        }
        if (userTag.getStatisticUpdateTime() != null && LocalDateTime.now().compareTo(userTag.getStatisticUpdateTime().plusMinutes(1)) < 0) {
            throw new YamiShopBindException("yami.user.tag.refresh.limit");
        }
        // 筛选条件
        List<List<User>> listUserList = new ArrayList<>();
        // 基础条件
        // 成为客户时间
        if (Objects.nonNull(userTag.getRegisterMinTime()) && Objects.nonNull(userTag.getRegisterMaxTime())) {
            Date startDate = Date.from(userTag.getRegisterMinTime().atZone(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(userTag.getRegisterMaxTime().atZone(ZoneId.systemDefault()).toInstant());
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.lambda()
                    .select(User::getUserId)
                    .eq(User::getStatus, 1)
                    .between(User::getUserRegtime, startDate, endDate);
            List<User> users = userService.list(userQueryWrapper);
            listUserList.add(users);
        }
        // 关注时间
        if (Objects.nonNull(userTag.getSubscribeMinTime()) && Objects.nonNull(userTag.getSubscribeMaxTime())) {

        }
        // 成为会员时间
        if (Objects.nonNull(userTag.getToBeMemberMinTime()) && Objects.nonNull(userTag.getToBeMemberMaxTime())) {
            Date startDate = Date.from(userTag.getToBeMemberMinTime().atZone(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(userTag.getToBeMemberMaxTime().atZone(ZoneId.systemDefault()).toInstant());
            List<String> userIds = userLevelLogMapper.listUserIdByEarliestRechargeTime(1, startDate, endDate);
            List<User> users = new ArrayList<>();
            for (String string : userIds) {
                User user = new User();
                user.setUserId(string);
                users.add(user);
            }
            listUserList.add(users);
        }
        // 交易条件
        buyTransactionConditionTag(userTag, listUserList);
        amountTransactionConditionTag(userTag, listUserList);

        // 抽取用户子集
        Set<User> users = new TreeSet<>(Comparator.comparing(User::getUserId));
        if (!listUserList.isEmpty()) {
            for (int i = 0; i < listUserList.size(); i++) {
                users.addAll(listUserList.get(i));
            }
        }
        // 清空关联表
        QueryWrapper<UserTagUser> userTagUserQueryWrapper = new QueryWrapper<>();
        userTagUserQueryWrapper.lambda().eq(UserTagUser::getUserTagId, userTagId);
        userTagUserService.remove(userTagUserQueryWrapper);
        // 写入关联表
        List<UserTagUser> userTagUsers = new ArrayList<>();
        for (User user : users) {
            UserTagUser userTagUser = new UserTagUser();
            userTagUser.setUserId(user.getUserId());
            userTagUser.setUserTagId(userTagId);
            userTagUsers.add(userTagUser);
        }
        userTagUserService.saveBatch(userTagUsers);
        // 更新统计人数，时间
        userTag.setUserNum((long) userTagUsers.size());
        userTag.setStatisticUpdateTime(LocalDateTime.now());
        updateById(userTag);
        return getById(userTagId);
    }

    @Override
    public Date addDateTimeCondition(Integer timeType, LocalDateTime localDateTime) {
        Date startDate = null;
        switch (timeType) {
            case 1:
                startDate = Date.from(localDateTime.minusDays(RecentDaysType.TODAY.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 2:
                startDate = Date.from(localDateTime.minusDays(RecentDaysType.RECENT_SEVEN_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 3:
                startDate = Date.from(localDateTime.minusDays(RecentDaysType.RECENT_FIFTEEN_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 4:
                startDate = Date.from(localDateTime.minusDays(RecentDaysType.RECENT_THIRTY_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 5:
                startDate = Date.from(localDateTime.minusDays(RecentDaysType.RECENT_FORTY_FIVE_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 6:
                startDate = Date.from(localDateTime.minusDays(RecentDaysType.RECENT_SIXTY_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 7:
                startDate = Date.from(localDateTime.minusDays(RecentDaysType.RECENT_NINETY_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case 8:
                startDate = Date.from(localDateTime.minusDays(RecentDaysType.RECENT_ONE_HUNDRED_AND_EIGHTY_DAYS.value()).atZone(ZoneId.systemDefault()).toInstant());
                break;
            default:
        }
        return startDate;
    }

    @Override
    public List<User> userFilter(List<User> users1, List<User> users2) {
        return users1.stream().filter(users2::contains).collect(toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendTagMsg(Long templateId) {
        NotifyTemplate notifyTemplate = notifyTemplateService.getById(templateId);
        // 推送消息
        List<User> users = notifyTemplateTagMapper.getUserListByTemplateId(templateId);
        if(CollectionUtils.isEmpty(users)){
            return;
        }
        List<NotifyLog> notifyLogs = new ArrayList<>();
        for (User user : users) {
            NotifyLog notifyLog = new NotifyLog();
            notifyLog.setCreateTime(new Date());
            notifyLog.setShopId(Constant.PLATFORM_SHOP_ID);
            // 如果大于则为店铺自行接收的消息
            notifyLog.setRemindId(user.getUserId());
            notifyLog.setSendType(SendType.CUSTOMIZE.getValue());
            notifyLog.setMessage(notifyTemplate.getMessage());
            notifyLog.setNickName(user.getNickName());
            notifyLog.setUserMobile(user.getUserMobile());
            notifyLog.setRemindType(RemindType.MINI.value());
            notifyLog.setTemplateId(templateId);
            notifyLog.setStatus(0);
            notifyLogs.add(notifyLog);
        }
        //批量保存消息
        if(CollectionUtils.isNotEmpty(notifyLogs)){
            notifyLogService.saveBatch(notifyLogs);
        }
    }

    @Override
    public Boolean batchUpdateUserTag(UserUpdateParam param) {
        List<String> userIds = param.getUserIds();
        List<Long> tagList = param.getTagList();
        HashSet<Long> longs = new HashSet<>(tagList);
        tagList.clear();
        tagList.addAll(longs);
        if (CollectionUtils.isEmpty(tagList) || CollectionUtils.isEmpty(userIds)) {
            return false;
        }
        List<UserTagUser> tagUsers = new ArrayList<>();
        userIds.forEach(userId -> {
            tagList.forEach(tagId-> {
                // 给用户打标签 tagList
                Integer count = userTagUserService.countByUserIdAndTagId(userId,tagId);
                if (count > 0) {
                    // 已经存在的就不用给用户打标签了
                    return;
                }
                UserTagUser userTagUser = new UserTagUser();
                userTagUser.setUserTagId(tagId);
                userTagUser.setUserId(userId);
                tagUsers.add(userTagUser);
            });
        });
        if (CollectionUtils.isEmpty(tagUsers)) {
            return false;
        }
        // 给用户打上标签
        boolean b = userTagUserService.saveBatch(tagUsers);
        // 统计某个标签有多少个人，然后更新标签人数
        Map<Long, Integer> tagUser = new HashMap<>(16);
        Map<Long, List<UserTagUser>> collect = tagUsers.stream().collect(Collectors.groupingBy(UserTagUser::getUserTagId));
        collect.forEach((tagId,lists)->{
            int size = lists.size();
            tagUser.put(tagId,size);
        });
        userTagMapper.updateBatch(tagUser);
        return b;
    }

    @Override
    public IPage<UserTag> pageUserTagByParam(Page page, Integer tagType, String tagName) {
        return userTagMapper.pageUserTagByParam(page,tagType,tagName);
    }

    /**
     * 基础条件
     * @param userTagDto  标签信息
     * @param userTag 客户标签
     * @param updateWrapper 更新实例
     */
    private void baseOptionalCondition(UserTagDto userTagDto, UserTag userTag, UpdateWrapper<UserTag> updateWrapper) {
        // 成为客户时间
        if (Objects.nonNull(userTagDto.getRegisterMinTime()) && Objects.nonNull(userTagDto.getRegisterMaxTime())) {
            if (userTagDto.getRegisterMinTime().isAfter(userTagDto.getRegisterMaxTime())) {
                throw new YamiShopBindException("yami.user.tag.time");
            }
            userTag.setRegisterMinTime(userTagDto.getRegisterMinTime());
            userTag.setRegisterMaxTime(userTagDto.getRegisterMaxTime());
        }
        if (Objects.nonNull(userTagDto.getClearRegisterTime()) && userTagDto.getClearRegisterTime()) {
            updateWrapper.lambda().set(UserTag::getRegisterMinTime, null);
            updateWrapper.lambda().set(UserTag::getRegisterMaxTime, null);
        }
        // 关注时间
        if (Objects.nonNull(userTagDto.getSubscribeMinTime()) && Objects.nonNull(userTagDto.getSubscribeMaxTime())) {
            if (userTagDto.getSubscribeMinTime().isAfter(userTagDto.getSubscribeMaxTime())) {
                throw new YamiShopBindException("yami.user.tag.time");
            }
            userTag.setSubscribeMinTime(userTagDto.getSubscribeMinTime());
            userTag.setSubscribeMaxTime(userTagDto.getSubscribeMaxTime());
        }
        if (Objects.nonNull(userTagDto.getClearSubscribeTime()) && userTagDto.getClearSubscribeTime()) {
            updateWrapper.lambda().set(UserTag::getSubscribeMinTime, null);
            updateWrapper.lambda().set(UserTag::getSubscribeMaxTime, null);
        }
        // 成为会员时间
        if (Objects.nonNull(userTagDto.getToBeMemberMinTime()) && Objects.nonNull(userTagDto.getToBeMemberMaxTime())) {
            if (userTagDto.getToBeMemberMinTime().isAfter(userTagDto.getToBeMemberMaxTime())) {
                throw new YamiShopBindException("yami.user.tag.time");
            }
            userTag.setToBeMemberMinTime(userTagDto.getToBeMemberMinTime());
            userTag.setToBeMemberMaxTime(userTagDto.getToBeMemberMaxTime());
        }
        if (Objects.nonNull(userTagDto.getClearToBeMemberTime()) && userTagDto.getClearToBeMemberTime()) {
            updateWrapper.lambda().set(UserTag::getToBeMemberMinTime, null);
            updateWrapper.lambda().set(UserTag::getToBeMemberMaxTime, null);
        }
    }

    /**
     * 交易条件
     * @param userTagDto  标签信息
     * @param userTag 客户标签
     * @param updateWrapper 更新实例
     */
    private void transactionOptionalCondition(UserTagDto userTagDto, UserTag userTag, UpdateWrapper<UserTag> updateWrapper) {
        // 最近消费时间
        if (Objects.nonNull(userTagDto.getRecentPurchaseTime())) {
            userTag.setRecentPurchaseTime(userTagDto.getRecentPurchaseTime());
        }
        if (Objects.nonNull(userTagDto.getClearRecentPurchaseTime()) && userTagDto.getClearRecentPurchaseTime()) {
            updateWrapper.lambda().set(UserTag::getRecentPurchaseTime, null);
        }
        // 消费次数
        if (Objects.nonNull(userTagDto.getPurchaseNumTime()) && Objects.nonNull(userTagDto.getPurchaseNumMinNum()) && Objects.nonNull(userTagDto.getPurchaseNumMaxNum())) {
            if (userTagDto.getPurchaseNumMinNum() > userTagDto.getPurchaseNumMaxNum()) {
                throw new YamiShopBindException("yami.user.tag.number");
            }
            userTag.setPurchaseNumTime(userTagDto.getPurchaseNumTime());
            userTag.setPurchaseNumMinNum(userTagDto.getPurchaseNumMinNum());
            userTag.setPurchaseNumMaxNum(userTagDto.getPurchaseNumMaxNum());
        }
        if (Objects.nonNull(userTagDto.getClearPurchaseNum()) && userTagDto.getClearPurchaseNum()) {
            updateWrapper.lambda().set(UserTag::getPurchaseNumTime, null);
            updateWrapper.lambda().set(UserTag::getPurchaseNumMinNum, null);
            updateWrapper.lambda().set(UserTag::getPurchaseNumMaxNum, null);
        }
        // 消费金额
        if (Objects.nonNull(userTagDto.getPurchaseAmountTime()) && Objects.nonNull(userTagDto.getPurchaseAmountMinAmount()) && Objects.nonNull(userTagDto.getPurchaseAmountMaxAmount())) {
            if (userTagDto.getPurchaseAmountMinAmount().compareTo(userTagDto.getPurchaseAmountMaxAmount()) == 1) {
                throw new YamiShopBindException("yami.user.tag.amount");
            }
            userTag.setPurchaseAmountTime(userTagDto.getPurchaseAmountTime());
            userTag.setPurchaseAmountMinAmount(userTagDto.getPurchaseAmountMinAmount());
            userTag.setPurchaseAmountMaxAmount(userTagDto.getPurchaseAmountMaxAmount());
        }
        if (Objects.nonNull(userTagDto.getClearPurchaseAmount()) && userTagDto.getClearPurchaseAmount()) {
            updateWrapper.lambda().set(UserTag::getPurchaseAmountTime, null);
            updateWrapper.lambda().set(UserTag::getPurchaseAmountMinAmount, null);
            updateWrapper.lambda().set(UserTag::getPurchaseAmountMaxAmount, null);
        }
        // 订单均价
        if (Objects.nonNull(userTagDto.getOrderAveragePriceTime()) && Objects.nonNull(userTagDto.getOrderAveragePriceMinAmount()) && Objects.nonNull(userTagDto.getOrderAveragePriceMaxAmount())) {
            if (userTagDto.getOrderAveragePriceMinAmount().compareTo(userTagDto.getOrderAveragePriceMaxAmount()) == 1) {
                throw new YamiShopBindException("yami.user.tag.amount");
            }
            userTag.setOrderAveragePriceTime(userTagDto.getOrderAveragePriceTime());
            userTag.setOrderAveragePriceMinAmount(userTagDto.getOrderAveragePriceMinAmount());
            userTag.setOrderAveragePriceMaxAmount(userTagDto.getOrderAveragePriceMaxAmount());
        }
        if (Objects.nonNull(userTagDto.getClearOrderAveragePrice()) && userTagDto.getClearOrderAveragePrice()) {
            updateWrapper.lambda().set(UserTag::getOrderAveragePriceTime, null);
            updateWrapper.lambda().set(UserTag::getOrderAveragePriceMinAmount, null);
            updateWrapper.lambda().set(UserTag::getOrderAveragePriceMaxAmount, null);
        }
        // 充值金额
        if (Objects.nonNull(userTagDto.getRechargeAmountTime()) && Objects.nonNull(userTagDto.getRechargeAmountMinAmount()) && Objects.nonNull(userTagDto.getRechargeAmountMaxAmount())) {
            if (userTagDto.getRechargeAmountMinAmount().compareTo(userTagDto.getRechargeAmountMaxAmount()) == 1) {
                throw new YamiShopBindException("yami.user.tag.amount");
            }
            userTag.setRechargeAmountTime(userTagDto.getRechargeAmountTime());
            userTag.setRechargeAmountMinAmount(userTagDto.getRechargeAmountMinAmount());
            userTag.setRechargeAmountMaxAmount(userTagDto.getRechargeAmountMaxAmount());
        }
        if (Objects.nonNull(userTagDto.getClearRechargeAmount()) && userTagDto.getClearRechargeAmount()) {
            updateWrapper.lambda().set(UserTag::getRechargeAmountTime, null);
            updateWrapper.lambda().set(UserTag::getRechargeAmountMinAmount, null);
            updateWrapper.lambda().set(UserTag::getRechargeAmountMaxAmount, null);
        }
        // 充值次数
        if (Objects.nonNull(userTagDto.getRechargeNumTime()) && Objects.nonNull(userTagDto.getRechargeNumMinNum()) && Objects.nonNull(userTagDto.getRechargeNumMaxNum())) {
            if (userTagDto.getRechargeNumMinNum() > userTagDto.getRechargeNumMaxNum()) {
                throw new YamiShopBindException("yami.user.tag.number");
            }
            userTag.setRechargeNumTime(userTagDto.getRechargeNumTime());
            userTag.setRechargeNumMinNum(userTagDto.getRechargeNumMinNum());
            userTag.setRechargeNumMaxNum(userTagDto.getRechargeNumMaxNum());
        }
        if (Objects.nonNull(userTagDto.getClearRechargeNum()) && userTagDto.getClearRechargeNum()) {
            updateWrapper.lambda().set(UserTag::getRechargeNumTime, null);
            updateWrapper.lambda().set(UserTag::getRechargeNumMinNum, null);
            updateWrapper.lambda().set(UserTag::getRechargeNumMaxNum, null);
        }
    }

    /**
     * 刷新条件标签统计人数 -交易条件（金额相关）
     * @param userTag 客户标签
     * @param listUserList 筛选条件
     */
    private void amountTransactionConditionTag(UserTag userTag, List<List<User>> listUserList) {
        // 订单均价
        Date endTime = new Date();
        if (Objects.nonNull(userTag.getOrderAveragePriceTime()) && Objects.nonNull(userTag.getOrderAveragePriceMinAmount()) && Objects.nonNull(userTag.getOrderAveragePriceMaxAmount())) {
            List<String> userIds;
            if (userTag.getOrderAveragePriceTime() != 0) {
                BigDecimal bigDecimal = new BigDecimal("0.00");
                //订单均价为0.00~0.00
                if (userTag.getOrderAveragePriceMinAmount().equals(bigDecimal) && userTag.getOrderAveragePriceMaxAmount().equals(bigDecimal)){
                    LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    Date startDate = addDateTimeCondition(userTag.getOrderAveragePriceTime(), localDateTime);
                    userIds = orderMapper.listUserIdByAverageActualTotals(1, 0, startDate, endTime, OrderStatus.SUCCESS.value(), bigDecimal);
                    List<String> userIdList = userMapper.getUserIdList();
                    userIdList.removeAll(userIds);
                    userIds = userIdList;
                }else {
                    LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    Date startDate = addDateTimeCondition(userTag.getOrderAveragePriceTime(), localDateTime);
                    userIds = orderMapper.listUserIdByAverageActualTotal(1, 0, startDate, endTime, OrderStatus.SUCCESS.value(), userTag.getOrderAveragePriceMinAmount(), userTag.getOrderAveragePriceMaxAmount());
                }
            } else {
                userIds = orderMapper.listUserIdByAverageActualTotal(1, 0, null, null, OrderStatus.SUCCESS.value(), userTag.getOrderAveragePriceMinAmount(), userTag.getOrderAveragePriceMaxAmount());
            }
            List<User> users = new ArrayList<>();
            for (String string : userIds) {
                User user = new User();
                user.setUserId(string);
                users.add(user);
            }
            listUserList.add(users);
        }
        // 充值金额
        if (Objects.nonNull(userTag.getRechargeAmountTime()) && Objects.nonNull(userTag.getRechargeAmountMinAmount()) && Objects.nonNull(userTag.getRechargeAmountMaxAmount())) {
            List<String> userIds;
            if (userTag.getRechargeAmountTime() != 0) {
                LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                Date startDate = addDateTimeCondition(userTag.getRechargeAmountTime(), localDateTime);
                userIds = userBalanceLogMapper.listUserIdByRechargeAmount(1, startDate, date, userTag.getRechargeAmountMinAmount(), userTag.getRechargeAmountMaxAmount());
            } else {
                userIds = userBalanceLogMapper.listUserIdByRechargeAmount(1, null, null, userTag.getRechargeAmountMinAmount(), userTag.getRechargeAmountMaxAmount());
            }
            List<User> users = new ArrayList<>();
            for(String string : userIds) {
                User user = new User();
                user.setUserId(string);
                users.add(user);
            }
            listUserList.add(users);
        }
        // 充值次数
        if (Objects.nonNull(userTag.getRechargeNumTime()) && Objects.nonNull(userTag.getRechargeNumMinNum()) && Objects.nonNull(userTag.getRechargeNumMaxNum())) {
            List<String> userIds;
            if (userTag.getRechargeNumTime() != 0) {
                if (userTag.getRechargeNumMinNum() == 0 && userTag.getRechargeNumMaxNum() == 0){
                    //充值次数为0
                    List<String> userIdList = userMapper.getUserIdList();
                    LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    Date startDate = addDateTimeCondition(userTag.getRechargeNumTime(), localDateTime);
                    userIds = userBalanceLogMapper.listUserIdByRechargeNums(1, startDate, date, 0L);
                    userIdList.removeAll(userIds);
                    userIds = userIdList;
                }else {
                    LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    Date startDate = addDateTimeCondition(userTag.getRechargeNumTime(), localDateTime);
                    userIds = userBalanceLogMapper.listUserIdByRechargeNum(1, startDate, date, userTag.getRechargeNumMinNum(), userTag.getRechargeNumMaxNum());
                }
            } else {
                userIds = userBalanceLogMapper.listUserIdByRechargeNum(1, null, null, userTag.getRechargeNumMinNum(), userTag.getRechargeNumMaxNum());
            }
            List<User> users = new ArrayList<>();
            for (String string : userIds) {
                User user = new User();
                user.setUserId(string);
                users.add(user);
            }
            listUserList.add(users);
        }
    }

    /**
     * 刷新条件标签统计人数 -交易条件（购买相关）
     * @param userTag 客户标签
     * @param listUserList 筛选条件
     */
    private void buyTransactionConditionTag(UserTag userTag, List<List<User>> listUserList) {
        // 最近消费时间
        if (Objects.nonNull(userTag.getRecentPurchaseTime())) {
            QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.lambda()
                    .select(Order::getUserId)
                    .eq(Order::getIsPayed, 1)
                    .eq(Order::getDeleteStatus, 0)
                    .eq(Order::getStatus, OrderStatus.SUCCESS.value());
            // 当0时不限时间
            if (userTag.getRecentPurchaseTime() != 0) {
                LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                Date startDate = addDateTimeCondition(userTag.getRecentPurchaseTime(), localDateTime);
                DateTime endTime = DateUtil.endOfDay(new Date());
                orderQueryWrapper.lambda().between(Order::getFinallyTime, startDate, endTime);
            }
            List<Order> orders = orderService.list(orderQueryWrapper);
            List<User> users = new ArrayList<>();
            for (Order order : orders) {
                User user = new User();
                user.setUserId(order.getUserId());
                users.add(user);
            }
            listUserList.add(users);
        }
        // 消费次数
        if (Objects.nonNull(userTag.getPurchaseNumTime()) && Objects.nonNull(userTag.getPurchaseNumMinNum()) && Objects.nonNull(userTag.getPurchaseNumMaxNum())) {
            Date endTime = new Date();
            List<String> userIds;
            if (userTag.getPurchaseNumTime() != 0) {
                if (userTag.getPurchaseNumMinNum() == 0 && userTag.getPurchaseNumMaxNum() == 0){
                    //查询的消费次数是0~0
                    //先查询出消费过1次及以上的用户
                    LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    Date startDate = addDateTimeCondition(userTag.getPurchaseNumTime(), localDateTime);
                    List<String> orderUserId = orderMapper.listUserIdByPurchaseNum(1, 0, startDate, endTime, OrderStatus.SUCCESS.value(), 1L, 99999L);
                    //查询所有会员
                    List<String> userIdList = userMapper.getUserIdList();
                    //移除掉消费一次及以上的会员,得到消费0次的会员id
                    userIdList.removeAll(orderUserId);
                    userIds = userIdList;
                }else {
                    LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    Date startDate = addDateTimeCondition(userTag.getPurchaseNumTime(), localDateTime);
                    userIds = orderMapper.listUserIdByPurchaseNum(1, 0, startDate, endTime, OrderStatus.SUCCESS.value(), userTag.getPurchaseNumMinNum(), userTag.getPurchaseNumMaxNum());
                }
            } else {
                userIds = orderMapper.listUserIdByPurchaseNum(1, 0, null, null, OrderStatus.SUCCESS.value(), userTag.getPurchaseNumMinNum(), userTag.getPurchaseNumMaxNum());
            }
            List<User> users = new ArrayList<>();
            for (String string : userIds) {
                User user = new User();
                user.setUserId(string);
                users.add(user);
            }
            listUserList.add(users);
        }
        // 消费金额
        if (Objects.nonNull(userTag.getPurchaseAmountTime()) && Objects.nonNull(userTag.getPurchaseAmountMinAmount()) && Objects.nonNull(userTag.getPurchaseAmountMaxAmount())) {
            QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
            List<Order> orders;
            List<User> users = new ArrayList<>();
            // 当0时不限时间
            if (userTag.getPurchaseAmountTime() != 0) {
                //查询消费金额范围0.00~0.00
                BigDecimal big = new BigDecimal("0.00");
                if (userTag.getPurchaseAmountMinAmount().equals(big) && userTag.getPurchaseAmountMaxAmount().equals(big)){
                    //查询出消费0.00以上的会员
                    orderQueryWrapper.lambda()
                            .select(Order::getUserId)
                            .eq(Order::getIsPayed, 1)
                            .eq(Order::getDeleteStatus, 0)
                            .eq(Order::getStatus, OrderStatus.SUCCESS.value())
                            .gt(Order::getActualTotal, 0.00);
                    LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    Date startDate = addDateTimeCondition(userTag.getPurchaseAmountTime(), localDateTime);
                    DateTime endTime = DateUtil.endOfDay(new Date());
                    orderQueryWrapper.lambda().between(Order::getFinallyTime, startDate, endTime);
                    orders = orderService.list(orderQueryWrapper);
                    ArrayList<Object> orderUserId = new ArrayList<>();
                    for (Order order : orders) {
                        User user = new User();
                        user.setUserId(order.getUserId());
                        users.add(user);
                        orderUserId.add(order.getUserId());
                    }
                    //查询所有会员
                    List<String> userIdList = userMapper.getUserIdList();
                    //移除掉消费0.00及以上的会员,得到消费0.00的会员id
                    userIdList.removeAll(orderUserId);
                    ArrayList<User> list = new ArrayList<>();
                    for (String string : userIdList) {
                        User user = new User();
                        user.setUserId(string);
                        list.add(user);
                    }
                    listUserList.add(list);
                }else {
                    LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    Date startDate = addDateTimeCondition(userTag.getPurchaseAmountTime(), localDateTime);
                    DateTime endTime = DateUtil.endOfDay(new Date());
                    orderQueryWrapper.lambda().between(Order::getFinallyTime, startDate, endTime);
                    orders = orderService.list(orderQueryWrapper);
                    users = new ArrayList<>();
                    for (Order order : orders) {
                        User user = new User();
                        user.setUserId(order.getUserId());
                        users.add(user);
                    }
                    listUserList.add(users);
                }
            }else {
                orderQueryWrapper.lambda()
                        .select(Order::getUserId)
                        .eq(Order::getIsPayed, 1)
                        .eq(Order::getDeleteStatus, 0)
                        .eq(Order::getStatus, OrderStatus.SUCCESS.value())
                        .between(Order::getActualTotal, userTag.getPurchaseAmountMinAmount(), userTag.getPurchaseAmountMaxAmount());
                orders = orderService.list(orderQueryWrapper);
                users = new ArrayList<>();
                for (Order order : orders) {
                    User user = new User();
                    user.setUserId(order.getUserId());
                    users.add(user);
                }
                listUserList.add(users);
            }
        }
    }

}
