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
import java.util.Date;

/**
 *
 *
 * @author lhd
 * @date 2020-05-25 15:31:02
 */
@Data
@TableName("tz_user_score_detail")
public class UserScoreDetail implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 积分明细表
     */
    @TableId
    private Long userScoreDetailId;
    /**
     * 可用积分
     */
    private Long usableScore;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 业务id
     */
    private String bizId;
    /**
     * 获取时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 过期时间
     */
    private Date expireTime;
    /**
     * 状态  -1过期 0订单抵现 1正常
     */
    private Integer status;

}
