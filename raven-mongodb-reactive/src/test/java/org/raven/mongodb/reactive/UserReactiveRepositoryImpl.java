package org.raven.mongodb.reactive;

import org.raven.mongodb.test.model.User;

public class UserReactiveRepositoryImpl extends ReactiveMongoRepositoryImpl<User, Long> {
    public UserReactiveRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }
}
