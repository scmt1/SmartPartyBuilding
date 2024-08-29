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
import me.flyray.bsin.server.common.enums.QiniuZone;

/**
 * 七牛云存储配置信息
 * @author lgh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Qiniu extends SwitchBaseModel {

    private String accessKey;

    private String secretKey;

    private String bucket;

    private QiniuZone zone;
}
