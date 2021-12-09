package org.raven.mongodb.repository;

import ch.qos.logback.classic.LoggerContext;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import ch.qos.logback.classic.Level;

import ch.qos.logback.classic.Logger;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author by yanfeng
 * date 2021/11/17 17:09
 */
@Slf4j
@SpringBootApplication(exclude = {
        ErrorMvcAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
@EnableConfigurationProperties
public class Application {

    public static void main(String[] args) throws Exception {

        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.run(args);

    }
}
