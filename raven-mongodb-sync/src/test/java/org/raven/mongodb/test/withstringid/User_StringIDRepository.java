package org.raven.mongodb.test.withstringid;

import org.raven.mongodb.MongoRepositoryImpl;
import org.raven.mongodb.test.MongoSessionInstance;
import org.raven.mongodb.test.model.User_StringID;

public class User_StringIDRepository extends MongoRepositoryImpl<User_StringID, String> {
    public User_StringIDRepository() {
        super(MongoSessionInstance.mongoSession);
    }
}
