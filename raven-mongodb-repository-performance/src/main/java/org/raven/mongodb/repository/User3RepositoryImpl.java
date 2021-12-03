package org.raven.mongodb.repository;

import org.springframework.stereotype.Repository;

@Repository
public class User3RepositoryImpl extends MongoRepositoryImpl<User3, Long> {
    public User3RepositoryImpl(MongoSession mongoSession) {
        super(mongoSession);

    }
}
