/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.bo;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 签名校验完毕之后返回的信息
 * @author FrozenWatermelon
 * @date 2021/12/29
 */
@Data
public class SignResponse {

    /**
     * 改写后的http请求信息，如果是json格式的数据的话，这边是需要进行改写的
     */
    private HttpServletRequest req;
    /**
     * data所在那一层的map数据
     */
    private Map<String, Object> dataMap;

    private String appSecret;

    private Long timestamp;

}
