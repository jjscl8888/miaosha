package com.jjs.miaosha.redis;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author jjs
 * @Version 1.0 2020/4/12
 */
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;



    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            return stringToBean(str, clazz);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public <T> boolean set(KeyPrefix prefix, String key, T value) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (StringUtils.isEmpty(str)) {
                return false;
            }
            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                jedis.set(realKey, str);
            } else {
                jedis.setex(realKey, seconds, str);
            }
            return true;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static  <T> String beanToString(T bean) {
        if (bean == null) {
            return null;
        }
        Class<?> clazz = bean.getClass();
        if (clazz == Integer.class || clazz == int.class) {
            return ""+bean;
        } else if (clazz == String.class) {
            return (String)bean;
        } else if (clazz == Long.class || clazz == long.class) {
            return ""+bean;
        }
        return JSON.toJSONString(bean);
    }

    public static  <T> T stringToBean(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        if (clazz == Integer.class || clazz == int.class) {
            return (T)Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T)str;
        } else if (clazz == Long.class || clazz == long.class) {
            return (T)Long.valueOf(str);
        }
        return JSON.toJavaObject(JSON.parseObject(str), clazz);
    }

    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;

            return jedis.exists(realKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;

            return jedis.incr(realKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;

            return jedis.del(realKey) > 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
