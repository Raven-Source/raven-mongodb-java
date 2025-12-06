package org.raven.mongodb.spring.sync.config;

import lombok.extern.slf4j.Slf4j;
import org.raven.mongodb.MongoSession;
import org.raven.mongodb.spring.sync.builder.MongoSessionBuilder;
import org.raven.mongodb.spring.common.config.MongoProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot auto-configuration for MongoDB synchronous operations.
 * <p>
 * This configuration automatically creates a MongoSession bean if one is not already defined.
 * It reads configuration from application properties using MongoProperties.
 * </p>
 *
 * <p><b>Note:</b> This is a default single datasource configuration.
 * For multiple datasources, disable this auto-configuration and create your own configuration.</p>
 *
 * <h2>Single Datasource Example (application.properties):</h2>
 * <pre>
 * mongodb.options.connString=mongodb://127.0.0.1:27017/
 * mongodb.options.dbName=TestDB
 * mongodb.options.writeConcern=ACKNOWLEDGED
 * mongodb.options.readPreference=PRIMARY
 * </pre>
 *
 * <h2>Disable Auto-Configuration (for multi-datasource):</h2>
 * <pre>
 * spring.autoconfigure.exclude=org.raven.mongodb.spring.sync.config.MongoAutoConfiguration
 * </pre>
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(MongoSession.class)
public class MongoAutoConfiguration {

    /**
     * Creates default MongoDB properties bean.
     * Binds to: mongodb.options.*
     *
     * @return MongoDB properties instance
     */
    @Bean
    @ConditionalOnMissingBean(name = "mongoProperties")
    @ConfigurationProperties(prefix = "mongodb.options")
    public MongoProperties mongoProperties() {
        return new MongoProperties();
    }

    /**
     * Creates a MongoSession bean if one is not already defined.
     *
     * @param properties MongoDB configuration properties
     * @return configured MongoSession instance
     */
    @Bean
    @ConditionalOnMissingBean
    public MongoSession mongoSession(MongoProperties properties) {
        log.info("Auto-configuring default MongoSession from mongodb.options.*");

        return MongoSessionBuilder.create()
            .properties(properties)
            .build();
    }
}
