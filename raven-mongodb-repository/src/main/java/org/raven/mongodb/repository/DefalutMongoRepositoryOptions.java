package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

/**
 * @author yi.liang
 * @date 2018.11.08 18:03
 * @since JDK1.8
 */
public class DefalutMongoRepositoryOptions implements MongoRepositoryOptions {

    /**
     * 数据库连接节点
     */
    private String connString;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 数据库集合名称(非必须)
     */
    private String collectionName;

    /**
     * WriteConcern(非必须)
     */
    private WriteConcern writeConcern;

    /**
     * ReadPreference(非必须)
     */
    private ReadPreference readPreference;

    /**
     * Mongo自增长ID数据序列对象(非必须)
     */
    private MongoSequence sequence;

    @Override
    public String getConnString() {
        return connString;
    }

    public DefalutMongoRepositoryOptions setConnString(String connString) {
        this.connString = connString;
        return this;
    }

    @Override
    public String getDbName() {
        return dbName;
    }

    public DefalutMongoRepositoryOptions setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    @Override
    public String getCollectionName() {
        return collectionName;
    }

    public DefalutMongoRepositoryOptions setCollectionName(String collectionName) {
        this.collectionName = collectionName;
        return this;
    }

    @Override
    public WriteConcern getWriteConcern() {
        return writeConcern;
    }

    public DefalutMongoRepositoryOptions setWriteConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    @Override
    public ReadPreference getReadPreference() {
        return readPreference;
    }

    public DefalutMongoRepositoryOptions setReadPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
        return this;
    }

    @Override
    public MongoSequence getSequence() {
        return sequence;
    }

    public DefalutMongoRepositoryOptions setSequence(MongoSequence sequence) {
        this.sequence = sequence;
        return this;
    }

    public DefalutMongoRepositoryOptions(){}

    /**
     * constructor
     *
     * @param connString     数据库连接节点(必须)
     * @param dbName         数据库连接节点(必须)
     * @param collectionName 数据库集合名称(非必须)
     * @param writeConcern   WriteConcern(非必须)
     * @param readPreference ReadPreference(非必须)
     * @param sequence       Mongo自增长ID数据序列对象(非必须)
     */
    public DefalutMongoRepositoryOptions(final String connString, final String dbName, final String collectionName, final WriteConcern writeConcern, final ReadPreference readPreference, final MongoSequence sequence) {
        this.connString = connString;
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.writeConcern = writeConcern;
        this.readPreference = readPreference;
        this.sequence = sequence;
    }

}
