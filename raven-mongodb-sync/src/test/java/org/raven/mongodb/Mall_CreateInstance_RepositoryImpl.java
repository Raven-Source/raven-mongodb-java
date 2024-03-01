package org.raven.mongodb;

import org.raven.mongodb.model.Mall;

public class Mall_CreateInstance_RepositoryImpl extends MongoRepositoryImpl<Mall, String> {
    private Mall_CreateInstance_RepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }

    private static Mall_CreateInstance_RepositoryImpl createInstance() {
        return new Mall_CreateInstance_RepositoryImpl();
    }

}
