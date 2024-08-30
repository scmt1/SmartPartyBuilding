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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.yami.shop.bean.event.PermissionErrorHandleEvent;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.sys.common.dao.ShopMenuMapper;
import com.yami.shop.sys.common.dao.SysMenuMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 用户没有权限的错误处理
 *
 * @author lhd
 */
@Component("permissionErrorHandleListener")
@AllArgsConstructor
public class PermissionErrorHandleListener {
    private final SysMenuMapper sysMenuMapper;
    private final ShopMenuMapper shopMenuMapper;

    @EventListener(PermissionErrorHandleEvent.class)
    public void permissionErrorHandleListener(PermissionErrorHandleEvent event) {
        if (StrUtil.isBlank(event.getPerms())) {
            return;
        }
        Integer lang = I18nMessage.getLang();
        List<String> list;
        // 平台端
        if (Objects.equals(event.getShopId(), Constant.PLATFORM_SHOP_ID)) {
            list = sysMenuMapper.listMenuNameByPerms(event.getPerms(), lang);
        }
        // 商家端
        else {
            list = shopMenuMapper.listMenuNameByPerms(event.getPerms(), lang);
        }
        if (CollUtil.isEmpty(list)) {
            return;
        }
        String messagePre = I18nMessage.getMessage("yami.lack.permission.pre");
        String messageSuf = I18nMessage.getMessage("yami.lack.permission.suf");
        throw new YamiShopBindException(messagePre + list.toString() + messageSuf);
    }
}
