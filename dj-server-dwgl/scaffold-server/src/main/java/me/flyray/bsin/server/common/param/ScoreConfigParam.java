/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.common.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Yami
 */
@Data
public class ScoreConfigParam {
    /**
     * id
     */
    private Long id;

    @ApiModelProperty("参数名")
    private String paramKey;

    @ApiModelProperty("参数值")
    private String paramValue;

    @ApiModelProperty("签到获取积分")
    private String signInScoreString;

    @ApiModelProperty("签到获取积分")
    private List<Integer> signInScore;

    @ApiModelProperty("注册获取积分")
    private Long registerScore;

    @ApiModelProperty("购物开关")
    private Boolean shopScoreSwitch;

    @ApiModelProperty("购物获取积分")
    private Double shopGetScore;

    @ApiModelProperty("购物积分抵现比例")
    private Double shopUseScore;

    @ApiModelProperty("购物积分分类获取范围0为全部商品1为根据分类使用")
    private Integer getDiscountRange;

    @ApiModelProperty("购物积分分类获取上限比例")
    private Double getDiscount;



    @ApiModelProperty("购物积分分类使用范围0为全部商品1为根据分类使用")
    private Integer useDiscountRange;

    @ApiModelProperty("平台使用积分分类上限比例")
    private Double useDiscount;
//
//    /**
//     * 购物时使用积分上限
//     */
//    private Double shopUserLimit;


}
