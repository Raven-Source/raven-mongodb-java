package org.raven.mongodb;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author yi.liang
 */
public class DefaultMongoSession implements MongoSession {

    /**
     * MongoClient
     */
    @Getter
    private final MongoClient mongoClient;

    /**
     * MongoDB WriteConcern
     */
    @Getter
    private final WriteConcern writeConcern;

    /**
     * MongoDB ReadPreference
     */
    @Getter
    private final ReadPreference readPreference;

    /**
     * @return MongoDatabase {@link com.mongodb.client.MongoDatabase}
     */
    @Getter
    private final MongoDatabase database;

    /**
     * constructor
     *
     * @param connectionString connectionString
     * @param dbName           dbName
     * @param writeConcern     WriteConcern
     * @param readPreference   ReadPreference
     */
    public DefaultMongoSession(@NonNull String connectionString, @NonNull String dbName, WriteConcern writeConcern, ReadPreference readPreference) {

        this.writeConcern = writeConcern != null ? writeConcern : WriteConcern.W1;
        this.readPreference = readPreference != null ? readPreference : ReadPreference.secondaryPreferred();

        mongoClient = MongoClients.create(connectionString);

        database = mongoClient.getDatabase(dbName).withReadPreference(this.readPreference).withWriteConcern(this.writeConcern);
    }


    /**
     * constructor
     *
     * @param connectionString connectionString
     * @param dbName           dbName
     */
    public DefaultMongoSession(@NonNull String connectionString, @NonNull String dbName) {
        this(connectionString, dbName, null, null);
    }

}
