package com.styeeqan.server.gateway.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.styeeqan.server.gateway.config.FilterConfig;
import com.styeeqan.server.common.util.JwtUtil;
import com.styeeqan.server.common.constant.CommonField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author yeeq
 * @date 2022/6/4
 */
@Slf4j
@Component
public class TokenVerifyFilter implements GlobalFilter, Ordered {

    @Autowired
    private FilterConfig filterConfig;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUri = exchange.getRequest().getURI().getPath();
        List<String> whiteRequestUris = filterConfig.getWhiteRequestUri();
        if (whiteRequestUris.contains(requestUri)) {
            return chain.filter(exchange);
        }
        String token = exchange.getRequest().getHeaders().getFirst(CommonField.TOKEN);
        if (StringUtils.isEmpty(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            log.info("{} 请求验证失败,token 为空", requestUri);
        }
        try {
            DecodedJWT tokenInfo = jwtUtil.getTokenInfo(token);
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header(CommonField.OPEN_ID, tokenInfo.getClaim(CommonField.OPEN_ID).asString())
                    .header(CommonField.USER_ID, tokenInfo.getClaim(CommonField.USER_ID).asString())
                    .build();
            ServerWebExchange newExchange = exchange.mutate().request(request).build();
            return chain.filter(newExchange);
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            log.info("{} 请求验证失败,token 非法", requestUri);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}
