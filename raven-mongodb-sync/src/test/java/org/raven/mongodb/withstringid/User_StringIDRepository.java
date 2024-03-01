package org.raven.mongodb.withstringid;

import org.raven.mongodb.MongoRepositoryImpl;
import org.raven.mongodb.MongoSessionInstance;
import org.raven.mongodb.model.User_StringID;

public class User_StringIDRepository extends MongoRepositoryImpl<User_StringID, String> {
    public User_StringIDRepository() {
        super(MongoSessionInstance.mongoSession);
    }
}
