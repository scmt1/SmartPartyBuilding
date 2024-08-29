/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.common.configService.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import me.flyray.bsin.facade.service.SysConfigService;
import me.flyray.bsin.server.common.bean.*;

import me.flyray.bsin.server.common.config.Constant;
import me.flyray.bsin.server.common.param.ScoreConfigParam;
import me.flyray.bsin.server.mapper.SysConfigMapper;
import me.flyray.bsin.server.utils.Json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lgh
 */
@Slf4j
@Service("sysConfigService")
@AllArgsConstructor
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Autowired
    private final SysConfigMapper sysConfigMapper;

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames="SysConfigObject",key="#key"),
            @CacheEvict(cacheNames="SysConfig",key="#key")
    })
    public void updateValueByKey(String key, String value) {
        sysConfigMapper.updateValueByKey(key, value);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        sysConfigMapper.deleteBatch(ids);
    }

    @Override
    @Cacheable(cacheNames = "SysConfig", key = "#key")
    public String getValue(String key) {
        SysConfig config = sysConfigMapper.queryByKey(key);
        return config == null ? null : config.getParamValue();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames="SysConfigObject",key="#key"),
            @CacheEvict(cacheNames="SysConfig",key="#key")
    })
    public void removeSysConfig(String key) {
    }




    @Caching(evict = {
            @CacheEvict(cacheNames="SysConfigObject",key="#sysConfig.paramKey"),
            @CacheEvict(cacheNames="SysConfig",key="#sysConfig.paramKey")
    })
    public void saveOrUpdateSysConfigByKey(SysConfig sysConfig) {
        QueryWrapper<SysConfig> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("tz_sys_config.para_key",sysConfig.getParamKey());

        int count = sysConfigMapper.selectCount(new LambdaQueryWrapper<SysConfig>()
              .eq(SysConfig::getParamKey, sysConfig.getParamKey()));

        /*int count = sysConfigMapper.selectCount(Wrappers.lambdaQuery(SysConfig.class)
                .eq(SysConfig::getParamKey, sysConfig.getParamKey())
        );*/
        if (count > 0) {
            sysConfigMapper.updateValueByKey(sysConfig.getParamKey(), sysConfig.getParamValue());
        } else {
            sysConfig.setId(null);
            sysConfigMapper.insert(sysConfig);
        }
    }

    /**
     * 关闭激活开关
     * @param key 配置项key
     * @param clazz 继承了SwitchBaseModel的类
     * @param invalidKeys 缓存失效的配置集合
     * @param <T>
     */
    public <T> void closeSwitch(String key, Class<T> clazz, List<String> invalidKeys) {
        if (!SwitchBaseModel.class.isAssignableFrom(clazz)) {
            // 不是配置开关属性类的子类不能调用该方法
            return;
        }
        T valueObj = this.getSysConfigObject(key, clazz);
        if (Objects.isNull(valueObj)) {
            // 当前配置在数据库不存在
            return;
        }
        try {
            PropertyDescriptor pd = new PropertyDescriptor("isOpen", clazz);
            Method readMethod = pd.getReadMethod();
            if (BooleanUtil.isFalse((Boolean) readMethod.invoke(valueObj))) {
                // isOpen已经是关闭状态，直接返回
                return;
            }
            Method writeMethod = pd.getWriteMethod();
            // 关闭当前配置
            writeMethod.invoke(valueObj, false);
        } catch (Exception e) {
            log.error(clazz + "类配置存在问题: ", e);
            return;
        }
        String str = Json.toJsonString(valueObj);
        sysConfigMapper.updateValueByKey(key, str);
        invalidKeys.add(key);
    }

    @Override
    @Cacheable(cacheNames="SysConfigObject",key="#key")
    public <T> T  getSysConfigObject(String key, Class<T> clazz) {
        String value = getValue(key);
        if (StrUtil.isBlank(value)) {
            return null;
        }

        if(Objects.equals("java.lang.String",clazz.getName())){
            return (T)value;
        }else{
            return Json.parseObject(value, clazz);
        }
    }
}
