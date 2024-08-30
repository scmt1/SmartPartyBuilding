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
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 下线处理事件记录项
 *
 * @author
 * @date 2019-09-20 10:44:58
 */
@Data
@TableName("tz_offline_handle_event_item")
public class OfflineHandleEventItem implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long eventItemId;
    /**
     * 事件id
     */
    private Long eventId;
    /**
     * 重新申请上线理由
     */
    private String reapplyReason;
    /**
     * 拒绝原因
     */
    private String refuseReason;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 重新申请上线时间
     */
    private Date reapplyTime;

    /**
     * 审核时间
     */
    private Date auditTime;

}
