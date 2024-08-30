package com.yami.shop.bean.dto.dj;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class StudyUserDto {


    @ApiModelProperty(value = "视频或文件名字")
    private String name;
    @ApiModelProperty(value = "具体描述")
    private String description;
    @ApiModelProperty(value = "1文件精神，2党史学习，3在线视频，4、专项答题")
    private String type;
    @ApiModelProperty(value = "视频类型，如果为空则说明是文件")
    private String videoType;
    @ApiModelProperty(value = "学分")
    private Integer studyScore;
    @ApiModelProperty(value = "学时")
    private Integer studyHours;
    @ApiModelProperty(value = "获得时间")
    private Date updateTime;
}
