package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;

public class Mall_CreateInstance_RepositoryImpl extends MongoRepositoryImpl<Mall, String> {
    private Mall_CreateInstance_RepositoryImpl() {
        super("mongodb://127.0.0.1:27017/"
            , "RepositoryTest2", null, WriteConcern.ACKNOWLEDGED, null, null);

    }

    private static Mall_CreateInstance_RepositoryImpl createInstance() {
        return new Mall_CreateInstance_RepositoryImpl();
    }

}
