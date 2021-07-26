package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;

/**
 * @author yi.liang
 * @since JDK11
 */
public class DefaultMongoSession implements MongoSession {

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
     * @return {@link com.mongodb.client.MongoDatabase}
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
    public DefaultMongoSession(String connectionString, String dbName, WriteConcern writeConcern, ReadPreference readPreference) {

        this.writeConcern = writeConcern != null ? writeConcern : WriteConcern.W1;
        this.readPreference = readPreference != null ? readPreference : ReadPreference.secondaryPreferred();


        mongoClient = MongoClients.create(connectionString);

        database = mongoClient.getDatabase(dbName).withReadPreference(this.readPreference).withWriteConcern(this.writeConcern);
    }
}