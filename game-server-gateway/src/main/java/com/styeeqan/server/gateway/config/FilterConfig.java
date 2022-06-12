package com.styeeqan.server.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author yeeq
 * @date 2022/6/4
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "gateway.filter")
public class FilterConfig {

    /**
     * 请求白名单,在白名单中的 URI 不进行权限校验
     */
    private List<String> whiteRequestUri;

    /**
     * 针对所有用户限流器每秒产生的令牌数
     */
    private double globalRequestRateCount;

    /**
     * 针对单个用户限流器每秒产生的令牌数
     */
    private double userRequestRateCount;

    /**
     * 最大限流用户缓存数量
     */
    private int cacheUserMaxCount;

    /**
     * 每个用户缓存的超时时间,超过规定时间,从缓存中清除
     */
    private int cacheUserTimeout;
}
