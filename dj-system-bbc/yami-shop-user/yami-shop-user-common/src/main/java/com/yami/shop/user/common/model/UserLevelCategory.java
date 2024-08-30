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
 *
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Data
@TableName("tz_user_level_category")
public class UserLevelCategory implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 用户等级分类关联id
     */
    @TableId
    private Long userLevelCategoryId;
    /**
     * 等级id
     */
    private Long levelId;
    /**
     * 分类id
     */
    private Long categoryId;

}
