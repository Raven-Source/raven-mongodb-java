package org.raven.mongodb.repository.reactive;

public class UserReactiveRepositoryImpl extends ReactiveMongoRepositoryImpl<User, Long> {
    public UserReactiveRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }
}
