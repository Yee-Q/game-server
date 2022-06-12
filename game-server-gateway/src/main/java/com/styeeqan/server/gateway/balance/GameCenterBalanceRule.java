package com.styeeqan.server.gateway.balance;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang.math.RandomUtils;

import java.util.List;

/**
 * @author yeeq
 * @date 2022/6/4
 */
public class GameCenterBalanceRule extends AbstractLoadBalancerRule {

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        List<Server> servers = this.getLoadBalancer().getReachableServers();
        if (servers.isEmpty()) {
            return null;
        }
        if (servers.size() == 1) {
            return servers.get(0);
        }
        if (key == null) {
            return randomChoose(servers);
        }
        return hashKeyChoose(servers, key);
    }

    /**
     * 根据 key 的 HashCode 选择一个服务实例
     * @param servers servers
     * @param key key
     * @return Server
     */
    private Server hashKeyChoose(List<Server> servers, Object key) {
        int hashCode = Math.abs(key.hashCode());
        int index = hashCode % servers.size();
        return servers.get(index);
    }

    /**
     * 随机选择一个服务实例
     * @param servers servers
     * @return Server
     */
    private Server randomChoose(List<Server> servers) {
        int randomIndex = RandomUtils.nextInt(servers.size());
        return servers.get(randomIndex);
    }
}
