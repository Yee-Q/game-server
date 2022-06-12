package com.styeeqan.server.redis;

import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * @author yeeq
 * @date 2022/5/25
 */
public enum EnumRedisKey {

    USER_ID_INCR(null), // UserId自增key
    USER_ACCOUNT(Duration.ofDays(7)),    // 用户信息
    PLAYER_NICKNAME(Duration.ofDays(7)), // 昵称
    PLAYER_ID_INCR(null) // PlayerId自增key
    ;

    /**
     * Redis 的 key 的过期时间,如果为 null,表示 value 永不过期
     */
    private Duration timeout;

    private EnumRedisKey() {

    }

    private EnumRedisKey(Duration timeout) {
        this.timeout = timeout;
    }

    public String getKey(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        return this.name() + "_" + id;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public String getKey() {
        return this.name();
    }
}
