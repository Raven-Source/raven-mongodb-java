package org.raven.mongodb;

import org.raven.mongodb.model.User2;

public class User2RepositoryImpl extends MongoRepositoryImpl<User2, Long> {
    public User2RepositoryImpl() {
        super(MongoSessionInstance.mongoSession, "User", new MongoSequence(), null);

    }
}