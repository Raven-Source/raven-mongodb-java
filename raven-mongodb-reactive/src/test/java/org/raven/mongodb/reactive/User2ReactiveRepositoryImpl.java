package org.raven.mongodb.reactive;

import org.raven.mongodb.test.model.User2;

public class User2ReactiveRepositoryImpl extends ReactiveMongoRepositoryImpl<User2, Long> {
    public User2ReactiveRepositoryImpl() {
        super(MongoSessionInstance.mongoSession, "User", null);

    }
}
