package org.raven.mongodb.config;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import org.raven.mongodb.DefaultMongoSession;
import org.raven.mongodb.MongoSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * @author yanfen
 */
@Configuration
public class DataSourceConfiguration {

    @Value("${mongodb.options.maxCommitTime:10}")
    private long maxCommitTime;

    @Primary
    @Bean(MongoSessionConstant.MongoSession)
    public MongoSession mongoSessionMain(@Qualifier("mongoPropertiesUser") MongoProperties mongoProperties) {

        DefaultMongoSession defaultMongoSession = new DefaultMongoSession(
                mongoProperties.getConnString(),
                mongoProperties.getDbName(),
                null,
                null
        );

        return defaultMongoSession;

    }

    @Bean(MongoSessionConstant.MongoSessionMpAdmin)
    public MongoSession mongoSessionAdmin(@Qualifier("mongoPropertiesAdmin") MongoProperties mongoProperties) {

        DefaultMongoSession defaultMongoSession = new DefaultMongoSession(
                mongoProperties.getConnString(),
                mongoProperties.getDbName(),
                null,
                null
        );

        return defaultMongoSession;

    }

    @Bean
    public TransactionOptions txnOptions() {
        TransactionOptions txnOptions = TransactionOptions.builder()
                .readPreference(ReadPreference.secondaryPreferred())
                .readConcern(ReadConcern.MAJORITY)
                .writeConcern(WriteConcern.W1)
                .maxCommitTime(maxCommitTime, TimeUnit.SECONDS)
                .build();

        return txnOptions;
    }

}
