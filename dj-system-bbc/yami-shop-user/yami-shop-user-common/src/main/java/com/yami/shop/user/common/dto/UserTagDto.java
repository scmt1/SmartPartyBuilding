/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yami
 */
@Data
public class UserTagDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增标签校验
     */
    public interface AddUserTag {
    }

    /**
     * 修改标签校验
     */
    public interface UpdateUserTag {
    }

    /**
     * 自增id
     */
    @ApiModelProperty(value = "自增id")
    @NotNull(message = "自增id不能为空", groups = UpdateUserTag.class)
    private Long userTagId;

    /**
     * 标签名字
     */
    @ApiModelProperty(value = "标签名字")
    @NotBlank(message = "标签名字不能为空", groups = AddUserTag.class)
    @NotBlank(message = "标签名字不能为空", groups = UpdateUserTag.class)
    private String tagName;

    /**
     * 标签类型0手动1条件
     */
    @ApiModelProperty(value = "标签类型0手动1条件")
    @Max(value = 1, message = "只能为0或1", groups = AddUserTag.class)
    @Min(value = 0, message = "只能为0或1", groups = AddUserTag.class)
    @NotNull(message = "标签类型不能为空。", groups = AddUserTag.class)
    private Integer tagType;

    /**
     * 系统标签是否开启
     */
    @ApiModelProperty(value = "系统标签是否开启")
    @Max(value = 1, message = "只能为0或1", groups = UpdateUserTag.class)
    @Min(value = 0, message = "只能为0或1", groups = UpdateUserTag.class)
    private Integer isSysTurnOn;

    /**
     * 成为客户开始时间
     */
    @ApiModelProperty(value = "成为客户开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerMinTime;

    /**
     * 成为客户结束时间
     */
    @ApiModelProperty(value = "成为客户结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerMaxTime;

    /**
     * 清空成为客户时间标记
     */
    @ApiModelProperty(value = "清空成为客户时间标记")
    private Boolean clearRegisterTime;

    /**
     * 关注开始时间
     */
    @ApiModelProperty(value = "关注开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime subscribeMinTime;

    /**
     * 关注结束时间
     */
    @ApiModelProperty(value = "关注结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime subscribeMaxTime;

    /**
     * 清空关注时间标记
     */
    @ApiModelProperty(value = "清空关注时间标记")
    private Boolean clearSubscribeTime;

    /**
     * 成为会员开始时间
     */
    @ApiModelProperty(value = "成为会员开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime toBeMemberMinTime;

    /**
     * 成为会员结束时间
     */
    @ApiModelProperty(value = "成为会员结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime toBeMemberMaxTime;

    /**
     * 清空成为会员时间标记
     */
    @ApiModelProperty(value = "清空成为会员时间标记")
    private Boolean clearToBeMemberTime;

    /**
     * 最近消费时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)
     */
    @ApiModelProperty(value = "最近消费时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
    @Max(value = 8, message = "最大值为8", groups = {AddUserTag.class, UpdateUserTag.class})
    @Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Integer recentPurchaseTime;

    /**
     * 清空最近消费时间标记
     */
    @ApiModelProperty(value = "清空成为会员时间标记")
    private Boolean clearRecentPurchaseTime;

    /**
     * 消费次数时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)
     */
    @ApiModelProperty(value = "消费次数时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
    @Max(value = 8, message = "最大值为8", groups = {AddUserTag.class, UpdateUserTag.class})
    @Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Integer purchaseNumTime;

    /**
     * 消费次数最小次数
     */
    @ApiModelProperty(value = "消费次数最小次数")
    @Max(value = 100000, message = "最大值为100000", groups = {AddUserTag.class, UpdateUserTag.class})
    @Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Long purchaseNumMinNum;

    /**
     * 消费次数最大次数
     */
    @ApiModelProperty(value = "消费次数最大次数")
    @Max(value = 100000, message = "最大值为100000", groups = {AddUserTag.class, UpdateUserTag.class})
    @Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Long purchaseNumMaxNum;

    /**
     * 清空消费次数标记
     */
    @ApiModelProperty(value = "清空消费次数标记")
    private Boolean clearPurchaseNum;

    /**
     * 消费金额时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)
     */
    @ApiModelProperty(value = "消费金额时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
    @Max(value = 8, message = "最大值为8", groups = {AddUserTag.class, UpdateUserTag.class})
    @Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Integer purchaseAmountTime;

    /**
     * 消费金额最小金额
     */
    @ApiModelProperty(value = "消费金额最小金额")
    @DecimalMax(value = "100000000", message = "最大值为100000000", groups = {AddUserTag.class, UpdateUserTag.class})
    @DecimalMin(value = "0", message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private BigDecimal purchaseAmountMinAmount;

    /**
     * 消费金额最大金额
     */
    @ApiModelProperty(value = "消费金额最大金额")
    @DecimalMax(value = "100000000", message = "最大值为100000000", groups = {AddUserTag.class, UpdateUserTag.class})
    @DecimalMin(value = "0", message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private BigDecimal purchaseAmountMaxAmount;

    /**
     * 清空消费金额标记
     */
    @ApiModelProperty(value = "清空消费次数标记")
    private Boolean clearPurchaseAmount;

    /**
     * 订单均价时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)
     */
    @ApiModelProperty(value = "订单均价时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
    @Max(value = 8, message = "最大值为8", groups = {AddUserTag.class, UpdateUserTag.class})
    @Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Integer orderAveragePriceTime;

    /**
     * 订单均价最小金额
     */
    @ApiModelProperty(value = "订单均价最小金额")
    @DecimalMax(value = "100000000", message = "最大值为100000000", groups = {AddUserTag.class, UpdateUserTag.class})
    @DecimalMin(value = "0", message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private BigDecimal orderAveragePriceMinAmount;

    /**
     * 订单均价最大金额
     */
    @ApiModelProperty(value = "订单均价最大金额")
    @DecimalMax(value = "100000000", message = "最大值为100000000", groups = {AddUserTag.class, UpdateUserTag.class})
    @DecimalMin(value = "0", message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private BigDecimal orderAveragePriceMaxAmount;

    /**
     * 清空订单均价标记
     */
    @ApiModelProperty(value = "清空订单均价标记")
    private Boolean clearOrderAveragePrice;

    /**
     * 充值金额时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)
     */
    @ApiModelProperty(value = "充值金额时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
    @Max(value = 8, message = "最大值为8", groups = {AddUserTag.class, UpdateUserTag.class})
    @Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Integer rechargeAmountTime;

    /**
     * 充值金额最小金额
     */
    @ApiModelProperty(value = "充值金额最小金额")
    @DecimalMax(value = "100000000", message = "最大值为100000000", groups = {AddUserTag.class, UpdateUserTag.class})
    @DecimalMin(value = "0", message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private BigDecimal rechargeAmountMinAmount;

    /**
     * 充值金额最大金额
     */
    @ApiModelProperty(value = "充值金额最大金额")
    @DecimalMax(value = "100000000", message = "最大值为100000000", groups = {AddUserTag.class, UpdateUserTag.class})
    @DecimalMin(value = "0", message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private BigDecimal rechargeAmountMaxAmount;

    /**
     * 清空充值金额标记
     */
    @ApiModelProperty(value = "清空充值金额标记")
    private Boolean clearRechargeAmount;

    /**
     * 充值次数时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)
     */
    @ApiModelProperty(value = "充值次数时间范围0(不限)1(今天)2(最近7天)3(最近15天)4(最近30天)5(最近45天)6(最近60天)7(最近90天)8(最近180天)")
    @Max(value = 8, message = "最大值为8", groups = {AddUserTag.class, UpdateUserTag.class})
    @Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Integer rechargeNumTime;

    /**
     * 充值次数最小次数
     */
    @ApiModelProperty(value = "充值次数最小次数")
    @Max(value = 100000, message = "最大值为100000", groups = {AddUserTag.class, UpdateUserTag.class})
    @Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Long rechargeNumMinNum;

    /**
     * 充值次数最大次数
     */
    @ApiModelProperty(value = "充值次数最大次数")
    @Max(value = 100000, message = "最大值为100000", groups = {AddUserTag.class, UpdateUserTag.class})
    @Min(value = 0, message = "最小值为0", groups = {AddUserTag.class, UpdateUserTag.class})
    private Long rechargeNumMaxNum;

    /**
     * 清空充值次数标记
     */
    @ApiModelProperty(value = "清空充值次数标记")
    private Boolean clearRechargeNum;

}
