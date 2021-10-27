package org.raven.mongodb.repository;

public class User3RepositoryImpl extends MongoRepositoryImpl<User3, Long> {
    public User3RepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }
}
