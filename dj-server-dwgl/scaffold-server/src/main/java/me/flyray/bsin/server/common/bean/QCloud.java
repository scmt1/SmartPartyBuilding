/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.common.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 腾讯云cos存储配置信息
 * @author lgh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QCloud extends SwitchBaseModel {

    private String secretId;

    private String secretKey;

    private String region;

    private String bucketName;
}
