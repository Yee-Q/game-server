package com.styeeqan.server.common.http.response;

import lombok.Data;

/**
 * @author yeeq
 * @date 2022/6/3
 */
@Data
public class LoginResult {

    private Long userId;
    private String token;
}
