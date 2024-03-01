package org.raven.mongodb;

import org.springframework.stereotype.Repository;

@Repository
public class User2RepositoryImpl extends MongoRepositoryImpl<User2, Long> {
    public User2RepositoryImpl(MongoSession mongoSession) {
        super(mongoSession, "User", new MongoSequence(), null);

    }
}