/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 * @author yami
 */
@Slf4j
public class RedisUtil {

    @SuppressWarnings("unchecked")
    private static RedisTemplate<String,Object> redisTemplate = SpringContextUtils.getBean("redisTemplate",RedisTemplate.class);

    private static StringRedisTemplate stringRedisTemplate = SpringContextUtils.getBean("stringRedisTemplate",StringRedisTemplate.class);


    //=============================common============================
    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public static Boolean expire(String key,long time){
        try {
            if(time>0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("指定Redis缓存失效时间错误：", e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回-1代表为永久有效 失效时间为0，说明该主键未设置失效时间（失效时间默认为-1）
     */
    public static Long getExpire(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false 不存在
     */
    public static Boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("判断Redis中指定的key是否存在错误:", e);
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public static void del(String ... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 删除缓存
     * @param keys key列表
     */
    @SuppressWarnings("unchecked")
    public static void del(List<String> keys){
        if(CollUtil.isEmpty(keys)){
            return;
        }
        redisTemplate.delete(keys);
    }

    //============================String=============================
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key){
        return key==null?null:(T)redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean set(String key,Object value,long time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else{
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("设置Redis缓存错误:", e);
            return false;
        }
    }

    /**
     * 递增 此时value值必须为int类型 否则报错
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public static Long incr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public static Long decr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须小于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, -delta);
    }

    public static boolean setLongValue(String key,Long value,long time) {
        try {
            if(time>0){
                stringRedisTemplate.opsForValue().set(key, String.valueOf(value), time, TimeUnit.SECONDS);
            }else{
                stringRedisTemplate.opsForValue().set(key, String.valueOf(value));
            }
            return true;
        } catch (Exception e) {
            log.error("设置Redis缓存错误:", e);
            return false;
        }
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public static Long getLongValue(String key){
        if (key == null) {
            return null;
        }
        String result = stringRedisTemplate.opsForValue().get(key);
        if (result == null) {
            return null;
        }
        return Long.valueOf(result);
    }

    /**
     * 比较和删除标记，原子性
     * @return 是否成功
     */
    public static boolean cad(String key, String value) {

        if (key.contains(StrUtil.SPACE) || value.contains(StrUtil.SPACE)) {
            throw new BusinessException("网络繁忙");
        }

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        //通过lure脚本原子验证令牌和删除令牌
        Long result = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                Collections.singletonList(key),
                value);

        return !Objects.equals(result, 0L);
    }

    /**
     * 批量删除缓存
     * @param keys
     */
    public static void deleteBatch(List<String> keys) {
        if (CollUtil.isEmpty(keys)) {
            return;
        }
        for (String key : keys) {
            if (key.contains(StrUtil.SPACE)) {
                throw new BusinessException("网络繁忙");
            }
        }
        redisTemplate.delete(keys);
    }

    public static Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
}
