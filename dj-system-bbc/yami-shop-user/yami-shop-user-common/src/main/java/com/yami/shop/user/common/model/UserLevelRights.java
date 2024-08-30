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
 * 会员等级
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Data
@TableName("tz_user_level_rights")
public class UserLevelRights implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 等级-权益关联id
     */
    @TableId
    private Long levelRightsId;
    /**
     * 等级id
     */
    private Long levelId;
    /**
     * 权益id
     */
    private Long rightsId;

}