package org.raven.mongodb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * date 2022/11/14 16:52
 */
@Configuration
public class MongoPropertiesConfiguration {

    @ConfigurationProperties(prefix = "mongodb.options.user")
    @Bean("mongoPropertiesUser")
    @Primary
    public MongoProperties mongoProperties() {
        return new MongoProperties();
    }

    @ConfigurationProperties(prefix = "mongodb.options.admin")
    @Bean("mongoPropertiesAdmin")
    public MongoProperties mongoPropertiesAdmin() {
        return new MongoProperties();

    }

}
