package org.raven.mongodb.reactive;

import com.mongodb.ClientSessionOptions;
import com.mongodb.reactivestreams.client.ClientSession;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.reactivestreams.Publisher;

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

    default Publisher<ClientSession> startSession() {
        return getMongoClient().startSession();
    }

    default Publisher<ClientSession> startSession(ClientSessionOptions options) {
        return getMongoClient().startSession(options);
    }
}
