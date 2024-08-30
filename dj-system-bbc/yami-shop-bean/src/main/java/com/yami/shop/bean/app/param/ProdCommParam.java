/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author Yami
 */
@Data
@ApiModel(value= "添加评论信息")
public class ProdCommParam {

    @ApiModelProperty(value = "订单项ID")
    @NotNull(message = "订单项ID不能为空")
    private Long orderItemId;

    @ApiModelProperty(value = "评分，0-5分",required=true)
    @NotNull(message = "评分不能为空")
    private Integer score;

    @ApiModelProperty(value = "评论内容",required=true)
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 500, message = "评论内容长度应该小于{max}")
    private String content;

    @ApiModelProperty(value = "评论图片, 用逗号分隔")
    private String pics;

    @ApiModelProperty(value = "是否匿名(1:是  0:否) 默认为否")
    @NotNull(message = "是否匿名不能为空")
    private Integer isAnonymous;

    @ApiModelProperty(value = "评价(0好评 1中评 2差评)")
    @NotNull(message = "评价不能为空")
    private Integer evaluate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "评价记录时间")
    @NotNull(message = "评价记录时间不能为空")
    private Date recTime;

    }
