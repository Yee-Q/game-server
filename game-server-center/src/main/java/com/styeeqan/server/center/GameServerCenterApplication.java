package com.styeeqan.server.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author yeeq
 */
@SpringBootApplication(scanBasePackages = {"com.styeeqan.server"})
@EnableMongoRepositories(basePackages = {"com.styeeqan.server"})
public class GameServerCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameServerCenterApplication.class, args);
    }
}
