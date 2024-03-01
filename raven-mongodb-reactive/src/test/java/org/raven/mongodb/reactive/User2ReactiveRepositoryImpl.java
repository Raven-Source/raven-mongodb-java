package org.raven.mongodb.reactive;

public class User2ReactiveRepositoryImpl extends ReactiveMongoRepositoryImpl<User2, Long> {
    public User2ReactiveRepositoryImpl() {
        super(MongoSessionInstance.mongoSession, "User", null, null);

    }
}
