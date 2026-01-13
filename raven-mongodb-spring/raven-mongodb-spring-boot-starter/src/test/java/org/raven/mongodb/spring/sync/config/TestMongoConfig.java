package org.raven.mongodb.spring.sync.config;

import org.raven.mongodb.DefaultMongoSession;
import org.raven.mongodb.MongoSession;
import org.raven.mongodb.spring.sync.annotation.EnableMongoRepositories;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Test configuration for sync MongoDB repositories.
 *
 * @author yi.liang
 * @since 3.1.0
 */
@SpringBootConfiguration
@EnableMongoRepositories(basePackages = "org.raven.mongodb.spring.sync.repository")
public class TestMongoConfig {

    @Bean
    public MongoSession mongoSession() {
        // Use test database
        return new DefaultMongoSession(
            "mongodb://127.0.0.1:27018/",
            "TestDB_Sync_Spring"
        );
    }
}
