package org.raven.mongodb.repository.stringID;

import com.mongodb.WriteConcern;
import org.raven.mongodb.repository.MongoRepositoryImpl;
import org.raven.mongodb.repository.MongoSessionInstance;

public class User_StringIDRepository extends MongoRepositoryImpl<User_StringID, String> {
    public User_StringIDRepository() {
        super(MongoSessionInstance.mongoSession);
    }
}
