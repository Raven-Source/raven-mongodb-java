package org.raven.mongodb;

import org.raven.mongodb.model.User3;

public class User3RepositoryImpl extends MongoRepositoryImpl<User3, Long> {
    public User3RepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }
}
