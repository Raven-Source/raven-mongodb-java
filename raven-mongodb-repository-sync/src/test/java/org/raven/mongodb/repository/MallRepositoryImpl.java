package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;

public class MallRepositoryImpl extends MongoRepositoryImpl<Mall, String> {
    public MallRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }

    public static class MallRepositoryImpl2 extends MongoRepositoryImpl<Mall, String> {
        public MallRepositoryImpl2() {
            super(MongoSessionInstance.mongoSession);

        }
    }

}

