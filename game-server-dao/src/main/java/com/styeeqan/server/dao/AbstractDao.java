package com.styeeqan.server.dao;

import com.alibaba.fastjson.JSON;
import com.styeeqan.server.redis.EnumRedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Optional;

/**
 * @author yeeq
 * @date 2022/5/25
 */
public abstract class AbstractDao<Entity, ID> {

    private static final String REDIS_DEFAULT_VALUE = "#null#";

    @Autowired
    protected StringRedisTemplate redisTemplate;

    protected abstract EnumRedisKey getRedisKey();

    protected abstract MongoRepository<Entity, ID> getMongoRepository();

    protected abstract Class<Entity> getEntityClass();

    public Optional<Entity> findById(ID id) {
        Entity entity = null;
        if (id != null) {
            String key = this.getRedisKey().getKey(id.toString());
            String value = redisTemplate.opsForValue().get(key);
            // 说明 Redis 没有信息
            if (value == null) {
                // 保证字符串在常量池
                key = key.intern();
                // 对 key 加锁,防止并发操作,导致缓存穿透
                synchronized (key) {
                    // 二次获取
                    value = redisTemplate.opsForValue().get(key);
                    // 如果 Redis 还是没值,则从数据库获取
                    if (value == null) {
                        Optional<Entity> op = this.getMongoRepository().findById(id);
                        // 如果数据不为空,存储到 Redis
                        if (op.isPresent()) {
                            entity = op.get();
                            this.saveToRedis(entity, id);
                        } else {
                            // 设置默认值,防止缓存穿透
                            this.setRedisDefaultValue(key);
                        }
                    } else if (REDIS_DEFAULT_VALUE.equals(value)) {
                        // 如果是默认值,返回空
                        value = null;
                    }
                }
            } else if (REDIS_DEFAULT_VALUE.equals(value)) {
                // 如果是默认值,返回空
                value = null;
            }

            if (value != null) {
                entity = JSON.parseObject(value, this.getEntityClass());
            }
        }
        return Optional.ofNullable(entity);
    }

    /**
     * 保存数据到数据库或Redis
     */
    public void save(Entity entity, ID id) {
        this.saveToRedis(entity, id);
        this.getMongoRepository().save(entity);
    }

    /**
     * 保存到缓存
     * @param entity entity
     * @param id id
     */
    private void saveToRedis(Entity entity, ID id) {
        String key = this.getRedisKey().getKey(id.toString());
        String value = JSON.toJSONString(entity);
        Duration timeout = this.getRedisKey().getTimeout();
        if (timeout != null) {
            redisTemplate.opsForValue().set(key, value, timeout);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 设置默认值
     * @param key key
     */
    private void setRedisDefaultValue(String key) {
        Duration duration = Duration.ofMinutes(1);
        redisTemplate.opsForValue().set(key, REDIS_DEFAULT_VALUE, duration);
    }
}
