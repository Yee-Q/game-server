package com.styeeqan.server.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.styeeqan.server"})
public class GameServerGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameServerGatewayApplication.class, args);
    }
}
