package org.raven.mongodb;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author by yanfeng
 * date 2021/11/17 17:09
 */
@Slf4j
@SpringBootApplication(scanBasePackageClasses = MongoSessionInstance.class)
@EnableConfigurationProperties
public class Application {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.run(args);

    }
}
