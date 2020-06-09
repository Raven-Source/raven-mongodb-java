package org.raven.mongodb.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public class MongoSession {

    /**
     * MongoDB WriteConcern
     */
    private WriteConcern _writeConcern;

    public WriteConcern get_writeConcern() {
        return _writeConcern;
    }

    /**
     * MongoDB ReadPreference
     */
    private ReadPreference _readPreference;

    public ReadPreference get_readPreference() {
        return _readPreference;
    }

    /**
     * MongoClient
     */
    private MongoClient _mongoClient;

    /**
     * @return MongoDatabase
     */
    public MongoDatabase getDatabase() {
        return database;
    }

    private MongoDatabase database;

    /**
     * constructor
     *
     * @param uri            数据库链接uri
     * @param dbName         数据库名称
     * @param writeConcern   WriteConcern选项
     * @param isSlaveOK
     * @param readPreference
     */
    public MongoSession(final String uri, final String dbName, final WriteConcern writeConcern, final Boolean isSlaveOK, final ReadPreference readPreference) {

        this._writeConcern = writeConcern != null ? writeConcern : WriteConcern.W1;
        this._readPreference = readPreference != null ? readPreference : ReadPreference.secondaryPreferred();

        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        _mongoClient = new MongoClient(mongoClientURI);

        database = _mongoClient.getDatabase(dbName).withReadPreference(this._readPreference).withWriteConcern(this._writeConcern);
    }


}
