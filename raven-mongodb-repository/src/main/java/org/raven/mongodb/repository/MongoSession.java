package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public interface MongoSession {

//    /**
//     * @return {@link com.mongodb.WriteConcern}
//     */
//    WriteConcern getWriteConcern();
//
//    /**
//     * @return {@link com.mongodb.ReadPreference}
//     */
//    ReadPreference getReadPreference();

    /**
     * @return {@link com.mongodb.client.MongoDatabase}
     */
    MongoDatabase getDatabase();

}
