/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.multishop.adapter;

import com.yami.shop.security.common.adapter.DefaultAuthConfigAdapter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/7/16
 */
@Component
public class ResourceServerAdapter extends DefaultAuthConfigAdapter {

    public static final List<String> EXCLUDE_PATH = Arrays.asList(
            "/webjars/**",
            "/swagger/**",
            "/v2/api-docs",
            "/doc.html",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/captcha/**",
            "/order/refund/result/**",
            "/sys/webConfig/getActivity",
            "/shop/shopUserRegister/**",
            "/shopLogin",
            "/appLogin",
            "/actuator/health/liveness",
            "/actuator/health/readiness",
          "/scmt/tzSysDept/getAllDeptName",
          "/createShop",
          "/swagger-ui.html/**",
          "/swagger-ui/**" ,
                "/resetPassword");

    @Override
    public List<String> excludePathPatterns() {
        return EXCLUDE_PATH;
    }
}
