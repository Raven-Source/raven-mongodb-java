package org.raven.mongodb.test;

import org.raven.mongodb.MongoRepositoryImpl;
import org.raven.mongodb.test.model.Mall;

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

