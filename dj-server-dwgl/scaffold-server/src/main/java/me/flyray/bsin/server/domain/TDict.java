/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author LGH
 * @date 2022-09-22 15:48:48
 */
@Data
@TableName("t_dict")
public class TDict implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    @ApiModelProperty(value = "")
    private Long createBy;
    @ApiModelProperty(value = "")
    private Date createTime;
    @ApiModelProperty(value = "")
    private Integer delFlag;
    @ApiModelProperty(value = "")
    private Long updateBy;
    @ApiModelProperty(value = "")
    private Date updateTime;
    @ApiModelProperty(value = "")
    private String description;
    @ApiModelProperty(value = "")
    private String title;
    @ApiModelProperty(value = "")
    private String type;
    @ApiModelProperty(value = "")
    private Double sortOrder;
}
