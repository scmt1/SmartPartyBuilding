/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Yami
 */
@Data
public class UserUpdateParam {

    @ApiModelProperty("正数代表增加，负数代表减少，只能输入整数，根据修改后的成长值重新计算会员等级")
    private Integer growthValue;

    @ApiModelProperty("修改成长值，是否赠送礼物 0不赠送，1赠送")
    private Integer isSendReward;

    @ApiModelProperty("正数代表增加，负数代表减少，只能输入整数，修改会员积分")
    private Long scoreValue;

    @ApiModelProperty("用户id列表")
    private List<String> userIds;

    @ApiModelProperty("关联业务id")
    private String bizId;

    @ApiModelProperty("成长值来源")
    private Integer growthSource;

    @ApiModelProperty("积分来源")
    private Integer scopeSource;

    @ApiModelProperty("标签id列表")
    private List<Long> tagList;

    @ApiModelProperty("修改余额 正数代表增加，负数代表减少，只能输入数字，最多两位小数")
    private Double balanceValue;

    @ApiModelProperty("发放优惠券列表")
    private List<SendUserCouponsParam> coupons;
}
