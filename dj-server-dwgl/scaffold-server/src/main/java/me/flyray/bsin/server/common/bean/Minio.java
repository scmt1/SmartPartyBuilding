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
 * minio配置信息
 * @Author lth
 * @Date 2021/11/11 10:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Minio extends SwitchBaseModel {
    private String bucketName;

    private String accessKey;

    private String secretKey;

    private String endpoint;
}
