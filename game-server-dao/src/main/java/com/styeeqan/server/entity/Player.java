package com.styeeqan.server.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author yeeq
 * @date 2022/5/25
 */
@Data
@Document(collation = "Player")
public class Player {

    @Id
    private Long playerId;

    private String nickName;

    private Integer level;

    private Long lastLoginTime;

    private Long createTime;
}
