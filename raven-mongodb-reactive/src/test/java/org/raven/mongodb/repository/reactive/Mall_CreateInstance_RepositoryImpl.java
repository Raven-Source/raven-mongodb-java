package org.raven.mongodb.repository.reactive;

public class Mall_CreateInstance_RepositoryImpl extends ReactiveMongoRepositoryImpl<Mall, String> {
    private Mall_CreateInstance_RepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }

    private static Mall_CreateInstance_RepositoryImpl createInstance() {
        return new Mall_CreateInstance_RepositoryImpl();
    }

}
