package org.raven.mongodb.repository.reactive;

public class User2RepositoryImpl extends ReactiveMongoRepositoryImpl<User2, Long> {
    public User2RepositoryImpl() {
        super(MongoSessionInstance.mongoSession, "User", null);

    }
}