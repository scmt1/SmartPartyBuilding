/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.controller;

import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.common.bo.TokenInfoBO;
import com.yami.shop.security.common.dto.RefreshTokenDTO;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.vo.TokenInfoVO;
import io.swagger.annotations.Api;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author FrozenWatermelon
 * @date 2020/6/30
 */
@RestController
@Api(tags = "token")
public class TokenController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private MapperFacade mapperFacade;

    @PostMapping("/token/refresh")
    public ServerResponseEntity<TokenInfoVO> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        TokenInfoBO tokenInfoServerResponseEntity = tokenStore
                .refreshToken(refreshTokenDTO.getRefreshToken());
        return ServerResponseEntity
                .success(mapperFacade.map(tokenInfoServerResponseEntity, TokenInfoVO.class));
    }

}
