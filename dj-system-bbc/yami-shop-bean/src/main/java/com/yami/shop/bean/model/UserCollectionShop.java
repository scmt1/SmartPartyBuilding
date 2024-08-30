/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户店铺收藏记录
 *
 * @author LGH
 * @date 2019-09-25 15:38:30
 */
@Data
@TableName("tz_user_collection_shop")
public class UserCollectionShop implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 收藏自增id
     */
    @TableId
    private Long collectionId;
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 创建时间
     */
    private Date createTime;

}
