package org.raven.mongodb.spring.reactive.config;

import org.raven.mongodb.reactive.DefaultReactiveMongoSession;
import org.raven.mongodb.reactive.ReactiveMongoSession;
import org.raven.mongodb.spring.reactive.annotation.EnableReactiveMongoRepositories;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Test configuration for reactive MongoDB repositories.
 *
 * @author yi.liang
 * @since 3.1.0
 */
@SpringBootConfiguration
@EnableReactiveMongoRepositories(basePackages = "org.raven.mongodb.spring.reactive.repository")
public class TestReactiveMongoConfig {

    @Bean
    public ReactiveMongoSession reactiveMongoSession() {
        // Use test database
        return new DefaultReactiveMongoSession(
            "mongodb://127.0.0.1:27017/",
            "TestDB_Reactive_Spring"
        );
    }
}
