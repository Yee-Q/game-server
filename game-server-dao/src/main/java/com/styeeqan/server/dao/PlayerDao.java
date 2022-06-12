package com.styeeqan.server.dao;

import com.styeeqan.server.entity.Player;
import com.styeeqan.server.entity.UserAccount;
import com.styeeqan.server.redis.EnumRedisKey;
import com.styeeqan.server.repository.PlayerRepository;
import com.styeeqan.server.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yeeq
 * @date 2022/5/29
 */
@Repository
public class PlayerDao extends AbstractDao<Player, String> {

    @Autowired
    private PlayerRepository repository;

    @Override
    protected EnumRedisKey getRedisKey() {
        return EnumRedisKey.USER_ACCOUNT;
    }

    @Override
    protected MongoRepository<Player, String> getMongoRepository() {
        return repository;
    }

    @Override
    protected Class<Player> getEntityClass() {
        return Player.class;
    }

    /**
     * 获取唯一的PlayerId
     * @return Long
     */
    public Long getNextPlayerId(String zoneId) {
        return redisTemplate.opsForValue().increment(EnumRedisKey.PLAYER_ID_INCR.getKey(zoneId));
    }
}
