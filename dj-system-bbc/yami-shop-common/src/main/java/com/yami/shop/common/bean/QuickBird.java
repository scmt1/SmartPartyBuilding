/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

/**
 * 快递鸟物流详情查询
 * @author yami
 */
@EqualsAndHashCode(callSuper = true)
public class QuickBird extends SwitchBaseModel {

    /**
     * 快递鸟中顺丰编码
     */
    @JsonIgnore
    public final static String SF_CODE = "SF";

    private String eBusinessID;
    private String appKey;
    private String reqUrl;

    public String geteBusinessID() {
        return eBusinessID;
    }

    public void seteBusinessID(String eBusinessID) {
        this.eBusinessID = eBusinessID;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    @Override
    public String toString() {
        return "QuickBird{" +
                "eBusinessID='" + eBusinessID + '\'' +
                ", appKey='" + appKey + '\'' +
                ", reqUrl='" + reqUrl + '\'' +
                '}';
    }
}
