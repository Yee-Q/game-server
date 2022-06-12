package com.styeeqan.server.gateway.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.styeeqan.server.common.constant.CommonField;
import com.styeeqan.server.common.util.JwtUtil;
import com.styeeqan.server.gateway.config.FilterConfig;
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

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yeeq
 * @date 2022/6/4
 */
@Slf4j
@Component
public class RequestRateLimiterFilter implements GlobalFilter, Ordered {

    @Autowired
    private FilterConfig filterConfig;

    /**
     * 针对所有用户的限流器
     */
    private RateLimiter globalRateLimter;

    /**
     * 单个用户的流量限制缓存
     */
    private LoadingCache<String, RateLimiter> userRateLimterCache;

    @PostConstruct
    public void init() {
        double permitsPerSecond = filterConfig.getGlobalRequestRateCount();
        globalRateLimter = RateLimiter.create(permitsPerSecond);
        // 创建用户 cache
        int maximumSize = filterConfig.getCacheUserMaxCount();
        int duration = filterConfig.getCacheUserTimeout();
        userRateLimterCache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(duration, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<>() {
                    @Override
                    public RateLimiter load(String key) {
                        double permitsPerSecond = filterConfig.getUserRequestRateCount();
                        return RateLimiter.create(permitsPerSecond);
                    }
                });
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取 openId
        String openId = exchange.getRequest().getHeaders().getFirst(CommonField.OPEN_ID);
        // 如果存在 openId 判断个人限流
        if (!StringUtils.isEmpty(openId)) {
            try {
                RateLimiter userRateLimiter = userRateLimterCache.get(openId);
                // 获取令牌失败,触发限流
                if (!userRateLimiter.tryAcquire()) {
                    return this.tooManyRequest(exchange, chain);
                }
            } catch (Exception e) {
                log.error("限流器异常", e);
                return this.tooManyRequest(exchange, chain);
            }
        }
        // 全局限流判断
        if (!globalRateLimter.tryAcquire()) {
            return this.tooManyRequest(exchange, chain);
        }
        // 成功获取令牌,放行
        return chain.filter(exchange);
    }

    private Mono<Void> tooManyRequest(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("触发限流");
        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
