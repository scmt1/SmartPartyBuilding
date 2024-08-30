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

/**
 *
 *
 * @author lhd
 * @date 2020-09-12 10:08:46
 */
@Data
@TableName("tz_notify_template_tag")
public class NotifyTemplateTag implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 标签消息通知关联表
     */
    @TableId
    private Long notifyShopId;
    /**
     * 标签id
     */
    private Long userTagId;
    /**
     * 模板id
     */
    private Long templateId;

}
