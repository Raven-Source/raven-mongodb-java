package org.raven.mongodb.reactive;

public class UserReactiveRepositoryImpl extends ReactiveMongoRepositoryImpl<User, Long> {
    public UserReactiveRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }
}
