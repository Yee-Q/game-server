package com.styeeqan.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yeeq
 * @date 2022/5/25
 */
@Data
@Document(collation = "UserAccount")
public class UserAccount {

    /**
     * 用户的唯一Id,由服务器自己维护,保证全局唯一
     */
    private Long userId;

    /**
     * 用户的账号Id,一般是第三方SDK的openId
     */
    @Id
    private String openId;

    private Long createTime;

    private String ip;

    /**
     * 记录已创建角色的基本信息
     */
    private Map<String, ZonePlayerInfo> players = new HashMap<>();

    @Data
    @AllArgsConstructor
    public static class ZonePlayerInfo {

        /**
         * 此区内的角色Id
         */
        private long playerId;

        /**
         * 最近一次进入此区的时间
         */
        private long lastEnterTime;
    }
}
