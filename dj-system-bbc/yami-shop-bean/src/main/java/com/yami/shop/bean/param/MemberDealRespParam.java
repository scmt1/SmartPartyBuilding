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
public class MemberDealRespParam {

    @ApiModelProperty("全部成交会员")
    private MemberDealParam allMember;

    @ApiModelProperty("新成交会员")
    private MemberDealParam newMember;

    @ApiModelProperty("老成交会员")
    private MemberDealParam oldMember;

    @ApiModelProperty("图标参数")
    private List<MemberDealTreadParam> trendParam;



}
