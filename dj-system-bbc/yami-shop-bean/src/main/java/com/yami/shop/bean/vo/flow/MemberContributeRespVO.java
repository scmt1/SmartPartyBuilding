/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo.flow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author
 */
@Data
public class MemberContributeRespVO {

    @ApiModelProperty("普通会员")
    private MemberContributeValueVO publicMember;

    @ApiModelProperty("付费会员")
    private MemberContributeValueVO paidMember;

    @ApiModelProperty("符合条件的普通会员userIds")
    private List<Long> userIds;

    @ApiModelProperty("符合条件的付费会员userIds")
    private List<Long> paidUserIds;

}
