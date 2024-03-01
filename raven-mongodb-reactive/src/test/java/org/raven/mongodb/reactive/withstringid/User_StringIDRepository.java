package org.raven.mongodb.reactive.withstringid;

import org.raven.mongodb.reactive.MongoSessionInstance;
import org.raven.mongodb.reactive.ReactiveMongoRepositoryImpl;
import org.raven.mongodb.model.User_StringID;

public class User_StringIDRepository extends ReactiveMongoRepositoryImpl<User_StringID, String> {
    public User_StringIDRepository() {
        super(MongoSessionInstance.mongoSession);
    }
}
