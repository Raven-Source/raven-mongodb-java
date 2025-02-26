package org.raven.mongodb.reactive;

import org.raven.mongodb.test.model.Mall;

public class Mall_CreateInstance_RepositoryImpl extends ReactiveMongoRepositoryImpl<Mall, String> {
    private Mall_CreateInstance_RepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }

    private static Mall_CreateInstance_RepositoryImpl createInstance() {
        return new Mall_CreateInstance_RepositoryImpl();
    }

}
