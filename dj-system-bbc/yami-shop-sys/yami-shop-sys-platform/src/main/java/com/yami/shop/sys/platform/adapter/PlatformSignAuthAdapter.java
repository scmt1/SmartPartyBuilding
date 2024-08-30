/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.platform.adapter;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.common.config.Constant;
import com.yami.shop.security.common.adapter.SignAuthAdapter;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.sys.common.dao.SysMenuMapper;
import com.yami.shop.sys.common.dao.SysUserMapper;
import com.yami.shop.sys.common.model.SysMenu;
import com.yami.shop.sys.common.model.SysUser;
import com.yami.shop.sys.common.service.SysUserService;
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
public class PlatformSignAuthAdapter implements SignAuthAdapter {
    private final SysUserService sysUserService;
    private final SysMenuMapper sysMenuMapper;
    private final SysUserMapper sysUserMapper;

    @Override
    public UserInfoInTokenBO loadUserInfoInToken(Map<String, Object> dataMap) {

        UserInfoInTokenBO userInfoInToken = new UserInfoInTokenBO();
        Long userId = (Long)dataMap.get("userId");

        SysUser sysUser;
        // 没有指定用户，默认为管理员
        if (Objects.isNull(userInfoInToken.getBizUserId())) {
            sysUser = sysUserService.getSysUserById((long) Constant.SUPER_ADMIN_ID);
        } else {
            sysUser = sysUserService.getSysUserById(userId);
        }
        userInfoInToken.setUserId(String.valueOf(userId));
        userInfoInToken.setSysType(SysTypeEnum.PLATFORM.value());
        userInfoInToken.setEnabled(sysUser.getStatus() == 1);
        userInfoInToken.setPerms(getUserPermissions(sysUser.getUserId()));
        userInfoInToken.setNikeName(sysUser.getNickName());
        return userInfoInToken;
    }

    private Set<String> getUserPermissions(Long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN_ID){
            List<SysMenu> menuList = sysMenuMapper.selectList(Wrappers.emptyWrapper());
            permsList = menuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
        }else{
            permsList = sysUserMapper.queryAllPerms(userId);
            //去除隐藏按钮的用户权限
            permsList.removeAll(sysMenuMapper.listConcealButtonPerms());
        }

        return permsList.stream().flatMap((perms)->{
                    if (StrUtil.isBlank(perms)) {
                        return null;
                    }
                    return Arrays.stream(perms.trim().split(StrUtil.COMMA));
                }
        ).collect(Collectors.toSet());
    }
}
