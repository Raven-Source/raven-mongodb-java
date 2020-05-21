package org.raven.mongodb.repository.stringID;

import com.mongodb.WriteConcern;
import org.raven.mongodb.repository.MongoRepositoryImpl;

public class User_StringIDRepository extends MongoRepositoryImpl<User_StringID, String> {
    public User_StringIDRepository() {
        super("mongodb://127.0.0.1:27017/"
            , "RepositoryTest2", null, WriteConcern.ACKNOWLEDGED, null, null);
    }
}
