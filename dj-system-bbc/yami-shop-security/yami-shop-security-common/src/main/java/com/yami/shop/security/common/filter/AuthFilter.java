/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.handler.HttpHandler;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.Json;
import com.yami.shop.common.wrapper.ResponseWrapper;
import com.yami.shop.security.common.adapter.AuthConfigAdapter;
import com.yami.shop.security.common.adapter.SignAuthAdapter;
import com.yami.shop.security.common.bo.SignResponse;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.util.AuthUserContext;
import com.yami.shop.security.common.util.SignUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * 授权过滤，只要实现AuthConfigAdapter接口，添加对应路径即可：
 *
 * @author FrozenWatermelon
 * @date 2020/7/11
 */
@Component
public class AuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    private AuthConfigAdapter authConfigAdapter;

    @Autowired
    private HttpHandler httpHandler;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private SignAuthAdapter signAuthAdapter;

    @Autowired
    private SignUtils signUtils;

    public static final String AUTHORIZATION = "Authorization";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String requestUri = req.getRequestURI();

        List<String> excludePathPatterns = authConfigAdapter.excludePathPatterns();

        AntPathMatcher pathMatcher = new AntPathMatcher();
        // 如果匹配不需要授权的路径，就不需要校验是否需要授权
        if (CollectionUtil.isNotEmpty(excludePathPatterns)) {
            for (String excludePathPattern : excludePathPatterns) {
                if (pathMatcher.match(excludePathPattern, requestUri)) {
                    chain.doFilter(req, resp);
                    return;
                }
            }
        }

        String accessToken = req.getHeader(AUTHORIZATION);
        String grantType = req.getHeader(SignUtils.GRANT_TYPE);
        // 也许需要登录，不登陆也能用的uri
        // 比如优惠券接口，登录的时候可以判断是否已经领取过
        // 不能登录的时候会看所有的优惠券，等待领取的时候再登录
        boolean mayAuth = pathMatcher.match(AuthConfigAdapter.MAYBE_AUTH_URI, requestUri);

        try {
            // 通过签名访问
            if (Objects.equals(grantType, SignUtils.GRANT_TYPE_VALUE)) {
                signAccess(chain, req, resp);
            }

            // 如果有token，就要获取token
            else if (StrUtil.isNotBlank(accessToken)) {
                // token访问
                // 这个方法不是controller里面的方法，所以抛出来的异常不会被捕获
                UserInfoInTokenBO userInfoInToken = tokenStore.getUserInfoByAccessToken(accessToken, true);
                // 保存上下文
                AuthUserContext.set(userInfoInToken);

                chain.doFilter(req, resp);
            }

            else if (!mayAuth) {
                // 返回前端未授权
                httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
                return;
            }
        }
        catch (Exception e) {
            if (e instanceof YamiShopBindException) {
                httpHandler.printServerResponseToWeb((YamiShopBindException) e);
                return;
            }
            throw e;
        }
        finally {
            AuthUserContext.clean();
        }

    }

    private void signAccess(FilterChain chain, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        UserInfoInTokenBO userInfoInToken;
        ServerResponseEntity<SignResponse> verifyResponse = signUtils.verify(req);
        SignResponse signResponse = verifyResponse.getData();
        long timestamp = System.currentTimeMillis();

        if (!verifyResponse.isSuccess()) {
            verifyResponse.setSign(SignUtils.sign(signResponse.getAppSecret(), timestamp, null));
            verifyResponse.setTimestamp(timestamp);
            verifyResponse.setData(null);
            httpHandler.printServerResponseToWeb(ServerResponseEntity.success(Json.toJsonString(verifyResponse)));
        }
        userInfoInToken = signAuthAdapter.loadUserInfoInToken(signResponse.getDataMap());
        // 保存上下文
        AuthUserContext.set(userInfoInToken);
        ResponseWrapper responseWrapper = new ResponseWrapper(resp);
        chain.doFilter(signResponse.getReq(), responseWrapper);
        byte[] content = responseWrapper.getContent();
        // 获取相应数据
        String data = null;
        if (content.length > 0) {
            data = new String(content, StandardCharsets.UTF_8);
        }
        ServerResponseEntity<String> successResponse;
        if (responseWrapper.getStatus() != Constant.SUCCESS_CODE) {
            successResponse = ServerResponseEntity.showFailMsg(data);
        } else {
            successResponse = ServerResponseEntity.success(data);
        }
        successResponse.setSign(SignUtils.sign(signResponse.getAppSecret(), timestamp, data));
        successResponse.setTimestamp(timestamp);
        successResponse.setData(data);
        httpHandler.printServerResponseToWeb(successResponse);
    }

}
