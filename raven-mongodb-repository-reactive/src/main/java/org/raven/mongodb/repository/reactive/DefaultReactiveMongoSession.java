package org.raven.mongodb.repository.reactive;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.Getter;
import lombok.NonNull;
import org.raven.mongodb.repository.MongoOptions;

/**
 * @author yi.liang
 * @since JDK11
 */
public class DefaultReactiveMongoSession implements ReactiveMongoSession {

    /**
     * MongoClient
     */
    private MongoClient mongoClient;

    /**
     * MongoDB WriteConcern
     */
    @Getter
    private WriteConcern writeConcern;

    /**
     * MongoDB ReadPreference
     */
    @Getter
    private ReadPreference readPreference;

    /**
     * @return {@link com.mongodb.reactivestreams.client.MongoDatabase}
     */
    @Getter
    private MongoDatabase database;

    /**
     * constructor
     *
     * @param connectionString connectionString
     * @param dbName           dbName
     * @param writeConcern     WriteConcern选项
     * @param readPreference
     */
    public DefaultReactiveMongoSession(@NonNull String connectionString, @NonNull String dbName, WriteConcern writeConcern, ReadPreference readPreference) {

        this.writeConcern = writeConcern != null ? writeConcern : WriteConcern.W1;
        this.readPreference = readPreference != null ? readPreference : ReadPreference.secondaryPreferred();


        mongoClient = MongoClients.create(connectionString);

        database = mongoClient.getDatabase(dbName).withReadPreference(this.readPreference).withWriteConcern(this.writeConcern);
    }

    /**
     * constructor
     *
     * @param mongoOptions mongoOptions
     */
    public DefaultReactiveMongoSession(MongoOptions mongoOptions) {
        this(mongoOptions.getConnString(), mongoOptions.getDbName(), mongoOptions.getWriteConcern(), mongoOptions.getReadPreference());
    }
}
