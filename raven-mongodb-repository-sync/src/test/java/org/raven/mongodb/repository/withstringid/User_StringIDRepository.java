package org.raven.mongodb.repository.withstringid;

import org.raven.mongodb.repository.MongoRepositoryImpl;
import org.raven.mongodb.repository.MongoSessionInstance;
import org.raven.mongodb.repository.model.User_StringID;

public class User_StringIDRepository extends MongoRepositoryImpl<User_StringID, String> {
    public User_StringIDRepository() {
        super(MongoSessionInstance.mongoSession);
    }
}
