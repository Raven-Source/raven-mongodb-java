package org.raven.mongodb.repository;

import org.raven.mongodb.repository.model.User3;

public class User3RepositoryImpl extends MongoRepositoryImpl<User3, Long> {
    public User3RepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }
}
