package org.raven.mongodb.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;

/**
 * @author yi.liang
 * @since JDK1.8
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

    @Getter
    private MongoDatabase database;

    /**
     * constructor
     *
     * @param uri            数据库链接uri
     * @param dbName         数据库名称
     * @param writeConcern   WriteConcern选项
     * @param readPreference
     */
    public DefaultMongoSession(String uri, String dbName, WriteConcern writeConcern, ReadPreference readPreference) {

        this.writeConcern = writeConcern != null ? writeConcern : WriteConcern.W1;
        this.readPreference = readPreference != null ? readPreference : ReadPreference.secondaryPreferred();

        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        mongoClient = new MongoClient(mongoClientURI);

        database = mongoClient.getDatabase(dbName).withReadPreference(this.readPreference).withWriteConcern(this.writeConcern);
    }
}
