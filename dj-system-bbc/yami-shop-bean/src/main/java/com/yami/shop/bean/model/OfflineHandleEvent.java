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

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 下线处理事件
 *
 * @author
 * @date 2019-09-20 10:44:58
 */
@Data
@TableName("tz_offline_handle_event")
public class OfflineHandleEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 事件id
     */
    @TableId
    private Long eventId;

    @ApiModelProperty(value = "1 商品 2 店铺 3满减 4.优惠券 5.团购 6.分销 7.秒杀")
    private Integer handleType;

    @ApiModelProperty(value = "处理id，如果是商品就是商品id，店铺就是店铺id")
    private Long handleId;

    @ApiModelProperty(value = "关联店铺id")
    private Long shopId;

    @ApiModelProperty(value = "处理人id")
    private Long handlerId;

    @ApiModelProperty(value = "处理状态 1平台进行下线 2 重新申请，等待审核 3.审核通过 4 审核未通过")
    private Integer status;

    @ApiModelProperty(value = "下线原因")
    private String offlineReason;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "处理人")
    private String handler;

    @TableField(exist = false)
    @ApiModelProperty(value = "最新申请项")
    private OfflineHandleEventItem lastOfflineHandleEventItem;

    @TableField(exist = false)
    @ApiModelProperty(value = "历史申请列表")
    private List<OfflineHandleEventItem> offlineHandleEventItemList;

    public List<OfflineHandleEventItem> getOfflineHandleEventItemList() {
        if (CollectionUtil.isNotEmpty(offlineHandleEventItemList)) {
            offlineHandleEventItemList = offlineHandleEventItemList.stream().sorted(Comparator.comparing(OfflineHandleEventItem::getCreateTime).reversed()).collect(Collectors.toList());
            return offlineHandleEventItemList;
        }
        return null;
    }


    public OfflineHandleEventItem getLastOfflineHandleEventItem() {
        if (CollectionUtil.isNotEmpty(offlineHandleEventItemList)) {
            return offlineHandleEventItemList.stream().max(Comparator.comparing(OfflineHandleEventItem::getCreateTime)).get();
        }
        return null;
    }
}
