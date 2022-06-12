package com.styeeqan.server.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Map;

/**
 * @author yeeq
 * @date 2022/6/4
 */
@Slf4j
@Component
public class JwtUtil {

    private final String tokenSecret = "game-server_token#jfaowf213";

    /**
     * 生成 token
     */
    public String getToken(Map<String, String> map) {
        // TOKEN 有效期七天
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);
        JWTCreator.Builder builder = JWT.create();
        // 创建 payload
        map.forEach(builder::withClaim);
        // 签名，并指定密钥
        return builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(tokenSecret));
    }

    /**
     * 获取 token 的信息
     */
    public DecodedJWT getTokenInfo(String token) {
        return JWT.require(Algorithm.HMAC256(tokenSecret)).build().verify(token);
    }
}
