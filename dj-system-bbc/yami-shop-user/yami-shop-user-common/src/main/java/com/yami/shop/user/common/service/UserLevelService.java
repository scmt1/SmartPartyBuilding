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
import com.yami.shop.bean.bo.PayInfoResultBO;
import com.yami.shop.bean.model.PayInfo;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.param.UserUpdateParam;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.user.common.dto.LevelDetailDto;
import com.yami.shop.user.common.dto.UserLevelDto;
import com.yami.shop.user.common.model.UserLevel;
import com.yami.shop.user.common.model.UserLevelLog;

import java.util.List;

/**
 * 会员等级
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
public interface UserLevelService extends IService<UserLevel> {

    /**
     * 根据会员等级类型获取会员等级列表信息
     *
     * @param userLevelType 见 枚举UserLevelType
     * @return
     */
    List<UserLevelDto> listUserLevelsByUserLevelType(Integer userLevelType);

    /**
     * 根据会员等级类型移除会员等级列表缓存
     *
     * @param userLevelType 等级类型
     */
    void removeUserLevelsByUserLevelTypeCache(Integer userLevelType);

    /**
     * 购买会员回调
     * @param payInfoResultBO
     * @param payInfo
     * @return
     */
    ServerResponseEntity<String> noticeBuyVip(PayInfoResultBO payInfoResultBO, PayInfo payInfo);

    /**
     * 购买付费会员成功处理
     *
     * @param payNo 支付单号
     * @param userLevelLog
     */
    void paySuccess(String payNo, UserLevelLog userLevelLog, PayInfoResultBO payInfoResultBO);

    /**
     * 增加用户成长值和积分
     *
     * @param growthPrice 增加的成长值
     * @param score    增加的积分
     * @param userId    用户id
     * @param bizId    bizId
     * @param userExtension    用户扩展信息
     * @param type    1:等级 2：余额充值
     */
    void addGrowthAndScore(double growthPrice, Long score, String userId, String bizId, UserExtension userExtension, Integer type);

    /**
     * 等级提升
     * @param userLevels 多个等级，奖励发放时
     * @param userLevelLog 等级日志,在购买付费会员时存在
     * @param user 用户详细信息
     * @param historyLevel 等级提升后的等级
     * @param beforeLevel 等级提升前的等级
     */
    void levelUp(List<UserLevel> userLevels, UserLevelLog userLevelLog, UserExtension user, Integer historyLevel, Integer beforeLevel);

    /**
     * 更新等级模板信息
     * @param userLevel 等级信息
     */
    void updateUserLevelList(UserLevel userLevel);

    /**
     * 删除等级
     * @param userLevel 等级信息
     * @return 是否删除
     */
    Boolean deleteUserLevel(UserLevel userLevel);

    /**
     * 根据等级模板，更新用户的等级信息
     * @param userLevel 等级信息
     * @return 是否成功更新
     */
    Boolean updateUserLevel(UserLevel userLevel);

    /**
     * 获取等级信息
     * @param id 等级id
     * @return 等级信息
     */
    UserLevel getUserLevelById(Long id);

    /**
     * 获取用户所在等级的名称
     * @param userExtension 用户扩展信息
     * @return 等级名称
     */
    String getUserLevelName(UserExtension userExtension);

    /**
     * 获取等级列表，等级包含等级关联的权限列表信息
     * @param levelType 等级类型
     * @return 等级列表
     */
    List<LevelDetailDto> selectLevelAndRights(Integer levelType);

    /**
     * 批量修改用户成长值
     * @param param
     * @return
     */
    Boolean batchUpdateGrowth(UserUpdateParam param);

    /**
     * 批量修改用户积分
     * @param param
     * @return
     */
    Boolean batchUpdateScore(UserUpdateParam param);

}
