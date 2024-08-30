/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.multishop.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.yami.shop.bean.app.param.CheckRegisterSmsParam;
import com.yami.shop.bean.app.param.SendSmsParam;
import com.yami.shop.bean.dto.ShopUserRegisterDto;
import com.yami.shop.bean.enums.SendType;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.i18n.LanguageEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PrincipalUtil;
import com.yami.shop.common.util.RedisUtil;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.service.SmsLogService;
import com.yami.shop.service.SysConfigService;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author lth
 * @Date 2021/7/7 14:43
 */
@RestController
@RequestMapping("/shop/shopUserRegister")
@Api(tags="店铺注册接口")
@AllArgsConstructor
public class ShopUserRegisterController {

    private final SysConfigService sysConfigService;
    private final SmsLogService smsLogService;
    private final ShopEmployeeService shopEmployeeService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordManager passwordManager;

    private static final String CHECK_UPDATE_PWD_SMS_FLAG = "updateShopEmployeePwdSmsFlag";

    @GetMapping("/getMerchantRegisterProtocol")
    @ApiOperation(value = "获取商家注册协议", notes = "获取商家注册协议")
    public ServerResponseEntity<String> getMerchantRegisterProtocol() {
        Integer dbLang = I18nMessage.getDbLang();
        if(Objects.equals(LanguageEnum.LANGUAGE_EN.getLang(), dbLang)) {
            return ServerResponseEntity.success(sysConfigService.getValue(Constant.MERCHANT_REGISTER_PROTOCOL_EN));
        }
        return ServerResponseEntity.success(sysConfigService.getValue(Constant.MERCHANT_REGISTER_PROTOCOL_CN));
    }

    @GetMapping("/getShopProtocol")
    @ApiOperation(value = "获取开店协议", notes = "获取开店协议")
    public ServerResponseEntity<String> getShopProtocol() {
        Integer dbLang = I18nMessage.getDbLang();
        if(Objects.equals(LanguageEnum.LANGUAGE_EN.getLang(), dbLang)) {
            return ServerResponseEntity.success(sysConfigService.getValue(Constant.SHOP_PROTOCOL_EN));
        }
        return ServerResponseEntity.success(sysConfigService.getValue(Constant.SHOP_PROTOCOL_CN));
    }

    @PostMapping("/sendCode")
    @ApiOperation(value = "发送申请开店验证码", notes = "发送申请开店验证码")
    public ServerResponseEntity<Void> sendCode(@Valid @RequestBody SendSmsParam sendSmsParam) {
        int count = shopEmployeeService.count(Wrappers.lambdaQuery(ShopEmployee.class).eq(ShopEmployee::getMobile, sendSmsParam.getMobile()));
        if (count > 0) {
            // 手机号已存在
            throw new YamiShopBindException("yami.phone.number.already.exists");
        }
        smsLogService.sendSms(SendType.VALID, sendSmsParam.getMobile(), sendSmsParam.getMobile(), Maps.newHashMap());
        return ServerResponseEntity.success();
    }

    @PostMapping
    @ApiOperation(value = "注册商家", notes = "注册商家")
    public ServerResponseEntity<Void> register(@Valid @RequestBody ShopUserRegisterDto shopUserRegisterDTO) {
        shopEmployeeService.registerMerchant(shopUserRegisterDTO);
        return ServerResponseEntity.success();
    }

    @PostMapping("/sendUpdatePwdCode")
    @ApiOperation(value = "发送修改密码验证码接口", notes = "发送修改密码验证码接口")
    public ServerResponseEntity<Void> sendUpdatePwdCode(@Valid @RequestBody SendSmsParam sendSmsParam) {
        ShopEmployee shopEmployee = shopEmployeeService.getOne(Wrappers.lambdaQuery(ShopEmployee.class).eq(ShopEmployee::getMobile, sendSmsParam.getMobile()));
        if (Objects.isNull(shopEmployee)) {
            // 手机号不存在
            throw new YamiShopBindException("yami.phone.number.not.exists");
        }
        smsLogService.sendSms(SendType.VALID, shopEmployee.getEmployeeId().toString(), sendSmsParam.getMobile(), Maps.newHashMap());
        return ServerResponseEntity.success();
    }

    @PutMapping("/checkUpdatePwdSms")
    @ApiOperation(value = "修改密码校验验证码", notes = "校验验证码返回校验成功的标识")
    public ServerResponseEntity<String> checkUpdatePwdSms(@Valid @RequestBody CheckRegisterSmsParam checkRegisterSmsParam) {
        boolean isCheckPass = false;
        if (Objects.nonNull(checkRegisterSmsParam) && Objects.nonNull(checkRegisterSmsParam.getMobile())) {
            Matcher m = Pattern.compile(PrincipalUtil.MOBILE_REGEXP).matcher(checkRegisterSmsParam.getMobile());
            isCheckPass = m.matches();
        }
        if (!isCheckPass) {
            throw new YamiShopBindException("yami.user.err.phone");
        }

        ShopEmployee shopEmployee = shopEmployeeService.getOne(Wrappers.lambdaQuery(ShopEmployee.class).eq(ShopEmployee::getMobile, checkRegisterSmsParam.getMobile()));
        if (Objects.isNull(shopEmployee)) {
            // 手机号不存在
            throw new YamiShopBindException("yami.phone.number.not.exists");
        }
        if (!smsLogService.checkValidCode(shopEmployee.getMobile(), checkRegisterSmsParam.getValidCode(), SendType.VALID)) {
            // 验证码有误或已过期
            throw new YamiShopBindException("yami.user.code.error");
        }
        String checkRegisterSmsFlag = IdUtil.simpleUUID();
        RedisUtil.set(CHECK_UPDATE_PWD_SMS_FLAG + checkRegisterSmsFlag, checkRegisterSmsParam.getMobile(), 600);
        return ServerResponseEntity.success(checkRegisterSmsFlag);
    }

    @PutMapping("/updatePwd")
    @ApiOperation(value = "修改密码", notes = "修改密码")
    public ServerResponseEntity<Void> updatePwd(@RequestBody ShopUserRegisterDto shopUserRegisterDto) {
        ShopEmployee shopEmployee = shopEmployeeService.getOne(Wrappers.lambdaQuery(ShopEmployee.class).eq(ShopEmployee::getMobile, shopUserRegisterDto.getMobile()));
        if (Objects.isNull(shopEmployee)) {
            // 手机号不存在
            throw new YamiShopBindException("yami.phone.number.not.exists");
        }
        // 看看有没有校验验证码成功的标识
        if (StrUtil.isBlank(shopUserRegisterDto.getCheckRegisterSmsFlag())) {
            // 验证码已过期，请重新发送验证码校验
            throw new YamiShopBindException("yami.verification.expire");
        } else {
            String checkRegisterSmsFlag = CHECK_UPDATE_PWD_SMS_FLAG + shopUserRegisterDto.getCheckRegisterSmsFlag();
            String checkRegisterSmsFlagMobile = RedisUtil.get(checkRegisterSmsFlag);
            if (!Objects.equals(checkRegisterSmsFlagMobile, shopUserRegisterDto.getMobile())) {
                // 验证码已过期，请重新发送验证码校验
                throw new YamiShopBindException("yami.verification.expire");
            }
        }
        String decryptPassword = passwordManager.decryptPassword(shopUserRegisterDto.getPassword());
        if (StrUtil.isBlank(decryptPassword)) {
            // 新密码不能为空
            throw new YamiShopBindException("yami.user.password.no.exist");
        }
        if (StrUtil.equals(passwordEncoder.encode(decryptPassword), shopEmployee.getPassword())) {
            // 新密码不能与原密码相同!
            throw new YamiShopBindException("yami.user.password.check");
        }
        Boolean updateRes = shopEmployeeService.updatePasswordByEmployeeId(shopEmployee.getEmployeeId(), passwordEncoder.encode(decryptPassword));
        if (!updateRes) {
            throw new YamiShopBindException("更新失败");
        } else {
            RedisUtil.del(CHECK_UPDATE_PWD_SMS_FLAG + shopUserRegisterDto.getCheckRegisterSmsFlag());
        }
        return ServerResponseEntity.success();
    }

}
