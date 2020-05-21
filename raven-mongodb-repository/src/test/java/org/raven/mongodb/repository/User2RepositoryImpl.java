package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;

public class User2RepositoryImpl extends MongoRepositoryImpl<User2, Long> {
    public User2RepositoryImpl() {
        super("mongodb://127.0.0.1:27017/"
                , "RepositoryTest2", "User", WriteConcern.ACKNOWLEDGED, null, null);

    }
}