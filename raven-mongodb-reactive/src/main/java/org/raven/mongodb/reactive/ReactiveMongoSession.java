package org.raven.mongodb.reactive;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;

/**
 * @author yi.liang
 * date 2021.07.26 17:06
 */
public interface ReactiveMongoSession {

    /**
     * @return {@link com.mongodb.reactivestreams.client.MongoDatabase}
     */
    MongoDatabase getDatabase();

    /**
     * @return {@link MongoClient}
     */
    MongoClient getMongoClient();
}
