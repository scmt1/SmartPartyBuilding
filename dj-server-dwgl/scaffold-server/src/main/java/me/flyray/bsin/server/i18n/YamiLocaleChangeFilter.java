/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.i18n;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

/**
 * RequestContextFilter 会传入默认的Locale，优先级(-105) 要比RequestContextFilter优先级高
 * @author LGH
 */
@Slf4j
@Component
@Order(-104)
public class YamiLocaleChangeFilter implements Filter {

    public static String ZH_CN = "zh_CN";
    public static String ZH_CN_L = "zh_cn";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String newLocale = request.getHeader("locale");
        if(Objects.equals(newLocale,"zh_CN")|| Objects.equals(newLocale,"zh_cn")){
            newLocale = "zh";
        }
        if (newLocale != null) {
            String lowerLocale = newLocale.toLowerCase();
            LocaleContextHolder.setLocale(new Locale(lowerLocale));
        }
        filterChain.doFilter(request, response);
    }
}
