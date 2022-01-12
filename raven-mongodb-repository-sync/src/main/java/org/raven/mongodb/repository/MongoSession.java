package org.raven.mongodb.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * @author yi.liang
 * @since JDK11
 */
public interface MongoSession {

    /**
     * @return {@link com.mongodb.client.MongoDatabase}
     */
    MongoDatabase getDatabase();

    /**
     * @return {@link com.mongodb.client.MongoClient}
     */
    MongoClient getMongoClient();

}
