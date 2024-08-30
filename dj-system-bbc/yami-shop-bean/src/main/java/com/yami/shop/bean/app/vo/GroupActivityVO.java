/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yami
 * @date 2019/8/30 9:59
 */
@Data
public class GroupActivityVO {

    @ApiModelProperty(value = "拼团活动id")
    private Long groupActivityId;

    @ApiModelProperty(value = "商品id")
    private Long prodId;

    @ApiModelProperty(value = "拼团状态(1:启用、2:未启用、0:已失效、-1:删除、3:违规下架、4:平台审核 5:已结束)")
    private Integer status;

    @ApiModelProperty(value = "活动开始时间")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间")
    private Date endTime;

    @ApiModelProperty(value = "服务器当前时间")
    private Date nowTime;

    @ApiModelProperty(value = "成团人数")
    private Integer groupNumber;

    @ApiModelProperty(value = "商品是否限购（1:限购、0:不限购）")
    private Integer hasMaxNum;

    @ApiModelProperty(value = "限购数量")
    private Integer maxNum = 0;

    @ApiModelProperty(value = "已购买数量")
    private Integer prodCount;

    @ApiModelProperty(value = "当前用户参团的团队ID（null表示还没有参加该活动）")
    private Long joinGroupTeamId;

    @ApiModelProperty(value = "成团有效时间")
    private Integer groupValidTime;

    @ApiModelProperty(value = "是否开启凑团模式（1:凑团、0:不凑团）", notes = "开启模拟成团后，拼团有效期内人数未满的团，系统将会模拟“匿名买家”凑满人数，使该团成团。 你只需要对已付款参团的真实买家发货。建议合理开启，以提高成团率。")
    private Integer hasGroupTip;

    @ApiModelProperty(value = "是否开启活动预热（1:预热、0:不预热）", notes = "开启后，商品详情页展示未开始的拼团活动，但活动开始前用户无法拼团购买")
    private Integer isPreheat;

    @ApiModelProperty(value = "活动状态（活动状态：1:未开始、2:进行中、3:已结束、4:已失效、5:违规下架、6：等待审核）")
    private Integer activityStatus;

    @ApiModelProperty(value = "商品活动价格（最低价）")
    private Double actPrice;

    @ApiModelProperty(value = "sku列表")
    private List<GroupSkuVO> groupSkuList;
}
