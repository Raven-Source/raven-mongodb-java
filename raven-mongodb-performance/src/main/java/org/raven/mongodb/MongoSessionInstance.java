package org.raven.mongodb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MongoSessionInstance {

    @Value("${mongodb.options.connString}")
    private String cstr = "";

    @Value("${mongodb.options.dbName}")
    private String dbName = "";

//    private static final String cstr = "mongodb://127.0.0.1:27017/";

    @Bean
    public MongoSession init() {
        return new DefaultMongoSession(cstr
                , dbName, null, null);
    }
}
