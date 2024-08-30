/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@ApiModel("公告对象")
@Data
public class NoticeDto {

    @ApiModelProperty(value = "公告id")
    private Long id;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty("类型不能为空（平台端同时创建多类型公告,用逗号隔开）(1:在商家端展示 2:在用户端展示 3：在供应商端展示)")
    private String types;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "公告内容")
    private String content;

    @ApiModelProperty("是否置顶（1:是 0：否）")
    private Integer isTop;

    @ApiModelProperty(value = "公告发布时间")
    private Date publishTime;

    @ApiModelProperty("商家可见范围 (用逗号隔开，为空则全部可见)")
    private String multiShopIds;

    @ApiModelProperty("用户可见范围 (用逗号隔开，为空则全部可见)")
    private String userIds;

    @ApiModelProperty("是否立即发送 1：立即发送  0：定时发送")
    private Integer immediatelySend;

    @ApiModelProperty("定时发送时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    @ApiModelProperty("状态(1:公布 0:撤回)")
    private Integer status;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public interface NoContent{}

    public interface WithContent extends NoContent{}

}
