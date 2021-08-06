package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;

public class User2RepositoryImpl extends MongoRepositoryImpl<User2, Long> {
    public User2RepositoryImpl() {
        super(MongoSessionInstance.mongoSession, "User", new MongoSequence(), null);

    }
}