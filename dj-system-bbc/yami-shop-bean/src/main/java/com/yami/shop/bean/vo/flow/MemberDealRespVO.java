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
public class MemberDealRespVO {

    @ApiModelProperty("全部成交会员")
    private MemberDealVO allMember;

    @ApiModelProperty("新成交会员")
    private MemberDealVO newMember;

    @ApiModelProperty("老成交会员")
    private MemberDealVO oldMember;

    @ApiModelProperty("图标参数")
    private List<MemberDealTreadVO> trendParam;
}
