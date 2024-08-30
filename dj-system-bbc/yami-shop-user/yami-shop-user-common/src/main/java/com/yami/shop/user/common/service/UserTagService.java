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

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.param.UserUpdateParam;
import com.yami.shop.user.common.dto.UserTagDto;
import com.yami.shop.user.common.model.UserTag;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 客户标签
 *
 * @author LGH
 * @date 2020-09-09 11:31:16
 */
public interface UserTagService extends IService<UserTag> {

    /**
     * 添加用户标签信息
     * @param userTagDto 标签信息
     * @return 结果
     */
    boolean addUserTag(UserTagDto userTagDto);
    /**
     * 修改用户标签信息
     * @param userTagDto 标签信息
     * @return 结果
     */
    boolean updateUserTag(UserTagDto userTagDto);
    /**
     * 删除用户标签信息
     * @param userTagId 标签id
     * @return 结果
     */
    boolean removeUserTag(Long userTagId);

    /**
     * 填充自定义可选条件
     * @param userTagDto  标签信息
     * @param userTag 客户标签
     * @param updateWrapper 更新实例
     */
    void fillInCustomOptionalCondition(UserTagDto userTagDto, UserTag userTag, UpdateWrapper<UserTag> updateWrapper);

    /**
     * 刷新条件标签统计人数
     * @param userTagId 用户标签id
     * @return 条件标签统计人数
     */
    UserTag refreshConditionTag(Long userTagId);

    /**
     * 根据"最近几天"求出具体日期
     * @param timeType 时间类型条件
     * @param localDateTime 时间
     * @return 具体日期
     */
    Date addDateTimeCondition(Integer timeType, LocalDateTime localDateTime);

    /**
     * 求两个List<User>之间的交集
     * @param users1 用户列表1
     * @param users2 用户列表2
     * @return list的交集
     */
    List<User> userFilter(List<User> users1, List<User> users2);

    /**
     * 发送标签消息
     * @param templateId 模板id
     */
    void sendTagMsg(Long templateId);

    /**
     * 批量修改用户标签
     * @param param 用户标签
     * @return 结果
     */
    Boolean batchUpdateUserTag(UserUpdateParam param);

    /**
     * 根据参数查询标签列表
     * @param page 分页参数
     * @param tagType 标签类型
     * @param tagName 标签名称
     * @return 标签列表
     */
    IPage<UserTag> pageUserTagByParam(Page page,Integer tagType,String tagName);
}
