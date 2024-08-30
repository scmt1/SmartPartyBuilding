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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 积分锁定信息
 *
 * @author lhd
 * @date 2022-05-06 17:27:53
 */
@Data
@TableName("tz_user_score_lock")
@ApiModel("积分锁定信息")
public class UserScoreLock implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "订单号")
    private String orderNumber;
    @ApiModelProperty(value = "状态-1已解锁 0待确定 1已锁定")
    private Integer status;
    @ApiModelProperty(value = "锁定积分数量")
    private Long score;
    @ApiModelProperty(value = "变动的积分id集合")
    private String scoreGetLogIds;
}
