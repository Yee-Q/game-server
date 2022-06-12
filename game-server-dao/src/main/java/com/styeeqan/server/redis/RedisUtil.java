package com.styeeqan.server.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author yeeq
 * @date 2022/6/3
 */
@Component
public class RedisUtil {

    @Resource
    private StringRedisTemplate template;

    public String getRedisKey(EnumRedisKey redisKey, String id) {
        if (StringUtils.isEmpty(id)) {
            return redisKey.getKey();
        }
        return redisKey.getKey(id);
    }

    public Boolean setValueIfAbsent(String key, String value) {
        return template.opsForValue().setIfAbsent(key, value);
    }

    public void setValue(String key, String value) {
        template.opsForValue().set(key, value);
    }
}
