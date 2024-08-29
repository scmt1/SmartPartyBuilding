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

/**
 * 微信小程序参数
 * @author yami
 */
@Data
public class WxMiniApp {
    /**
     * 设置微信小程序的appid
     */
    private String appId;

    /**
     * 设置微信小程序的Secret
     */
    private String secret;

}
