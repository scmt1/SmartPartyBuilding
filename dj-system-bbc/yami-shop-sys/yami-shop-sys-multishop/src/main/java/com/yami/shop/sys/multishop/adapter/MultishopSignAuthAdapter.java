/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.multishop.adapter;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.enums.PositionType;
import com.yami.shop.common.config.Constant;
import com.yami.shop.security.common.adapter.SignAuthAdapter;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.sys.common.dao.ShopEmployeeMapper;
import com.yami.shop.sys.common.dao.ShopMenuMapper;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.model.ShopMenu;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 *
 * @author lgh on 2018/09/07.
 */
@Service
@AllArgsConstructor
public class MultishopSignAuthAdapter implements SignAuthAdapter {
    private final ShopMenuMapper shopMenuMapper;
    private final ShopEmployeeMapper shopEmployeeMapper;

    @Override
    public UserInfoInTokenBO loadUserInfoInToken(Map<String, Object> dataMap) {

        UserInfoInTokenBO userInfoInToken = new UserInfoInTokenBO();

        ShopEmployee shopEmployee;
        // 没有指定用户，默认为管理员
        long shopId = dataMap.get("shopId") == null ? Constant.MAIN_SHOP : (Long)dataMap.get("shopId");
        shopEmployee = shopEmployeeMapper.selectOne(new LambdaQueryWrapper<ShopEmployee>()
                .eq(ShopEmployee::getShopId, shopId)
                .eq(ShopEmployee::getType, 0)
        );
        userInfoInToken.setUserId(shopEmployee.getEmployeeId().toString());
        userInfoInToken.setSysType(SysTypeEnum.MULTISHOP.value());
        userInfoInToken.setEnabled(shopEmployee.getStatus() == 1);
        userInfoInToken.setShopId(shopEmployee.getShopId());
        userInfoInToken.setOtherId(shopEmployee.getEmployeeId());
        userInfoInToken.setPerms(getUserPermissions(shopEmployee));
        return userInfoInToken;
    }


    private Set<String> getUserPermissions(ShopEmployee shopEmployee) {
        Integer type = shopEmployee.getType();
        Long employeeId = shopEmployee.getEmployeeId();
        List<String> permsList;

        //店铺超级管理员，拥有最高权限
        if(Objects.equals(PositionType.ADMIN.value(),type)){
            List<ShopMenu> menuList = shopMenuMapper.selectList(Wrappers.emptyWrapper());
            permsList = menuList.stream().map(ShopMenu::getPerms).collect(Collectors.toList());
        }else{
            permsList = shopEmployeeMapper.queryAllPerms(employeeId);
        }

        return permsList.stream().flatMap((perms)->{
            if (StrUtil.isBlank(perms)) {
                return null;
            }
            return Arrays.stream(perms.trim().split(StrUtil.COMMA));
        }).collect(Collectors.toSet());
    }
}
