/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
public class TaizhangDto {

    @ApiModelProperty(value = "文件名")
    private String fileName;
    @ApiModelProperty(value = "文件id")
    private Long fileId;

    @ApiModelProperty(value = "所属部门")
    private String deptName;

    @ApiModelProperty(value = "会议名称")
    private String meetingName;

    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;
    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "大小")
    private Integer size;

    @ApiModelProperty(value = "会议类型")
    private String meetingType;


}
