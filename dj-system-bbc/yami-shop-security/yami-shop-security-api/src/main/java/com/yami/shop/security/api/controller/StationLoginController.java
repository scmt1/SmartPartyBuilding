/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.api.controller;

import com.yami.shop.bean.model.Station;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.dto.AuthenticationDTO;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.PasswordCheckManager;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.vo.TokenInfoVO;
import com.yami.shop.service.StationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/6/30
 */
@RestController
@Api(tags = "自提点登录")
public class StationLoginController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private StationService stationService;

    @Autowired
    private PasswordCheckManager passwordCheckManager;

    @Autowired
    private PasswordManager passwordManager;

    @PostMapping("/stationLogin")
    @ApiOperation(value = "自提点账号密码(用于前端登录)", notes = "通过账号/手机号/用户名密码登录")
    public ServerResponseEntity<TokenInfoVO> login(
            @Valid @RequestBody AuthenticationDTO authenticationDTO) {

        Station station = stationService.getStationInfoByAccount(authenticationDTO.getUserName());
        // 密码不正确
        if (Objects.isNull(station)){
            // 账号或密码不正确
            throw new YamiShopBindException("yami.user.account.error");
        }
        String decryptPassword = passwordManager.decryptPassword(authenticationDTO.getPassWord());
        // 半小时内密码输入错误十次，已限制登录30分钟
        passwordCheckManager.checkPassword(SysTypeEnum.STATION,authenticationDTO.getUserName(), decryptPassword, station.getPassword());

        if (!Objects.equals(station.getStatus(),1)){
            // 该自提点已关闭，请联系管理员
            throw new YamiShopBindException("yami.shop.station.close");
        }
        UserInfoInTokenBO userInfoInToken = new UserInfoInTokenBO();
        userInfoInToken.setUserId(station.getStationId().toString());
        userInfoInToken.setOtherId(station.getStationId());
        userInfoInToken.setShopId(station.getShopId());
        userInfoInToken.setSysType(SysTypeEnum.STATION.value());
        userInfoInToken.setEnabled(true);
        // 存储token返回vo
        TokenInfoVO tokenInfoVO = tokenStore.storeAndGetVo(userInfoInToken);
        return ServerResponseEntity.success(tokenInfoVO);
    }




}
