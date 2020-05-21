package org.raven.mongodb.repository.async;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoDatabase;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public class MongoSessionAsync {
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
     * 构造函数
     *
     * @param uri            数据库链接uri
     * @param dbName         数据库名称
     * @param writeConcern   WriteConcern选项
     * @param isSlaveOK
     * @param readPreference
     */
    public MongoSessionAsync(final String uri, final String dbName, final WriteConcern writeConcern, final Boolean isSlaveOK, final ReadPreference readPreference) {

        this._writeConcern = writeConcern != null ? writeConcern : WriteConcern.UNACKNOWLEDGED;
        this._readPreference = readPreference != null ? readPreference : ReadPreference.secondaryPreferred();

        _mongoClient = MongoClients.create(uri);

        //MongoClientURI mongoClientURI = new MongoClientURI(uri);
        //_mongoClient = new MongoClient(mongoClientURI);

        database = _mongoClient.getDatabase(dbName).withReadPreference(this._readPreference).withWriteConcern(this._writeConcern);
    }
}
