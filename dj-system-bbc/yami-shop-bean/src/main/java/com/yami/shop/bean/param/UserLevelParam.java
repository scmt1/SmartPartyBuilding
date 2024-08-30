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

import lombok.Data;

/**
 * 用户等级参数
 * @author: cl
 * @date: 2021-04-20 13:37:22
 */
@Data
public class UserLevelParam {
    private Long id;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 等级名称
     */
    private String levelName;
    /**
     * 等级条件 0 普通会员 1 付费会员
     */
    private Integer levelType;
    /**
     * 所需成长值
     */
    private Integer needGrowth;
    /**
     * 付费会员价格
     */
    private Double needAmount;
    /**
     * 有效期（天）
     */
    private Integer term;
    /**
     * 期数类型
     */
    private Integer termType;
    /**
     * 背景图片
     */
    private String img;
    /**
     * 折扣范围 0 全平台 1 自营店
     */
    private Integer discountRange;
    /**
     * 折扣方式 0 全部商品 1 分类下的商品
     */
    private Integer discountType;
    /**
     * 会员折扣
     */
    private Double discount;
    /**
     * 赠送积分
     */
    private Integer presScore;
    /**
     * 积分回馈倍率
     */
    private Double rateScore;
    /**
     * 是否包邮 0:不包邮 1:包邮
     */
    private Integer isFreeFee;
    /**
     * 1:已更新 0:停用 -1:未更新(等级修改后，用户等级的更新)
     */
    private Integer status;
}
