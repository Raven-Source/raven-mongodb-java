package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;

public class UserRepositoryImpl extends MongoRepositoryImpl<User, Long> {
    public UserRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }
}
