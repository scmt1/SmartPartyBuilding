/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chendt
 * @date 2021/7/7 17:16
 */
@Data
@TableName("tz_attach_file_group")
public class AttachFileGroup implements Serializable {

    @TableId
    private Long attachFileGroupId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "分组名称")
    private String name;

    @ApiModelProperty(value = "文件类型：1:图片 2:视频 3:文件")
    private Integer type;
}
