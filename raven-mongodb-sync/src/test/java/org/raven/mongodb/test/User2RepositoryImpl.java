package org.raven.mongodb.test;

import org.raven.mongodb.MongoRepositoryImpl;
import org.raven.mongodb.MongoSequence;
import org.raven.mongodb.test.model.User2;

public class User2RepositoryImpl extends MongoRepositoryImpl<User2, Long> {
    public User2RepositoryImpl() {
        super(MongoSessionInstance.mongoSession, "User", new MongoSequence(), null);

    }
}