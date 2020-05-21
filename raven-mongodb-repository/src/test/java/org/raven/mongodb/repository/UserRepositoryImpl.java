package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;

public class UserRepositoryImpl extends MongoRepositoryImpl<User, Long> {
    public UserRepositoryImpl() {
        super("mongodb://127.0.0.1:27017/"
                , "RepositoryTest2", null, WriteConcern.ACKNOWLEDGED, null, null);

    }
}
