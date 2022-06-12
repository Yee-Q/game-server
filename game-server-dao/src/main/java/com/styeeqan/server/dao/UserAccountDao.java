package com.styeeqan.server.dao;

import com.styeeqan.server.entity.UserAccount;
import com.styeeqan.server.redis.EnumRedisKey;
import com.styeeqan.server.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yeeq
 * @date 2022/5/29
 */
@Repository
public class UserAccountDao extends AbstractDao<UserAccount, String> {

    @Autowired
    private UserAccountRepository repository;

    @Override
    protected EnumRedisKey getRedisKey() {
        return EnumRedisKey.USER_ACCOUNT;
    }

    @Override
    protected MongoRepository<UserAccount, String> getMongoRepository() {
        return repository;
    }

    @Override
    protected Class<UserAccount> getEntityClass() {
        return UserAccount.class;
    }

    /**
     * 获取唯一的用户ID
     * @return Long
     */
    public Long getNextUserId() {
        return redisTemplate.opsForValue().increment(EnumRedisKey.USER_ID_INCR.getKey());
    }
}
