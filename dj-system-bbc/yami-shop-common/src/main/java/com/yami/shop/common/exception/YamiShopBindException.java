/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.exception;

import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import lombok.Data;
import lombok.Getter;

/**
 * 自定义异常
 * @author yami
 */
@Getter
public class YamiShopBindException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = -4137688758944857209L;

    /**
     * http状态码
     */
    private String code;

    private Object object;

    private ServerResponseEntity<?> serverResponseEntity;

    /**
     * @param responseEnum http状态码
     */
    public YamiShopBindException(ResponseEnum responseEnum) {
        super(responseEnum.getMsg());
        this.code = responseEnum.value();
    }

    /**
     * @param responseEnum http状态码
     */
    public YamiShopBindException(ResponseEnum responseEnum, String msg) {
        super(I18nMessage.getMessage(msg));
        this.code = responseEnum.value();
    }

    public YamiShopBindException(ServerResponseEntity<?> serverResponseEntity) {
        this.serverResponseEntity = serverResponseEntity;
    }

    public YamiShopBindException(String msg) {
//        super(msg);
        super(I18nMessage.getMessage(msg));
        this.code = ResponseEnum.SHOW_FAIL.value();
    }

    public YamiShopBindException(String msg, Object object) {
        super(I18nMessage.getMessage(msg));
        this.code = ResponseEnum.SHOW_FAIL.value();
        this.object = object;
    }

    public YamiShopBindException(String code, String msg) {
        super(I18nMessage.getMessage(msg));
        this.code = code;
    }
}
