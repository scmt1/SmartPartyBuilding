/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.yami.shop.common.config.SensitiveWordConfig;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.handler.SensitiveHandler;
import com.yami.shop.common.wrapper.RequestWrapper;
import com.yami.shop.common.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Citrus
 * @date 2021/8/17 11:05
 */
@Slf4j
@Component
public class SensitiveFilter implements Filter {

    Logger logger = LoggerFactory.getLogger(getClass().getName());


    @Autowired
    private SensitiveHandler sensitiveHandler;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        ServletRequestAttributes attributes = new ServletRequestAttributes(req, resp);
        RequestContextHolder.setRequestAttributes(attributes);
        logger.info("uri:{}", req.getRequestURI());
        Set<String> sensitiveWhiteSet = SensitiveWordConfig.getSensitiveWhiteSet();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (CollectionUtil.isNotEmpty(sensitiveWhiteSet)){
            for (String sensitiveUrl : sensitiveWhiteSet) {
                if (antPathMatcher.match(sensitiveUrl,req.getRequestURI())){
                    chain.doFilter(req, resp);
                    return;
                }
            }
        }
        ResponseWrapper responseWrapper = new ResponseWrapper(resp);
        // 敏感词输入过滤
        if (StrUtil.isNotBlank(req.getContentType()) && req.getContentType().contains(ContentType.JSON.getValue())) {
            RequestWrapper requestWrapper;
            requestWrapper = new RequestWrapper(req);
            String body = requestWrapper.getBody();
            if (StrUtil.isNotBlank(body) && sensitiveHandler.isSensitive(body)) {
                throw new YamiShopBindException("yami.sensitive.words.reenter");
            }
            chain.doFilter(requestWrapper, responseWrapper);
        } else {
            Map<String, String[]> parameterMap = req.getParameterMap();
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : parameterMap.keySet()) {
                stringBuilder.append(parameterMap.get(s)[0]);
            }
            // 传入参数是否包含敏感词,最小匹配
            if (StrUtil.isNotBlank(stringBuilder.toString()) && sensitiveHandler.isSensitive(stringBuilder.toString())) {
                throw new YamiShopBindException("yami.sensitive.words.reenter");
            }
            //xss
            chain.doFilter(req, responseWrapper);
        }
        boolean isType = Objects.nonNull(responseWrapper.getContentType()) &&
                (responseWrapper.getContentType().contains(ContentType.JSON.getValue()) || responseWrapper.getContentType().contains(ContentType.TEXT_PLAIN.getValue()));
        if (isType) {
            byte[] content = responseWrapper.getContent();
            //敏感词输出过滤
            if (content.length > 0) {
                String s = new String(content, StandardCharsets.UTF_8);
                //属于json格式再处理
                String sensitiveText = null;
                try {
                    sensitiveText = sensitiveHandler.replaceSensitiveWord(s, "*");
                } catch (Exception e) {
                    log.error("替换敏感词失败:", e);
                }
                ServletOutputStream outputStream = resp.getOutputStream();
                outputStream.write(sensitiveText != null ? sensitiveText.getBytes(StandardCharsets.UTF_8) : new byte[0]);
                outputStream.flush();
            }
        }
    }
}
