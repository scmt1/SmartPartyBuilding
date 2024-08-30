/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto.flow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author lhd
 */
@Data
public class MemberReqDTO extends CustomerReqDTO {

    @ApiModelProperty("0 全部会员 1免费会员(普通会员) 2付费会员")
    private Integer memberType;

    @ApiModelProperty("领券会员数")
    private Long receiverUserNum;

    @ApiModelProperty("几年前")
    private Date beforeYear;
}
