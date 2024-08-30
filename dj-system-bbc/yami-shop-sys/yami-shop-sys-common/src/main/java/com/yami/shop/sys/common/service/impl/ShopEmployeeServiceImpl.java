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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.dto.ShopUserRegisterDto;
import com.yami.shop.bean.enums.PositionType;
import com.yami.shop.bean.enums.SendType;
import com.yami.shop.bean.enums.ShopStatus;
import com.yami.shop.bean.model.ShopDetail;
import com.yami.shop.bean.model.TakeStock;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.service.ShopDetailService;
import com.yami.shop.service.SmsLogService;
import com.yami.shop.service.TakeStockService;
import com.yami.shop.sys.common.dao.ShopEmployeeMapper;
import com.yami.shop.sys.common.dao.ShopEmployeeRoleMapper;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 商家端用户
 * @author yami
 * @date 2021-03-01 17:42:09
 */
@Service
@AllArgsConstructor
public class ShopEmployeeServiceImpl extends ServiceImpl<ShopEmployeeMapper, ShopEmployee> implements ShopEmployeeService {

    private final ShopEmployeeMapper shopEmployeeMapper;
    private final ShopEmployeeRoleMapper shopEmployeeRoleMapper;
    private final ShopDetailService shopDetailService;
    private final PasswordEncoder passwordEncoder;
    private final SmsLogService smsLogService;
    private final MapperFacade mapperFacade;
    private final TakeStockService takeStockService;
    private final PasswordManager passwordManager;

    @Override
    public ShopEmployee getShopEmployeeById(Long employeeId) {
        return shopEmployeeMapper.selectById(employeeId);
    }

    @Override
    public Boolean updatePasswordByEmployeeId(Long employeeId, String newPassword) {
        ShopEmployee employee = new ShopEmployee();
        employee.setPassword(newPassword);
        employee.setEmployeeId(employeeId);
        Integer num = shopEmployeeMapper.updateById(employee);
        return num > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUserAndUserRole(ShopEmployee shopEmployee) {
        // 检查用户信息
        this.checkRegisterInfo(shopEmployee);
        shopEmployee.setCreateTime(new Date());
        shopEmployeeMapper.insert(shopEmployee);
        if(CollUtil.isEmpty(shopEmployee.getRoleIdList())){
            return true;
        }
        //保存用户与角色关系
        shopEmployeeRoleMapper.insertUserAndUserRole(shopEmployee.getEmployeeId(), shopEmployee.getRoleIdList());
        return true;
    }

    @Override
    public ShopEmployee getByUserName(String username) {
        return shopEmployeeMapper.getByUserName(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserAndUserRole(ShopEmployee shopEmployee) {
        // 检查用户信息
        this.checkRegisterInfo(shopEmployee);
        // 更新用户
        shopEmployeeMapper.updateById(shopEmployee);
        //先删除用户与角色关系
        shopEmployeeRoleMapper.deleteByEmployeeId(shopEmployee.getEmployeeId());

        if(CollUtil.isEmpty(shopEmployee.getRoleIdList())){
            return true;
        }
        //保存用户与角色关系
        shopEmployeeRoleMapper.insertUserAndUserRole(shopEmployee.getEmployeeId(), shopEmployee.getRoleIdList());
        //更新盘点制单人手机号
        TakeStock stock = takeStockService.getOne(new LambdaQueryWrapper<TakeStock>().eq(TakeStock::getMaker, shopEmployee.getEmployeeId()));
        if (Objects.nonNull(stock)){
            stock.setMakerMobile(shopEmployee.getMobile());
            takeStockService.updateById(stock);
        }
        return true;
    }

    @Override
    public int checkUserName(Long shopId, String mobile) {
        // shopId 为空，判断username是否重复
        if (Objects.isNull(shopId) || Objects.equals(0L,shopId)) {
            return this.count(new LambdaQueryWrapper<ShopEmployee>()
                    .eq(ShopEmployee::getUsername, mobile));
        }
        // 更改超级商家店铺超级管理员时，判断username是否重复
        ShopEmployee employee = getEmployeeSuperAdminByShopId(shopId);
        int count = this.count(new LambdaQueryWrapper<ShopEmployee>()
                .eq(ShopEmployee::getUsername, mobile)
                .ne(ShopEmployee::getEmployeeId,employee.getEmployeeId()));
        return count;
    }

    private ShopEmployee getEmployeeSuperAdminByShopId(Long shopId) {
        // 查询到店铺超级管理员
        List<ShopEmployee> employee = shopEmployeeMapper.selectList(new LambdaQueryWrapper<ShopEmployee>()
                .eq(ShopEmployee::getType,  PositionType.ADMIN.value())
                .eq(ShopEmployee::getShopId, shopId));
        if (!Objects.equals(1, employee.size())) {
            // 一个店铺只允许有一个超级管理员，请检查 tz_shop_employee 和 tz_shop_detail 表中数据，
            // 清除掉多余的超级关理员。一个店铺需要仅一个超级管理员，请处理店铺账号数据
            throw new YamiShopBindException("yami.shop.account.have.only.one");
        }
       return employee.get(0);
    }

    @Override
    public Boolean updatePasswordAndUserName(ShopEmployee shopEmployee) {
        Long shopId = shopEmployee.getShopId();
        // 更改超级商家店铺超级管理员时，判断username是否重复
        // 查询到店铺超级管理员
        ShopEmployee employee = getEmployeeSuperAdminByShopId(shopId);
        Long employeeId = employee.getEmployeeId();
        int count = this.count(new LambdaQueryWrapper<ShopEmployee>()
                .eq(ShopEmployee::getUsername, shopEmployee.getUsername())
                .ne(ShopEmployee::getEmployeeId,employeeId));
        if (count > 0) {
            // 该账号已存在，请重新输入
            throw new YamiShopBindException("yami.store.account.exist");
        }
        shopEmployee.setEmployeeId(employeeId);
        return this.updateById(shopEmployee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePassword(Long employeeId, String newPassword) {
        //        更新密码
        updatePasswordByEmployeeId(employeeId,newPassword);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserNameAndPassword(ShopDetail shopDetail) {
        shopDetailService.updateById(shopDetail);
        String mobile = shopDetail.getMobile();
        String password = shopDetail.getPassword();
        Long shopId = shopDetail.getShopId();
        ShopEmployee employee = getEmployeeSuperAdminByShopId(shopId);
        ShopEmployee update = new ShopEmployee();
        update.setEmployeeId(employee.getEmployeeId());
        update.setUsername(mobile);
        update.setMobile(mobile);
        update.setPassword(password);
        return this.updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePasswordByUserName(String mobile, String newPassword) {
        ShopEmployee employee = this.getByUserName(mobile);
        return this.updatePasswordByEmployeeId(employee.getEmployeeId(),newPassword);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerMerchant(ShopUserRegisterDto shopUserRegisterDTO) {
        ShopEmployee shopEmployee = mapperFacade.map(shopUserRegisterDTO, ShopEmployee.class);
        // 先校验注册信息
        this.checkRegisterInfo(shopEmployee);
        // 校验验证码
        if (!smsLogService.checkValidCode(shopUserRegisterDTO.getMobile(), shopUserRegisterDTO.getValidCode(), SendType.VALID)){
            // 验证码有误或已过期
            throw new YamiShopBindException("yami.user.code.error");
        }
        Date now = new Date();
        // 初始化店铺信息
        ShopDetail shopDetail = new ShopDetail();
        // 状态为开店申请中
        shopDetail.setShopStatus(ShopStatus.APPLYING.value());
        shopDetail.setCreateTime(now);
        shopDetail.setUpdateTime(now);
        // 店铺接收短信通知手机号
        shopDetail.setReceiveMobile(shopEmployee.getMobile());
        shopDetailService.save(shopDetail);
        // 创建商家账号
        shopEmployee.setShopId(shopDetail.getShopId());
        shopEmployee.setType(PositionType.ADMIN.value());
        shopEmployee.setNickname(shopEmployee.getUsername());
        String password = passwordEncoder.encode(passwordManager.decryptPassword(shopEmployee.getPassword()));
        shopEmployee.setPassword(password);
        shopEmployee.setStatus(1);
        shopEmployee.setCreateTime(now);
        shopEmployeeMapper.insert(shopEmployee);
        // 店铺更新店长id
        shopDetailService.update(Wrappers.lambdaUpdate(ShopDetail.class).set(ShopDetail::getUserId, shopEmployee.getEmployeeId()).eq(ShopDetail::getShopId, shopDetail.getShopId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeEmployeesByIds(List<Long> employeeIds) {
        shopEmployeeRoleMapper.deleteByEmployeeIds(employeeIds);
        removeByIds(employeeIds);
    }

    @Override
    public ShopEmployee getMerchantInfoByShopId(Long shopId) {
        return shopEmployeeMapper.getMerchantInfoByShopId(shopId);
    }

    @Override
    public void updateMerchantInfo(ShopEmployee shopEmployee) {
        if (Objects.isNull(shopEmployee.getShopId())) {
            throw new YamiShopBindException("店铺id不能为空");
        }
        ShopEmployee dbShopEmployee = shopEmployeeMapper.selectOne(Wrappers.lambdaQuery(ShopEmployee.class).eq(ShopEmployee::getShopId, shopEmployee.getShopId()).eq(ShopEmployee::getType, PositionType.ADMIN.value()));
        shopEmployee.setEmployeeId(dbShopEmployee.getEmployeeId());
        this.checkRegisterInfo(shopEmployee);
        if (Objects.nonNull(shopEmployee.getPassword())){
            shopEmployee.setPassword(passwordEncoder.encode(shopEmployee.getPassword()));
        }
        shopEmployeeMapper.updateById(shopEmployee);
    }

    @Override
    public List<ShopEmployee> listByParam(ShopEmployee shopEmployee) {
        return shopEmployeeMapper.listByParam(shopEmployee);
    }

    /**
     * 校验注册信息
     * @param shopEmployee
     * @return
     */
    private void checkRegisterInfo(ShopEmployee shopEmployee) {
        if (Objects.nonNull(shopEmployee.getMobile())) {
            // 手机号
            ShopEmployee getByMobile = shopEmployeeMapper.selectOne(Wrappers.lambdaQuery(ShopEmployee.class).eq(ShopEmployee::getMobile, shopEmployee.getMobile()));
            if (Objects.nonNull(getByMobile) && !Objects.equals(shopEmployee.getEmployeeId(), getByMobile.getEmployeeId())) {
                throw new YamiShopBindException("yami.phone.number.already.exists");
            }
        }
        if (Objects.nonNull(shopEmployee.getUsername())) {
            // 用户名
            ShopEmployee getByUsername = shopEmployeeMapper.selectOne(Wrappers.lambdaQuery(ShopEmployee.class).eq(ShopEmployee::getUsername,shopEmployee.getUsername()));
            if (Objects.nonNull(getByUsername) && !Objects.equals(shopEmployee.getEmployeeId(), getByUsername.getEmployeeId())) {
                throw new YamiShopBindException("yami.user.account.exist");
            }
        }
    }
}
