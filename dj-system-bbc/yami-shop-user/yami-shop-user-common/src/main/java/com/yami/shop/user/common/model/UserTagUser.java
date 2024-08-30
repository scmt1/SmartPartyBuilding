/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户和标签关联表
 *
 * @author LGH
 * @date 2020-09-10 08:44:22
 */
@Data
@TableName("tz_user_tag_user")
public class UserTagUser implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId
    private Long userTagUserId;
    /**
     * 标签id
     */
    private Long userTagId;
    /**
     * 用户id
     */
    private String userId;

}
