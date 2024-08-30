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

/**
 * 下线处理事件
 *
 * @author Yami
 */
@Data
public class OfflineHandleEventAuditParam {

    @ApiModelProperty(value = "事件id")
    private Long eventId;

    @ApiModelProperty(value = "对象id")
    private Long handleId;

    @ApiModelProperty(value = "申请理由")
    private String reapplyReason;

    @ApiModelProperty(value = "拒绝理由")
    private String refuseReason;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "事件项id")
    private Integer eventItemId;
}
