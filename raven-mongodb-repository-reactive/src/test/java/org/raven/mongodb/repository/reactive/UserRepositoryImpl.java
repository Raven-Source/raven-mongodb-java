package org.raven.mongodb.repository.reactive;

public class UserRepositoryImpl extends ReactiveMongoRepositoryImpl<User, Long> {
    public UserRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }
}
