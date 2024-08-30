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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@TableName("tz_attach_file")
public class AttachFile implements Serializable {

    @TableId
    @ApiModelProperty(value = "文件id")
    private Long fileId;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件名字")
    private String fileName;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "文件 1:图片 2:视频 3:文件")
    private Integer type;

    @ApiModelProperty(value = "文件大小(单位；字节)")
    private Integer fileSize;

    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;

    @ApiModelProperty(value = "文件分组id")
    private Long attachFileGroupId;

    @TableField(exist = false)
    @ApiModelProperty(value = "文件id列表")
    private List<Long> fileIds;


    @ApiModelProperty(value = "类型 身份证、毕业证.....")
    private String foreignKey;

    @ApiModelProperty(value = "太多表的附件都在该表，为了避免外键重复，通过添加自定义表名来做标识")
    private String tableType;

}
