/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.common.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 系统日志
 * @author yami
 */
@Data
@TableName("tz_sys_log")
public class SysLog implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户操作")
    private String operation;

    @ApiModelProperty("请求方法")
    private String method;

    @ApiModelProperty("请求参数")
    private String params;

    @ApiModelProperty("执行时长(毫秒)")
    private Long time;

    @ApiModelProperty("IP地址")
    private String ip;

    @ApiModelProperty("创建时间")
    private Date createDate;

}
