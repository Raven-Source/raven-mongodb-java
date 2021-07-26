package org.raven.mongodb.repository.reactive.withstringid;

import org.raven.mongodb.repository.reactive.MongoSessionInstance;
import org.raven.mongodb.repository.reactive.ReactiveMongoRepositoryImpl;

public class User_StringIDRepository extends ReactiveMongoRepositoryImpl<User_StringID, String> {
    public User_StringIDRepository() {
        super(MongoSessionInstance.mongoSession);
    }
}
