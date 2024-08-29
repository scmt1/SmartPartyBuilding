package me.flyray.bsin.server.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
@Component
public  class RedisUtils {
    private RedisTemplate<Object, Object> redisTemplate;

    public RedisUtils(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean expire(String key, long time) {
        try {
            if (time > 0L) {
                this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public long getExpire(Object key) {
        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public List<String> scan(String pattern) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).build();
        RedisConnectionFactory factory = this.redisTemplate.getConnectionFactory();
        RedisConnection rc = ((RedisConnectionFactory) Objects.requireNonNull(factory)).getConnection();
        Cursor<byte[]> cursor = rc.scan(options);
        ArrayList result = new ArrayList();

        while(cursor.hasNext()) {
            result.add(new String((byte[])cursor.next()));
        }

        try {
            RedisConnectionUtils.releaseConnection(rc, factory);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return result;
    }
    public Object getByKey(String key){
        Object o = this.redisTemplate.opsForValue().get("188888888");
        return o;
    }

    public List<String> findKeysForPage(String patternKey, int page, int size) {
        ScanOptions options = ScanOptions.scanOptions().match(patternKey).build();
        RedisConnectionFactory factory = this.redisTemplate.getConnectionFactory();
        RedisConnection rc = ((RedisConnectionFactory)Objects.requireNonNull(factory)).getConnection();
        Cursor<byte[]> cursor = rc.scan(options);
        List<String> result = new ArrayList(size);
        int tmpIndex = 0;
        int fromIndex = page * size;
        int toIndex = page * size + size;

        while(cursor.hasNext()) {
            if (tmpIndex >= fromIndex && tmpIndex < toIndex) {
                result.add(new String((byte[])cursor.next()));
                ++tmpIndex;
            } else {
                if (tmpIndex >= toIndex) {
                    break;
                }

                ++tmpIndex;
                cursor.next();
            }
        }

        try {
            RedisConnectionUtils.releaseConnection(rc, factory);
        } catch (Exception var13) {
            var13.printStackTrace();
        }

        return result;
    }

    public boolean hasKey(String key) {
        try {
            return this.redisTemplate.hasKey(key);
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                this.redisTemplate.delete(key[0]);
            } else {
                this.redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }

    }

    public Object get(String key) {
        return key == null ? null : this.redisTemplate.opsForValue().get(key);
    }

    public List<Object> multiGet(List<String> keys) {
        Object obj = this.redisTemplate.opsForValue().multiGet(Collections.singleton(keys));
        return null;
    }

    public boolean set(String key, Object value) {
        try {
            this.redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0L) {
                this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                this.set(key, value);
            }

            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public  boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0L) {
                this.redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                this.set(key, value);
            }

            return true;
        } catch (Exception var7) {
            var7.printStackTrace();
            return false;
        }
    }

    public  boolean setIfAbsent(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            return this.redisTemplate.opsForValue().setIfAbsent(key, value, time, timeUnit);
        } catch (Exception var7) {
            var7.printStackTrace();
            return false;
        }
    }


    public Object hget(String key, String item) {
        return this.redisTemplate.opsForHash().get(key, item);
    }

    public Map<Object, Object> hmget(String key) {
        return this.redisTemplate.opsForHash().entries(key);
    }

    public boolean hmset(String key, Map<String, Object> map) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public boolean hset(String key, String item, Object value) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public boolean hset(String key, String item, Object value, long time) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var7) {
            var7.printStackTrace();
            return false;
        }
    }

    public void hdel(String key, Object... item) {
        this.redisTemplate.opsForHash().delete(key, item);
    }

    public boolean hHasKey(String key, String item) {
        return this.redisTemplate.opsForHash().hasKey(key, item);
    }

    public double hincr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, by);
    }

    public double hdecr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, -by);
    }

    public Set<Object> sGet(String key) {
        try {
            return this.redisTemplate.opsForSet().members(key);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public boolean sHasKey(String key, Object value) {
        try {
            return this.redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public long sSet(String key, Object... values) {
        try {
            return this.redisTemplate.opsForSet().add(key, values);
        } catch (Exception var4) {
            var4.printStackTrace();
            return 0L;
        }
    }

    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = this.redisTemplate.opsForSet().add(key, values);
            if (time > 0L) {
                this.expire(key, time);
            }

            return count;
        } catch (Exception var6) {
            var6.printStackTrace();
            return 0L;
        }
    }

    public long sGetSetSize(String key) {
        try {
            return this.redisTemplate.opsForSet().size(key);
        } catch (Exception var3) {
            var3.printStackTrace();
            return 0L;
        }
    }

    public long setRemove(String key, Object... values) {
        try {
            Long count = this.redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception var4) {
            var4.printStackTrace();
            return 0L;
        }
    }

    public List<Object> lGet(String key, long start, long end) {
        try {
            return this.redisTemplate.opsForList().range(key, start, end);
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public long lGetListSize(String key) {
        try {
            return this.redisTemplate.opsForList().size(key);
        } catch (Exception var3) {
            var3.printStackTrace();
            return 0L;
        }
    }

    public Object lGetIndex(String key, long index) {
        try {
            return this.redisTemplate.opsForList().index(key, index);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public boolean lSet(String key, Object value) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public boolean lSet(String key, Object value, long time) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public boolean lSet(String key, List<Object> value) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public boolean lSet(String key, List<Object> value, long time) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            this.redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public long lRemove(String key, long count, Object value) {
        try {
            return this.redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception var6) {
            var6.printStackTrace();
            return 0L;
        }
    }
}
