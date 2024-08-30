package com.yami.shop.bean.dto.dj;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TotalStudyUserDto {

    @ApiModelProperty(value = "总学分")
    private String totalScore;
    @ApiModelProperty(value = "部门名字")
    private String deptName;
    @ApiModelProperty(value = "党员名字")
    private String partyName;
    @ApiModelProperty(value = "头像地址")
    private String avatar;
    @ApiModelProperty(value = "排名")
    private Integer ranks;
}
