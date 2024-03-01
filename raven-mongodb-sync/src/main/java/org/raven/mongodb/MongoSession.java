package org.raven.mongodb;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.ClientSession;
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

    default ClientSession startSession() {
        return getMongoClient().startSession();
    }

    default ClientSession startSession(ClientSessionOptions options) {
        return getMongoClient().startSession(options);
    }

}
