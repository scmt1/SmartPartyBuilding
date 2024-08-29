/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.facade.service;




import java.util.List;

/**
 * 系统配置信息
 * @author lgh
 */
public interface SysConfigService {

    /**
     * 根据key，更新value
     * @param key 参数key
     * @param value 参数value
     */
    void updateValueByKey(String key, String value);

    /**
     * 删除配置信息
     * @param ids 配置项id列表
     */
    void deleteBatch(Long[] ids);

    /**
     * 根据key，获取配置的value值
     * @param key 参数key
     * @return value
     */
    String getValue(String key);


    /**
     * 获取配置信息，并返回对应的类
     * @param key key
     * @param clazz 类
     * @param <T> 泛型
     * @return 泛型
     */
    <T> T  getSysConfigObject(String key, Class<T> clazz);

    /**
     * 删除key的配置信息
     * @param key key
     */
    void removeSysConfig(String key);


}
