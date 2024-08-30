/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.common.listener;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.dto.ShopUserRegisterDto;
import com.yami.shop.bean.enums.PositionType;
import com.yami.shop.bean.enums.SendType;
import com.yami.shop.bean.event.ShopEmployeeSuperAdminEvent;
import com.yami.shop.bean.event.UpdateShopAccountEvent;
import com.yami.shop.bean.event.UpdateShopSuperAdminAccountEvent;
import com.yami.shop.bean.model.ShopDetail;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.service.ShopDetailService;
import com.yami.shop.service.SmsLogService;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * 新增店铺职工
 * @author LGH
 */
@Component("shopEmployeeSuperAdmin")
@AllArgsConstructor
public class ShopEmployeeSuperAdminListener {

    private final ShopEmployeeService shopEmployeeService;
    private final MapperFacade mapperFacade;
    private final PasswordEncoder passwordEncoder;
    private final ShopDetailService shopDetailService;
    private final SmsLogService smsLogService;
    private final PasswordManager passwordManager;

    @EventListener(ShopEmployeeSuperAdminEvent.class)
    public void shopEmployeeSuperAdminListener(ShopEmployeeSuperAdminEvent event) {
        ShopUserRegisterDto shopUserRegisterDto = event.getShopUserRegisterDto();
        if (Objects.isNull(shopUserRegisterDto)) {
            return;
        }
        int countByUsername = shopEmployeeService.count(Wrappers.lambdaQuery(ShopEmployee.class)
                .eq(ShopEmployee::getUsername, shopUserRegisterDto.getUsername())
                .ne(ShopEmployee::getShopId, shopUserRegisterDto.getShopId()));
        if (countByUsername > 0) {
            throw new YamiShopBindException("yami.user.account.exist");
        }
        int countByMobile = shopEmployeeService.count(Wrappers.lambdaQuery(ShopEmployee.class)
                .eq(ShopEmployee::getMobile, shopUserRegisterDto.getMobile())
                .ne(ShopEmployee::getShopId, shopUserRegisterDto.getMobile()));
        if (countByMobile > 0) {
            throw new YamiShopBindException("yami.phone.number.already.exists");
        }
//        if (Objects.nonNull(shopUserRegisterDto.getValidCode())) {
//            if (!smsLogService.checkValidCode(shopUserRegisterDto.getMobile(), shopUserRegisterDto.getValidCode(), SendType.VALID)){
//                // 验证码有误或已过期
//                throw new YamiShopBindException("yami.user.code.error");
//            }
//        }
        String decryptPassword = passwordManager.decryptPassword(shopUserRegisterDto.getPassword());
        Date now = new Date();
        ShopEmployee shopEmployee = new ShopEmployee();
        shopEmployee.setShopId(shopUserRegisterDto.getShopId());
        shopEmployee.setType(PositionType.ADMIN.value());
        shopEmployee.setUsername(shopUserRegisterDto.getUsername());
        shopEmployee.setNickname(shopUserRegisterDto.getUsername());
        shopEmployee.setMobile(shopUserRegisterDto.getMobile());
        shopEmployee.setStatus(1);
        shopEmployee.setCreateEmployeeId(0L);
        shopEmployee.setTenantId(shopUserRegisterDto.getTenantId());
        shopEmployee.setOrgId(shopUserRegisterDto.getOrgId());
        String password = passwordEncoder.encode(decryptPassword);
        shopEmployee.setPassword(password);
        shopEmployee.setCreateTime(now);
        shopEmployeeService.save(shopEmployee);
        // 店铺更新店长id
        shopDetailService.update(Wrappers.lambdaUpdate(ShopDetail.class).set(ShopDetail::getUserId, shopEmployee.getEmployeeId()).eq(ShopDetail::getShopId, shopUserRegisterDto.getShopId()));
    }

    @EventListener(UpdateShopSuperAdminAccountEvent.class)
    public void updateShopEmployeeSuperAdminListener(UpdateShopSuperAdminAccountEvent event) {
        ShopDetail shopDetail = event.getShopDetail();
        ShopEmployee shopEmployee = new ShopEmployee();
        mapperFacade.map(shopDetail,shopEmployee);
        shopEmployee.setMobile(shopDetail.getMobile());
        shopEmployeeService.updatePasswordAndUserName(shopEmployee);
    }

    @EventListener(UpdateShopAccountEvent.class)
    public void updateShopEmployeeSuperAdminListener(UpdateShopAccountEvent event) {
        ShopDetail shopDetail = event.getShopDetail();
        ShopEmployee shopEmployee = new ShopEmployee();
        shopEmployee.setShopId(shopDetail.getShopId());
        shopEmployee.setMobile(shopDetail.getMobile());
        shopEmployeeService.updatePasswordAndUserName(shopEmployee);
    }

}
