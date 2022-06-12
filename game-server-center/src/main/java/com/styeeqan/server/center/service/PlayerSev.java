package com.styeeqan.server.center.service;

import com.styeeqan.server.dao.PlayerDao;
import com.styeeqan.server.entity.Player;
import com.styeeqan.server.common.error.GameServerError;
import com.styeeqan.server.common.exception.GameServerException;
import com.styeeqan.server.redis.EnumRedisKey;
import com.styeeqan.server.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yeeq
 * @date 2022/5/29
 */
@Service
public class PlayerSev {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PlayerDao playerDao;

    public Player createPlayer(String zoneId, String nickName) {
        boolean saveNickName = saveNickNameIfAbsent(zoneId, nickName);
        // 存储失败,抛出异常
        if (!saveNickName) {
            throw new GameServerException.Builder(GameServerError.NICKNAME_EXIST).message(nickName).build();
        }
        // 获取一个全局 playerId
        Long playerId = playerDao.getNextPlayerId(zoneId);
        Player player = new Player();
        player.setPlayerId(playerId);
        player.setNickName(nickName);
        player.setLastLoginTime(System.currentTimeMillis());
        player.setCreateTime(System.currentTimeMillis());
        this.updatePlayerIdForNickName(zoneId, nickName, playerId);
        return player;
    }

    private boolean saveNickNameIfAbsent(String zoneId, String nickName) {
        // 生成存储的 key
        String key = redisUtil.getRedisKey(EnumRedisKey.PLAYER_NICKNAME, zoneId + "_" + nickName);
        // value 先使用一个默认值
        return redisUtil.setValueIfAbsent(key, "0");
    }

    private void updatePlayerIdForNickName(String zoneId, String nickName, long playerId) {
        String key = redisUtil.getRedisKey(EnumRedisKey.PLAYER_NICKNAME, zoneId + "_" + nickName);
        redisUtil.setValue(key, String.valueOf(playerId));
    }
}
