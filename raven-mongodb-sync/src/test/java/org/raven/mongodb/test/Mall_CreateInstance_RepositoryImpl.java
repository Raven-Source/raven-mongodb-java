package org.raven.mongodb.test;

import org.raven.mongodb.MongoRepositoryImpl;
import org.raven.mongodb.test.model.Mall;

public class Mall_CreateInstance_RepositoryImpl extends MongoRepositoryImpl<Mall, String> {
    private Mall_CreateInstance_RepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }

    private static Mall_CreateInstance_RepositoryImpl createInstance() {
        return new Mall_CreateInstance_RepositoryImpl();
    }

}
