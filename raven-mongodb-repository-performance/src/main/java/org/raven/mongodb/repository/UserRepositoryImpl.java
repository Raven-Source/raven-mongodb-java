package org.raven.mongodb.repository;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends MongoRepositoryImpl<User, Long> {
    public UserRepositoryImpl(MongoSession mongoSession) {
        super(mongoSession);

    }
}
