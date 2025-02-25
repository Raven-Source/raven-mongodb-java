package org.raven.mongodb.test;

import org.raven.mongodb.MongoRepositoryImpl;
import org.raven.mongodb.test.model.User3;

public class User3RepositoryImpl extends MongoRepositoryImpl<User3, Long> {
    public User3RepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }
}
