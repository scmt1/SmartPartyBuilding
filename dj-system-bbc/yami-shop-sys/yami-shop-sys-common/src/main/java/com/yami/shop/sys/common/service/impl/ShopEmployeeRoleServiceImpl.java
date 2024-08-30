/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.sys.common.dao.ShopEmployeeRoleMapper;
import com.yami.shop.sys.common.model.ShopEmployeeRole;
import com.yami.shop.sys.common.service.ShopEmployeeRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户与角色对应关系
 * @author yami
 * @date 2021-03-01 17:42:09
 */
@Service
@AllArgsConstructor
public class ShopEmployeeRoleServiceImpl extends ServiceImpl<ShopEmployeeRoleMapper, ShopEmployeeRole> implements ShopEmployeeRoleService {

    private final ShopEmployeeRoleMapper shopEmployeeRoleMapper;
}
