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

import com.yami.shop.bean.model.OfflineHandleEventItem;
import lombok.Data;

import java.util.List;

/**
 * 下线处理时间对象
 */
/**
 * @author Yami
 */
@Data
public class OfflineHandleEventDto {

    /**
     * 处理人
     */
    private String handler;
    /**
     * 处理状态 1平台进行下线 2 重新申请，等待审核 3.审核通过 4 审核未通过
     */
    private Integer status;
    /**
     * 下线原因
     */
    private String offlineReason;

    /**
     * 申请项
     */
    List<OfflineHandleEventItem> offlineHandleEventItemList;
}
