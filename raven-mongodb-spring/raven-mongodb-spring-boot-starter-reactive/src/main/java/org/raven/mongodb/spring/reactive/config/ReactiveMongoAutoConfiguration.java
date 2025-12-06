package org.raven.mongodb.spring.reactive.config;

import lombok.extern.slf4j.Slf4j;
import org.raven.mongodb.reactive.ReactiveMongoSession;
import org.raven.mongodb.spring.reactive.builder.ReactiveMongoSessionBuilder;
import org.raven.mongodb.spring.common.config.MongoProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot auto-configuration for MongoDB reactive operations.
 * <p>
 * This configuration automatically creates a ReactiveMongoSession bean if one is not already defined.
 * It reads configuration from application properties using MongoProperties.
 * </p>
 *
 * <p><b>Note:</b> This is a default single datasource configuration.
 * For multiple datasources, disable this auto-configuration and create your own configuration.
 *
 * <h2>Single Datasource Example (application.properties):</h2>
 * <pre>
 * mongodb.reactive.options.connString=mongodb://127.0.0.1:27017/
 * mongodb.reactive.options.dbName=TestDB
 * mongodb.reactive.options.writeConcern=ACKNOWLEDGED
 * mongodb.reactive.options.readPreference=PRIMARY
 * </pre>
 *
 * <h2>Disable Auto-Configuration (for multi-datasource):</h2>
 * <pre>
 * spring.autoconfigure.exclude=org.raven.mongodb.spring.reactive.config.ReactiveMongoAutoConfiguration
 * </pre>
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(ReactiveMongoSession.class)
public class ReactiveMongoAutoConfiguration {

    /**
     * Creates default reactive MongoDB properties bean.
     * Binds to: mongodb.reactive.options.*
     */
    @Bean
    @ConditionalOnMissingBean(name = "reactiveMongoProperties")
    @ConfigurationProperties(prefix = "mongodb.reactive.options")
    public MongoProperties reactiveMongoProperties() {
        return new MongoProperties();
    }

    /**
     * Creates a ReactiveMongoSession bean if one is not already defined.
     *
     * @param properties MongoDB reactive configuration properties
     * @return configured ReactiveMongoSession instance
     */
    @Bean
    @ConditionalOnMissingBean
    public ReactiveMongoSession reactiveMongoSession(MongoProperties properties) {
        log.info("Auto-configuring default ReactiveMongoSession from mongodb.reactive.options.*");

        return ReactiveMongoSessionBuilder.create()
            .properties(properties)
            .build();
    }
}
